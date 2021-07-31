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

    def isMissileHit = boardService.missileCoordinate(board, Coordinate.of(1, 0))
    assertFalse(isMissileHit)
    assertTrue(board.missileAttempts.contains(Coordinate.of(1, 0)))

    isMissileHit = boardService.missileCoordinate(board, Coordinate.of(0, 0))
    assertTrue(isMissileHit)
    assertTrue(board.missileAttempts.contains(Coordinate.of(0, 0)))
  }

  @Test
  void testAllShipsSank() {
    def board = new Board(4)
    def ship = new Ship(Coordinate.of(0, 0))
    boardService.addShip(board, ship)

    def isMissileHit = boardService.missileCoordinate(board, Coordinate.of(0, 0))
    assertTrue(isMissileHit)
    assertTrue(boardService.allShipsSank(board))
  }
}
