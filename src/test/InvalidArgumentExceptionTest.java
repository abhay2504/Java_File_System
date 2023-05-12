package test;


import static org.junit.Assert.*;
import org.junit.Test;
import exceptions.InvalidArgumentException;

/**
 * This is a JUnit class that tests the InvalidArgumentException
 * 
 * 
 *
 */
public class InvalidArgumentExceptionTest {

  @Test
  /**
   * This method tests whether the appropriate exception is thrown
   */
  public void testExceptionThrown() {
    try {
      throw new InvalidArgumentException("this is an invalid argument");
    } catch (InvalidArgumentException e) {
      assertEquals("this is an invalid argument", e.getMessage());
    }
  }

}
