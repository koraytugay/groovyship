package biz.tugay.groovyship.modal

import spock.lang.Specification

class CoordinateTest
    extends Specification
{
  def "testCoordinatesEqual"() {
    given:
      def coordinate1 = Coordinate.of(0, 0)
      def coordinate2 = Coordinate.of(0, 0)

    expect:
      coordinate1.is(coordinate2)
      coordinate1 == (coordinate2)
      coordinate1.hashCode() == coordinate2.hashCode()
  }
}
