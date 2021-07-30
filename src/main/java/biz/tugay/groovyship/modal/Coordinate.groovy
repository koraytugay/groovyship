package biz.tugay.groovyship.modal

import groovy.transform.Memoized

/**
 * A Coordinate represents a certain point.
 *
 * <pre>
 * 0,0  5,0
 * .....
 * .
 * .
 * .
 * .
 * .
 * 0,5
 * </pre>
 */
class Coordinate
{
  final int column

  final int row

  private Coordinate(int column, int row) {
    this.column = column
    this.row = row
  }

  @Memoized
  static Coordinate of(int column, int row) {
    return new Coordinate(column, row)
  }
}
