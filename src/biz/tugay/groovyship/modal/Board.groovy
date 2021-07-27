package biz.tugay.groovyship.modal

import biz.tugay.groovyship.commons.GroovyShipConstants

class Board
{
  @SuppressWarnings('GrFinalVariableAccess')
  final int boardSize

  Set<Ship> ships = [] as Set<Ship>

  Set<Coordinate> missileAttempts = [] as Set<Coordinate>

  Board(int boardSize) {
    if (boardSize < GroovyShipConstants.MINIMUM_BOARD_SIZE) {
      throw new IllegalArgumentException("Board size must at least be: $GroovyShipConstants.MINIMUM_BOARD_SIZE")
    }
    if (boardSize > GroovyShipConstants.MAXIMUM_BOARD_SIZE) {
      throw new IllegalArgumentException("Board size cannot be bigger than: $GroovyShipConstants.MAXIMUM_BOARD_SIZE")
    }
    this.boardSize = boardSize
  }
}
