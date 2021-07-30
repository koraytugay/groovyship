package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

class BoardServiceTest
{

  BoardService boardService = new BoardService()

  @Test
  void testAddShip() {
    def board = new Board(9)
    def ship = new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0))
    assertTrue(boardService.addShip(board, ship))

    ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))
    assertTrue(boardService.addShip(board, ship))
  }

  @Test
  void testAddShip_mustNotAdd() {
    def board = new Board(9)
    def ship = new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0))
    assertTrue(boardService.addShip(board, ship))

    ship = new Ship(Coordinate.of(0, 1), Coordinate.of(1, 1))
    assertFalse(boardService.addShip(board, ship))
  }

  @Test
  void testMissileCoordinate() {
    def board = new Board(4)
    def ship = new Ship(Coordinate.of(0, 0))
    boardService.addShip(board, ship)

    def (newBoard, isMissileHit) = boardService.missileCoordinate(board, 1, 0)
    assertFalse(isMissileHit)
    assertFalse(board.missileAttempts.contains(Coordinate.of(1, 0)))
    assertTrue(newBoard.missileAttempts.contains(Coordinate.of(1, 0)))

    def (newNewBoard, newIsMissileHit) = boardService.missileCoordinate(board, 0, 0)
    assertFalse(isMissileHit)
    assertTrue(newIsMissileHit)

    assertFalse(newBoard.missileAttempts.contains(Coordinate.of(0, 0)))
    assertTrue(newNewBoard.missileAttempts.contains(Coordinate.of(0, 0)))
  }

  @Test
  void testAllShipsSank() {
    def board = new Board(4)
    def ship = new Ship(Coordinate.of(0, 0))
    boardService.addShip(board, ship)

    def (newBoard, isMissileHit) = boardService.missileCoordinate(board, 0, 0)
    assertFalse(boardService.allShipsSank(board))

    assertTrue(boardService.allShipsSank(newBoard))
    assertTrue(isMissileHit)
  }

  @Test
  void testCopy() {
    def ship = new Ship()
    ship.coordinateIsHitByMissileMap.put(Coordinate.of(5, 5), false)

    def board = new Board(4)
    board.missileAttempts.add(Coordinate.of(4, 4))
    board.ships.add(ship)

    def newBoard = boardService.copy(board)
    assertFalse(board.ships.iterator().next().is(newBoard.ships.iterator().next()))
    assertFalse(board.missileAttempts.is(newBoard.missileAttempts))
    assertFalse(board.is(newBoard))
  }
}
