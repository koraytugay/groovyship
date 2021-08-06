package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship
import spock.lang.Specification

class ShipServiceSpockTest
    extends Specification
{
  ShipService shipService = new ShipService()

  /**
   * Generate 1000 random ships.
   * Assert ships of all sizes are generated.
   * Assert all ships are within the bounds of the board.
   * Assert all locations of the board is covered by generated ships.
   */
  def "must add random ships"(int boardSize) {
    given:
      def generatedShips = [] as List<Ship>
      def populatedCoordinates = [] as Set<Coordinate>
      def generatedShipSizes = [] as Set<Integer>

    when:
      for (i in 0..<1000) {
        def ship = shipService.newRandomShip(boardSize)
        generatedShips << ship
        populatedCoordinates.addAll(ship.coordinateIsHitByMissileMap.keySet())
        generatedShipSizes.add(ship.coordinateIsHitByMissileMap.size())
      }

    then:
      populatedCoordinates.size() == boardSize * boardSize
      populatedCoordinates.every { it.column >= 0 && it.column < boardSize && it.row >= 0 && it.row < boardSize }
      generatedShips.every({ it.coordinateIsHitByMissileMap.size() >= 1 && it.coordinateIsHitByMissileMap.size() <= 4 })

    where:
      boardSize | _
      4         | _
      5         | _
      6         | _
      7         | _
      7         | _
  }

  def "test ship boundaries"() {
    /*
        0  1  2  3  4  5
      0 .  .  .  .  .  .
      1 .  .  .  .  .  .
      2 .  .  x  x  .  .
      3 .  .  .  .  .  .
      4 .  .  .  .  .  .
    */
    given:
      def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    expect:
      inBoundaries == shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(column, row)])

    where:
      column | row | inBoundaries
      0      | 0   | false
      1      | 0   | false
      2      | 0   | false
      3      | 0   | false
      4      | 0   | false
      5      | 0   | false
      0      | 1   | false
      1      | 1   | true
      2      | 1   | true
      3      | 1   | true
      4      | 1   | true
      5      | 1   | false
      0      | 2   | false
      1      | 2   | true
      2      | 2   | true
      3      | 2   | true
      4      | 2   | true
      5      | 2   | false
      0      | 3   | false
      1      | 3   | true
      2      | 3   | true
      3      | 3   | true
      4      | 3   | true
      5      | 3   | false
      0      | 4   | false
      1      | 4   | false
      2      | 4   | false
      3      | 4   | false
      4      | 4   | false
      5      | 4   | false
  }

  def "has part on coordinate"() {
    /*
      0  1  2  3  4  5
      0 .  .  .  .  .  .
      1 .  .  .  .  .  .
      2 .  .  x  x  .  .
      3 .  .  .  .  .  .
      4 .  .  .  .  .  .
    */

    given:
      def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    expect:
      hasPartOnCoordinate == shipService.hasPartOnCoordinate(ship, Coordinate.of(column, row))

    where:
      column | row | hasPartOnCoordinate
      0      | 0   | false
      1      | 0   | false
      2      | 0   | false
      3      | 0   | false
      4      | 0   | false
      5      | 0   | false
      0      | 1   | false
      1      | 1   | false
      2      | 1   | false
      3      | 1   | false
      4      | 1   | false
      5      | 1   | false
      0      | 2   | false
      1      | 2   | false
      2      | 2   | true
      3      | 2   | true
      4      | 2   | false
      5      | 2   | false
      0      | 3   | false
      1      | 3   | false
      2      | 3   | false
      3      | 3   | false
      4      | 3   | false
      5      | 3   | false
      0      | 4   | false
      1      | 4   | false
      2      | 4   | false
      3      | 4   | false
      4      | 4   | false
      5      | 4   | false
  }

  def "testAttemptMissileHit"() {
    given:
      def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    when:
      shipService.attemptMissileHit(ship, Coordinate.of(2, 2))

    then:
      ship.coordinateIsHitByMissileMap.get(Coordinate.of(2, 2))
      !ship.coordinateIsHitByMissileMap.get(Coordinate.of(3, 2))

    when:
      shipService.attemptMissileHit(ship, Coordinate.of(3, 2))

    then:
      ship.coordinateIsHitByMissileMap.get(Coordinate.of(2, 2))
      ship.coordinateIsHitByMissileMap.get(Coordinate.of(3, 2))
  }

  def "testIsSank"() {
    given:
      def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    when:
      shipService.attemptMissileHit(ship, Coordinate.of(3, 2))

    then:
      !shipService.isSank(ship)

    when:
      shipService.attemptMissileHit(ship, Coordinate.of(2, 2))

    then:
      shipService.isSank(ship)
  }

  def "testUnMissile"() {
    given:
      def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))
      shipService.attemptMissileHit(ship, Coordinate.of(2, 2))

    expect:
      ship.coordinateIsHitByMissileMap.get(Coordinate.of(2, 2))

    when:
      shipService.unMissile(ship, Coordinate.of(2, 2))

    then:
      !ship.coordinateIsHitByMissileMap.get(Coordinate.of(2, 2))
  }
}
