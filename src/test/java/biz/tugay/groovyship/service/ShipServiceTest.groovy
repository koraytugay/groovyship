package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*


class ShipServiceTest
{
  ShipService shipService = new ShipService()

  /**
   * Generate 1000 random ships.
   * Assert ships of all sizes are generated.
   * Assert all ships are within the bounds of the board.
   * Assert all locations of the board is covered by generated ships.
   */
  @Test
  void testNewRandomShip() {
    def boardSize = 8
    Set<Coordinate> populatedCoordinates = new HashSet<>()
    Set<Integer> generatedShipSizes = new HashSet<>()

    for (i in 0..<1000) {
      Ship ship = shipService.newRandomShip(boardSize)
      populatedCoordinates.addAll(ship.coordinateIsHitByMissileMap.keySet())
      generatedShipSizes.add(ship.coordinateIsHitByMissileMap.size())
      assertTrue(ship.coordinateIsHitByMissileMap.size() <= boardSize)
      ship.coordinateIsHitByMissileMap.keySet().
          each { assertTrue(it.row <= boardSize) && assertTrue(it.column <= boardSize) }
    }

    assertTrue(populatedCoordinates.size() == boardSize * boardSize)
  }

  @Test
  void testIsBoundariesOfShipInCoordinates() {
    /*

        0  1  2  3  4  5
      0 .  .  .  .  .  .
      1 .  .  .  .  .  .
      2 .  .  x  x  .  .
      3 .  .  .  .  .  .
      4 .  .  .  .  .  .

     */
    def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    // 0th Row
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(0, 0)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(1, 0)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(2, 0)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(3, 0)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(4, 0)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(5, 0)]))

    // 1st Row
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(0, 1)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(1, 1)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(2, 1)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(3, 1)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(4, 1)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(5, 1)]))

    // 2nd Row
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(0, 2)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(1, 2)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(2, 2)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(3, 2)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(4, 2)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(5, 2)]))

    // 3rd Row
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(0, 3)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(1, 3)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(2, 3)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(3, 3)]))
    assertTrue(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(4, 3)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(5, 3)]))

    // 4th Row
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(0, 4)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(1, 4)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(2, 4)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(3, 4)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(4, 4)]))
    assertFalse(shipService.isBoundariesOfShipInCoordinates(ship, [Coordinate.of(5, 4)]))
  }

  @Test
  void testHasPartOnCoordinate() {
    /*

    0  1  2  3  4  5
  0 .  .  .  .  .  .
  1 .  .  .  .  .  .
  2 .  .  x  x  .  .
  3 .  .  .  .  .  .
  4 .  .  .  .  .  .

 */
    def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))

    // 0th Row
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(0, 0)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(1, 0)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(2, 0)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(3, 0)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(4, 0)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(5, 0)))

    // 1st Row
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(0, 1)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(1, 1)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(2, 1)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(3, 1)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(4, 1)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(5, 1)))

    // 2nd Row
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(0, 2)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(1, 2)))
    assertTrue(shipService.hasPartOnCoordinate(ship, Coordinate.of(2, 2)))
    assertTrue(shipService.hasPartOnCoordinate(ship, Coordinate.of(3, 2)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(4, 2)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(5, 2)))

    // 3rd Row
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(0, 3)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(1, 3)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(2, 3)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(3, 3)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(4, 3)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(5, 3)))

    // 4th Row
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(0, 4)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(1, 4)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(2, 4)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(3, 4)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(4, 4)))
    assertFalse(shipService.hasPartOnCoordinate(ship, Coordinate.of(5, 4)))
  }

  @Test
  void testAttemptMissileHit() {
    def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))
    assertTrue(ship.coordinateIsHitByMissileMap.values().every { !it })
    assertTrue(shipService.attemptMissileHit(ship, Coordinate.of(2, 2)))
    assertTrue(shipService.attemptMissileHit(ship, Coordinate.of(3, 2)))
    assertTrue(ship.coordinateIsHitByMissileMap.values().every())
  }

  @Test
  void testIsSank() {
    def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))
    assertFalse(shipService.isSank(ship))
    assertTrue(shipService.attemptMissileHit(ship, Coordinate.of(2, 2)))
    assertFalse(shipService.isSank(ship))
    assertTrue(shipService.attemptMissileHit(ship, Coordinate.of(3, 2)))
    assertTrue(shipService.isSank(ship))
  }

  @Test
  void testIsSank_Miss() {
    def ship = new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2))
    assertFalse(shipService.isSank(ship))
    assertFalse(shipService.attemptMissileHit(ship, Coordinate.of(2, 3)))
    assertFalse(shipService.isSank(ship))
    assertFalse(shipService.attemptMissileHit(ship, Coordinate.of(3, 3)))
    assertFalse(shipService.isSank(ship))
  }
}
