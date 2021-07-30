package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship

class BoardService
{

  ShipService shipService = new ShipService()

  /**
   * Attempts to add the {@code ship} to the board. As per the rules of the game, a ship cannot overlay with
   * any other existing ship on the board. Ships also cannot be adjacent to each other.
   *
   * @param board The board the ship will be added to, potentially having ships already added.
   * @param ship The new ship that is being attempted to be added.
   * @return Whether ship was added to the board or not.
   */
  boolean addShip(Board board, Ship ship) {
    def shipCoordinates = ship.coordinateIsHitByMissileMap.keySet()

    if (board.ships.any { shipService.isBoundariesOfShipInCoordinates it, shipCoordinates }) {
      return false
    }

    board.ships << ship
    return true
  }

  /**
   * Sends a missile to to the board.
   * Returns a new board and the information whether the attempt was successful or not.
   */
  def missileCoordinate(Board board, int column, int row) {
    def newBoard = copy(board)
    newBoard.missileAttempts << Coordinate.of(column, row)

    def anyHit = false
    newBoard.ships.each { { anyHit = anyHit || shipService.attemptMissileHit(it, Coordinate.of(column, row)) } }

    return [newBoard, anyHit]
  }

  /**
   * @param board The board to be checked if all ships on the board sank
   * @return Whether all ships on the board sank
   */
  boolean allShipsSank(Board board) {
    return board.ships.every { it.coordinateIsHitByMissileMap.values().every { it } }
  }

  Board copy(Board board) {
    def newBoard = new Board(board.boardSize)

    board.missileAttempts.each { newBoard.missileAttempts << it }
    board.ships.each {
      newBoard.ships << shipService.copy(it)
    }

    return newBoard
  }
}
