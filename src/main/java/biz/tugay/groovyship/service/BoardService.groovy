package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship

class BoardService
{

  ShipService shipService = new ShipService()

  boolean addShip(Board board, Ship ship) {
    def shipCoordinates = ship.coordinateIsHitByMissileMap.keySet()

    if (board.ships.any { shipService.isBoundariesOfShipInCoordinates it, shipCoordinates }) {
      return false
    }

    board.ships << ship
    return true
  }

  boolean missileCoordinate(Board board, Coordinate coordinate) {
    board.missileAttempts << coordinate

    def missileHit = false

    board.ships.forEach({
      missileHit = missileHit || shipService.attemptMissileHit(it, coordinate)
    })

    return missileHit
  }

  Board unMissileCoordinate(Board board, Coordinate coordinate) {
    board.missileAttempts.remove(coordinate)
    board.ships.each { shipService.unMissile(it, coordinate) }
    return board
  }

  boolean allShipsSank(Board board) {
    return board.ships.every { ship -> shipService.isSank(ship) }
  }
}
