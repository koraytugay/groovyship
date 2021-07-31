package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship

import static biz.tugay.groovyship.commons.GroovyShipConstants.MAXIMUM_SHIP_LENGTH
import static biz.tugay.groovyship.commons.GroovyShipConstants.MINIMUM_SHIP_LENGTH
import static java.lang.Math.min
import static java.util.concurrent.ThreadLocalRandom.current

class ShipService
{
  Ship newRandomShip(int boardSize) {
    def shipSize = current().nextInt(MINIMUM_SHIP_LENGTH, 1 + min(boardSize, MAXIMUM_SHIP_LENGTH))
    def isHorizontal = current().nextBoolean()

    def shipCoordinateColumn
    def shipCoordinateRow

    // Ensure the ship does not exceed the borders of the board
    if (isHorizontal) {
      shipCoordinateColumn = current().nextInt(boardSize - shipSize + 1)
      shipCoordinateRow = current().nextInt(boardSize)
    }
    else {
      shipCoordinateColumn = current().nextInt(boardSize)
      shipCoordinateRow = current().nextInt(boardSize - shipSize + 1)
    }

    // Generate the coordinates of the ship
    def coordinates = new Coordinate[shipSize]
    (0..<shipSize).each {
      coordinates[it] = isHorizontal ?
          Coordinate.of(shipCoordinateColumn + it, shipCoordinateRow) :
          Coordinate.of(shipCoordinateColumn, shipCoordinateRow + it)
    }

    return new Ship(coordinates)
  }

  boolean isBoundariesOfShipInCoordinates(Ship ship, Collection<Coordinate> coordinates) {
    def occupiesCoordinate = false

    ship.coordinateIsHitByMissileMap.keySet().each { shipCoordinate ->
      (-1..1).each { colIndex ->
        (-1..1).each { rowIndex ->
          if (coordinates.contains(Coordinate.of(shipCoordinate.column + colIndex, shipCoordinate.row + rowIndex))) {
            occupiesCoordinate = true
          }
        }
      }
    }

    return occupiesCoordinate
  }

  boolean hasPartOnCoordinate(Ship ship, Coordinate coordinate) {
    return coordinate in ship.coordinateIsHitByMissileMap.keySet()
  }

  boolean attemptMissileHit(Ship ship, Coordinate missileCoordinate) {
    def coordinate = ship.coordinateIsHitByMissileMap.keySet().find { it == missileCoordinate }
    if (coordinate) {
      ship.coordinateIsHitByMissileMap.replace coordinate, true
    }
    return coordinate
  }

  void unMissile(Ship ship, Coordinate unMissileCoordinate) {
    def coordinate = ship.coordinateIsHitByMissileMap.keySet().find { it == unMissileCoordinate }
    if (coordinate) {
      ship.coordinateIsHitByMissileMap.replace coordinate, false
    }
  }

  /**
   * @return Whether this ship is sank or not.
   */
  boolean isSank(Ship ship) {
    return ship.coordinateIsHitByMissileMap.values().every { it }
  }
}
