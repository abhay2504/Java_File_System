package test;


import static org.junit.Assert.*;
import org.junit.Test;
import exceptions.InvalidPathException;

/**
 * This is a JUnit class that tests InvalidPathException
 * 
 * 
 *
 */
public class InvalidPathExceptionTest {

  @Test
  /**
   * This method tests whether InvalidPathException throws the appropriate custom exception
   */
  public void testExceptionThrown() {
    try {
      throw new InvalidPathException("this is an invalid path");
    } catch (InvalidPathException e) {
      assertEquals("this is an invalid path", e.getMessage());
    }
  }

}
