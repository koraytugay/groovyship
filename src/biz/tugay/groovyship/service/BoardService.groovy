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

    board.ships.add(ship)
    return true
  }

  /**
   * Sends a missile to to the board. Returns whether it hit a ship or not.
   *
   * @param board The board the missile be sent to
   * @param column The column of the missile
   * @param row The row of the missle
   * @return Whether a ship was hit or not
   */
  boolean missileCoordinate(Board board, int column, int row) {
    def missileCoordinate = Coordinate.of column, row
    board.missileAttempts << missileCoordinate

    def anyHit = false
    board.ships.each { { anyHit = anyHit || shipService.attemptMissileHit(it, missileCoordinate) } }

    return anyHit
  }

  /**
   * @param board The board to be checked if all ships on the board sank
   * @return Whether all ships on the board sank
   */
  boolean allShipsSank(Board board) {
    return board.ships.every { it.coordinateIsHitByMissileMap.values().every { it } }
  }
}
