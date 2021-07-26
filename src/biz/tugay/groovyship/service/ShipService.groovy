package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship

import static java.lang.Math.min
import static java.util.concurrent.ThreadLocalRandom.current

import static biz.tugay.groovyship.commons.GroovyShipConstants.*

class ShipService
{
  /**
   * Creates a {@link biz.tugay.groovyship.modal.Ship} object depending on the board it will be placed in.
   *
   * The passed in {@code boardSize} ensures the ship randomly generated
   * will fit in a board with a size of {@code boardSize}.
   *
   * @param boardSize The size of the board this ship should successfully can be placed in.
   * @return The randomly generated ship.
   */
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

  /**
   * As per the rule of the game, boundaries of a ship is the location of the ship itself
   * and the immediate surroundings of it. A ship with a size of 1 on coordinate 1,1 would
   * have boundaries: 0,0 - 0,1 - 0,2 - 1,0 - 1,1 - 1,2 - 2,0 - 2,1 - 2,2
   * @param ship The ship the boundaries will be checked.
   * @param coordinates The coordinates to check if any of them is in the boundaries of the ship.
   * @return Whether any of the coordinates provded is in the boundaries of the ship.
   */
  boolean isBoundariesOfShipInCoordinates(Ship ship, Collection<Coordinate> coordinates) {
    def occupiesCoordinate = false

    ship.coordinateIsHitByMissileMap.keySet().each { shipCoordinate ->
      (-1..1).each { colIndex ->
        (-1..1).each { rowIndex ->
          if (coordinates.contains Coordinate.of(shipCoordinate.column + colIndex, shipCoordinate.row + rowIndex)) {
            occupiesCoordinate = true
          }
        }
      }
    }

    return occupiesCoordinate
  }

  /**
   * @param ship The ship to check.
   * @param coordinate The coordinate to check.
   * @return Whether a ship immediately occupies the coordinate.
   */
  boolean hasPartOnCoordinate(Ship ship, Coordinate coordinate) {
    return ship.coordinateIsHitByMissileMap.keySet().contains(coordinate)
  }

  /**
   * Updates the state of the ship if the missileCoordinate hits this ship.
   * If this ship does not have any parts in the incoming missileCoordinate, this method
   * returns false without modifying the state of the {@code ship}.
   *
   * @param missileCoordinate The coordinates of the incoming missile.
   * @return Whether the missile hit the ship or not.
   */
  boolean attemptMissileHit(Ship ship, Coordinate missileCoordinate) {
    def coordinate = ship.coordinateIsHitByMissileMap.keySet().find { it == missileCoordinate }
    if (coordinate) {
      ship.coordinateIsHitByMissileMap.replace coordinate, true
    }
    return coordinate
  }

  /**
   * @return Whether this ship is sank or not.
   */
  boolean isSank(Ship ship) {
    return ship.coordinateIsHitByMissileMap.values().every { it }
  }
}
