package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Game
import biz.tugay.groovyship.modal.Ship
import org.junit.jupiter.api.Test
import org.testng.collections.Sets;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest
{
  GameService gameService = new GameService()

  @Test
  void testCreateNewGame() {
    def game = gameService.createNewGame(9, 2)

    assertTrue(game.board.ships.size() == 2)
    assertTrue(game.board.missileAttempts.isEmpty())

    assertTrue(game.missileHistory.isEmpty())
    assertTrue(game.currentHistoryStamp == -1)
  }

  @Test
  void testSendMissile() {
    def board = new Board(5)
    def ship = new Ship()
    ship.coordinateIsHitByMissileMap.put(Coordinate.of(0, 0), false)
    board.ships.add(ship)

    def game = new Game(board)
    def isHit

    (game, isHit) = gameService.sendMissile(game, 0, 0)

    assertTrue(isHit)
    assertTrue(ship.coordinateIsHitByMissileMap.get(Coordinate.of(0, 0)))
    assertTrue(board.missileAttempts.contains(Coordinate.of(0, 0)))
    assertTrue(game.missileHistory.size() == 1)
    assertTrue(game.missileHistory.get(0) == Coordinate.of(0, 0))
  }

  @Test
  void testSendMissile_Miss() {
    def shipCoordinate = Coordinate.of(0, 0)
    def missileCoordinate = Coordinate.of(1, 0)

    def board = new Board(5)
    def ship = new Ship()

    ship.coordinateIsHitByMissileMap.put(shipCoordinate, false)
    board.ships.add(ship)

    def game = new Game(board)
    def isHit

    (game, isHit) = gameService.sendMissile(game, missileCoordinate.column, missileCoordinate.row)

    assertFalse(isHit)
    assertFalse(ship.coordinateIsHitByMissileMap.get(shipCoordinate))

    assertTrue(board.missileAttempts.contains(missileCoordinate))
    assertTrue(game.missileHistory.size() == 1)
    assertTrue(game.missileHistory.get(0) == missileCoordinate)
  }

  @Test
  void testUndoRedo() {
    def shipCoordinate01 = Coordinate.of(0, 0)
    def shipCoordinate02 = Coordinate.of(1, 0)

    def board = new Board(4)
    def ship = new Ship()
    ship.coordinateIsHitByMissileMap.put(shipCoordinate01, false)
    ship.coordinateIsHitByMissileMap.put(shipCoordinate02, false)

    board.ships.add(ship)

    def game = new Game(board)
    gameService.sendMissile(game, shipCoordinate01.column, shipCoordinate01.row)
    gameService.sendMissile(game, shipCoordinate02.column, shipCoordinate02.row)

    assertTrue(ship.coordinateIsHitByMissileMap.get(shipCoordinate01))
    assertTrue(ship.coordinateIsHitByMissileMap.get(shipCoordinate02))
    assertTrue(board.missileAttempts.contains(shipCoordinate01))
    assertTrue(board.missileAttempts.contains(shipCoordinate02))
    assertTrue(game.missileHistory.contains(shipCoordinate01))
    assertTrue(game.missileHistory.contains(shipCoordinate02))
    assertTrue(game.currentHistoryStamp == 1)

    gameService.undo(game)

    assertTrue(ship.coordinateIsHitByMissileMap.get(shipCoordinate01))
    assertFalse(ship.coordinateIsHitByMissileMap.get(shipCoordinate02))
    assertTrue(board.missileAttempts.contains(shipCoordinate01))
    assertFalse(board.missileAttempts.contains(shipCoordinate02))
    assertTrue(game.missileHistory.containsAll(shipCoordinate01, shipCoordinate02))
    assertTrue(game.currentHistoryStamp == 0)

    gameService.redo(game)
    assertTrue(ship.coordinateIsHitByMissileMap.get(shipCoordinate01))
    assertTrue(ship.coordinateIsHitByMissileMap.get(shipCoordinate02))
    assertTrue(board.missileAttempts.contains(shipCoordinate01))
    assertTrue(board.missileAttempts.contains(shipCoordinate02))
    assertTrue(game.missileHistory.contains(shipCoordinate01))
    assertTrue(game.missileHistory.contains(shipCoordinate02))
    assertTrue(game.currentHistoryStamp == 1)
  }
}
