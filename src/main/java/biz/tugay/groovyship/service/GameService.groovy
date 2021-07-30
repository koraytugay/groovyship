package biz.tugay.groovyship.service

import biz.tugay.groovyship.commons.GroovyShipConstants
import biz.tugay.groovyship.modal.Board

/**
 * Responsible for creating a new board with the desired size
 * and desired number of ships.
 *
 * This is the only service that should be consumed for creating
 * and interacting with the game as it exposes the required methods
 * such as sendMissile and allShipsSank.
 */
class GameService
{
  ShipService shipService = new ShipService()

  BoardService boardService = new BoardService()

  Board createNewGame(int boardSize, int numberOfShips) {
    if (numberOfShips < GroovyShipConstants.MINIMUM_NUMBER_OF_SHIPS) {
      throw new IllegalArgumentException("At least $GroovyShipConstants.MINIMUM_NUMBER_OF_SHIPS must be included.")
    }

    def board = null

    int numberOfAttempts = 0
    while (!board && ++numberOfAttempts < 100) {
      board = boardWithRandomShips(boardSize, numberOfShips)
    }

    return board
  }

  def sendMissile(Board board, int column, int row) {
    return boardService.missileCoordinate(board, column, row)
  }

  boolean allShipsSank(Board board) {
    return boardService.allShipsSank(board)
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

      // We are unable to find a way to place $numberOfShips of ships in this board the way we placed
      // the existing ships so far. Return null - caller may do another attempt if it wants to.
      // The way we place the first few ships will determine whether a solution can be found or not.
      if (numberOfAttempts > 100) {
        return null
      }
    }

    return board
  }
}
