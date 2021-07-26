package biz.tugay.groovyship.modal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*

class CoordinateTest
{
  @Test
  void testCoordinatesEquals() {
    def coordinate1 = Coordinate.of(0, 0)
    def coordinate2 = Coordinate.of(0, 0)

    assertTrue(coordinate1.is(coordinate2))
    assertTrue(coordinate1 == (coordinate2))
    assertTrue(coordinate1.hashCode() == coordinate2.hashCode())
  }
}
