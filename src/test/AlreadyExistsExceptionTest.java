package test;


import static org.junit.Assert.*;
import org.junit.Test;
import exceptions.AlreadyExistsException;

/**
 * This is a JUnit class that tests the AlreadyExistsException
 * 
 * 
 *
 */
public class AlreadyExistsExceptionTest {

  @Test
  /**
   * This method tests whether the appropriate exception is thrown
   */
  public void testExceptionThrown() {
    try {
      throw new AlreadyExistsException("this file/directory already exists");
    } catch (AlreadyExistsException e) {
      assertEquals("this file/directory already exists", e.getMessage());
    }
  }

}
