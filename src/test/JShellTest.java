package test;


import static org.junit.Assert.*;

import exceptions.InvalidArgumentException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import driver.JShell;

/**
 * This is a JUnit class that tests JShell
 * 
 * 
 *
 */
public class JShellTest {
  /**
   * Stores the instance of ByteArrayOutputStream being used
   */
  ByteArrayOutputStream out;
  /**
   * Stores the instance of ByteArrayInputStream being used
   */
  ByteArrayInputStream in;
  /**
   * Stores the instance of JShell being tested
   */
  JShell shell;

  @Before
  /**
   * Sets up new input stream and output stream
   */
  public void setUp() {
    in = new ByteArrayInputStream("histor".getBytes());
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
    System.setIn(in);
  }

  @After
  /**
   * Resets input and output stream back to StandardOutput and StandardInput
   */
  public void tearDown() {
    System.setIn(System.in);
    System.setOut(System.out);
  }

  @Test
  /**
   * Tests whether the user is being prompted for input
   */
  public void testUserIsBeingPrompted() {
    String[] args = {};
    try {
      JShell.main(args);
    } catch (NoSuchElementException | IOException | InvalidArgumentException e) {
      assertEquals("'histor' is not a valid command", out.toString().trim());
    }
  }

}
