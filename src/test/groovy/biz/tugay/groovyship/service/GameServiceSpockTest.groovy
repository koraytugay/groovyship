package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Game
import biz.tugay.groovyship.modal.Ship
import spock.lang.Specification

class GameServiceSpockTest
    extends Specification
{
  GameService gameService = new GameService()

  BoardService boardService = new BoardService()

  def "createNewGame"() {
    given:
      def game = gameService.createNewGame(4, 2)

    expect:
      game.missileHistory.isEmpty()
      game.currentHistoryStamp == -1
      game.board.ships.size() == 2
      game.board.missileAttempts.isEmpty()
  }

  def "testSendMissile"() {
    given:
      def board = new Board(5)
      def ship = new Ship(Coordinate.of(0, 0))
      boardService.addShip(board, ship)
      def game = new Game(board)
      def isHit

    when:
      (game, isHit) = gameService.sendMissile(game, 0, 0)

    then:
      isHit
      ship.coordinateIsHitByMissileMap.get(Coordinate.of(0, 0))
      board.missileAttempts.contains(Coordinate.of(0, 0))
      game.missileHistory.size() == 1
      game.missileHistory.get(0) == Coordinate.of(0, 0)
      game.currentHistoryStamp == 0

  }

  def "testSendMissile_Miss"() {
    given:
      def board = new Board(5)
      def ship = new Ship(Coordinate.of(0, 0))
      boardService.addShip(board, ship)
      def game = new Game(board)
      def isHit

    when:
      (game, isHit) = gameService.sendMissile(game, 1, 1)

    then:
      !isHit
      !ship.coordinateIsHitByMissileMap.get(Coordinate.of(0, 0))
      board.missileAttempts.contains(Coordinate.of(1, 1))
      game.missileHistory.size() == 1
      game.missileHistory.get(0) == Coordinate.of(1, 1)
      game.currentHistoryStamp == 0
  }

  def "testUndoRedo"() {
    given:
      def coordinate00 = Coordinate.of(0, 0)
      def coordinate10 = Coordinate.of(1, 0)
      def board = new Board(4)
      def ship = new Ship(coordinate00, coordinate10)
      boardService.addShip(board, ship)
      def game = new Game(board)

    when:
      gameService.sendMissile(game, coordinate00.column, coordinate00.row)
      gameService.sendMissile(game, coordinate10.column, coordinate10.row)

    then:
      ship.coordinateIsHitByMissileMap.get(coordinate00)
      ship.coordinateIsHitByMissileMap.get(coordinate10)
      board.missileAttempts.containsAll(coordinate00, coordinate10)
      game.missileHistory.containsAll(coordinate00, coordinate10)
      game.currentHistoryStamp == 1

    when:
      gameService.undo(game)

    then:
      ship.coordinateIsHitByMissileMap.get(coordinate00)
      !ship.coordinateIsHitByMissileMap.get(coordinate10)
      board.missileAttempts.contains(coordinate00)
      !board.missileAttempts.contains(coordinate10)
      game.missileHistory.contains(coordinate00)
      game.missileHistory.contains(coordinate10)
      game.currentHistoryStamp == 0

    when:
      gameService.redo(game)

    then:
      ship.coordinateIsHitByMissileMap.get(coordinate00)
      ship.coordinateIsHitByMissileMap.get(coordinate10)
      board.missileAttempts.containsAll(coordinate00, coordinate10)
      game.missileHistory.containsAll(coordinate00, coordinate10)
      game.currentHistoryStamp == 1
  }

  def "overwrite after redo"() {
    given:
      def board = new Board(4)
      def game = new Game(board)
      gameService.sendMissile(game, 0, 0)
      gameService.sendMissile(game, 1, 0)
      gameService.sendMissile(game, 2, 0)
      gameService.sendMissile(game, 3, 0)

    when:
      gameService.undo(game)
      gameService.undo(game)
      gameService.undo(game)

    then:
      game.board.missileAttempts.size() == 1
      game.board.missileAttempts.contains(Coordinate.of(0, 0))

    when:
      gameService.sendMissile(game, 0, 1)

    then:
      game.board.missileAttempts.size() == 2
      game.board.missileAttempts.containsAll(Coordinate.of(0, 0), Coordinate.of(0, 1))

    when:
      gameService.redo(game)

    then:
      game.board.missileAttempts.size() == 2
      game.board.missileAttempts.containsAll(Coordinate.of(0, 0), Coordinate.of(0, 1))
  }

  def "testAllShipsSank"() {
    given:
      def ship = new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0))
      def board = new Board(4)
      boardService.addShip(board, ship)
      def game = new Game(board)
      boardService.missileCoordinate(board, Coordinate.of(0, 0))
      boardService.missileCoordinate(board, Coordinate.of(1, 0))

    expect:
      gameService.allShipsSank(game)
  }

  def "testUndoWhenNothingToUndo"() {
    given:
      def ship = new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0))
      def board = new Board(4)
      boardService.addShip(board, ship)
      def game = new Game(board)

    when:
      gameService.undo(game)

    then:
      board.missileAttempts.isEmpty()
      game.missileHistory.isEmpty()
      game.currentHistoryStamp == -1
  }
}
