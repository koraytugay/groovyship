package biz.tugay.groovyship.modal

/**
 * A Game instance encapsulates a board and the history of the game that allows
 * the user to undo and redo their actions.
 */
class Game
{
  Board board

  List<Coordinate> missileHistory

  int currentHistoryStamp

  Game(Board board) {
    this.board = board
    missileHistory = []
    currentHistoryStamp = -1
  }
}
