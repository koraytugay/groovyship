package biz.tugay.groovyship.cli


import biz.tugay.groovyship.modal.Board
import biz.tugay.groovyship.modal.Coordinate
import biz.tugay.groovyship.modal.Ship
import biz.tugay.groovyship.service.ShipService

class BoardCommandLinePrinter
{
  ShipService shipService = new ShipService()

  void print(Board board) {
    def noAttempt = ' '
    def missedAttempt = '-'
    def successfulAttempt = '□'
    def sankShip = '◼'

    print '   '
    for (column in 0..<board.boardSize) {
      print "$column  "
    }
    println ''
    for (row in 0..<board.boardSize) {
      print "$row  "
      for (column in 0..<board.boardSize) {
        Coordinate coordinate = Coordinate.of(column, row)
        Ship shipOnCoordinate = board.ships.find { shipService.hasPartOnCoordinate(it, coordinate) }
        if (!board.missileAttempts.contains(coordinate)) {
          print "$noAttempt  "
        }
        else if (!shipOnCoordinate) {
          print "$missedAttempt  "
        }
        else if (shipService.isSank(shipOnCoordinate)) {
          print "$sankShip  "
        }
        else {
          print "$successfulAttempt  "
        }
      }
      println ''
    }
  }
}
