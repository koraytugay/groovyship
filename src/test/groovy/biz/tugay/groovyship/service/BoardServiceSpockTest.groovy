package biz.tugay.groovyship.service

import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship
import spock.lang.Specification

class BoardServiceSpockTest
    extends Specification
{
  BoardService boardService = new BoardService()

  def "must add ship"() {
    given:
      def board = new Board(9)

    expect:
      boardService.addShip(board, new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0)))
      board.ships.size() == 1
      boardService.addShip(board, new Ship(Coordinate.of(2, 2), Coordinate.of(3, 2)))
      board.ships.size() == 2
  }

  def "must not add ship"() {
    given:
      def board = new Board(9)

    when:
      def shipAdded = boardService.addShip(board, new Ship(Coordinate.of(0, 0), Coordinate.of(1, 0)))

    then:
      shipAdded
      board.ships.size() == 1

    when:
      shipAdded = boardService.addShip(board, new Ship(Coordinate.of(0, 1), Coordinate.of(1, 1)))

    then:
      !shipAdded
      board.ships.size() == 1
  }

  def "must missile coordinate"() {
    given:
      def board = new Board(4)
      def ship = new Ship(Coordinate.of(0, 0))
      boardService.addShip(board, ship)

    when:
      def isHit = boardService.missileCoordinate(board, Coordinate.of(1, 0))

    then:
      !isHit
      board.missileAttempts.contains(Coordinate.of(1, 0))

    when:
      isHit = boardService.missileCoordinate(board, Coordinate.of(0, 0))

    then:
      isHit
      board.missileAttempts.contains(Coordinate.of(0, 0))
  }

  def "all ships must be sunk"(shipCoordinates) {
    given:
      def board = new Board(4)
      def ship = new Ship(shipCoordinates)
      boardService.addShip(board, ship)

    when:
      shipCoordinates.each { boardService.missileCoordinate(board, it) }

    then:
      boardService.allShipsSank(board)

    where:
      shipCoordinates                       | _
      [Coordinate.of(0, 0)] as Coordinate[] | _
      [Coordinate.of(0, 0),
       Coordinate.of(1, 0)] as Coordinate[] | _
  }

  def "must unMissileCoordinate"() {
    def coordinate_00 = Coordinate.of(0, 0)
    def coordinate_10 = Coordinate.of(1, 0)

    given:
      def board = new Board(4)
      def ship = new Ship(coordinate_00, coordinate_10)
      boardService.addShip(board, ship)

    when:
      boardService.missileCoordinate(board, coordinate_00)
      boardService.missileCoordinate(board, coordinate_10)

    then:
      board.missileAttempts.containsAll(coordinate_00, coordinate_10)
      boardService.allShipsSank(board)
      ship.coordinateIsHitByMissileMap.values().stream().allMatch({ it })

    when:
      boardService.unMissileCoordinate(board, coordinate_00)

    then:
      !board.missileAttempts.contains(coordinate_00)
      board.missileAttempts.contains(coordinate_10)
      !boardService.allShipsSank(board)
      ship.coordinateIsHitByMissileMap.size() == 2
      ship.coordinateIsHitByMissileMap.keySet().containsAll(coordinate_00, coordinate_10)
      !ship.coordinateIsHitByMissileMap.get(coordinate_00)
      ship.coordinateIsHitByMissileMap.get(coordinate_10)

    when:
      boardService.unMissileCoordinate(board, coordinate_10)

    then:
      !board.missileAttempts.contains(coordinate_00)
      !board.missileAttempts.contains(coordinate_10)
      !boardService.allShipsSank(board)
      ship.coordinateIsHitByMissileMap.size() == 2
      ship.coordinateIsHitByMissileMap.keySet().containsAll(coordinate_00, coordinate_10)
      !ship.coordinateIsHitByMissileMap.get(coordinate_00)
      !ship.coordinateIsHitByMissileMap.get(coordinate_10)
  }
}
