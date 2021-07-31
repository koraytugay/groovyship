package biz.tugay.groovyship.service

import biz.tugay.groovyship.commons.GroovyShipConstants
import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Game

class GameService
{
  ShipService shipService = new ShipService()

  BoardService boardService = new BoardService()

  Game createNewGame(int boardSize, int numberOfShips) {
    if (numberOfShips < GroovyShipConstants.MINIMUM_NUMBER_OF_SHIPS) {
      throw new IllegalArgumentException("At least $GroovyShipConstants.MINIMUM_NUMBER_OF_SHIPS must be included.")
    }

    def board = null

    int numberOfAttempts = 0
    while (!board && ++numberOfAttempts < 100) {
      board = boardWithRandomShips boardSize, numberOfShips
    }

    if (!board) {
      return null
    }

    return new Game(board)
  }

  def sendMissile(Game game, int column, int row) {
    def history = game.missileHistory.subList(0, game.currentHistoryStamp + 1)
    game.missileHistory = [*history, Coordinate.of(column, row)]
    game.currentHistoryStamp = game.currentHistoryStamp + 1

    def isHit = boardService.missileCoordinate(game.board, Coordinate.of(column, row))
    return [game, isHit]
  }

  Game undo(Game game) {
    if (game.currentHistoryStamp == -1) {
      return game
    }

    boardService.unMissileCoordinate(game.board, game.missileHistory[game.currentHistoryStamp])
    game.currentHistoryStamp = game.currentHistoryStamp - 1

    return game
  }

  Game redo(Game game) {
    if (game.currentHistoryStamp == game.missileHistory.size() - 1) {
      return game
    }
    boardService.missileCoordinate(game.board, game.missileHistory[game.currentHistoryStamp + 1])
    game.currentHistoryStamp = game.currentHistoryStamp + 1
    return game
  }

  boolean allShipsSank(Game game) {
    return boardService.allShipsSank(game.board)
  }

  private Board boardWithRandomShips(int boardSize, int numberOfShips) {
    def numberOfAttempts = 0
    def board = new Board(boardSize)
    def numberOfShipsPlaced = 0

    while (numberOfShipsPlaced < numberOfShips) {
      def ship = shipService.newRandomShip(boardSize)

      numberOfAttempts++
      if (boardService.addShip(board, ship)) {
        numberOfShipsPlaced++
      }

      if (numberOfAttempts > 100) {
        return null
      }
    }

    return board
  }
}
