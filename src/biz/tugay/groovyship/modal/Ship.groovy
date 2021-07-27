package biz.tugay.groovyship.modal

/**
 * A Ship object is represented as a Map of Coordinate:Boolean where the Boolean value represents whether
 * the ship coordinate has been hit by a missile or not.
 * An example might be: [
 *   [0,0]:false,
 *   [1,0]:false,
 *   [2,0]:true
 * ].
 * That would be a ship floating horizontally on the first row of a board, where its last piece has been damaged.
 */
class Ship
{
  Map<Coordinate, Boolean> coordinateIsHitByMissileMap = [:]

  Ship(Coordinate... coordinates) {
    coordinates.each { coordinateIsHitByMissileMap.put it, false }
  }
}
