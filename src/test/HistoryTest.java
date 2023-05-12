package test;


import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.History;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.FileSystem;

/**
 * This is a JUnit class that tests History
 * 
 * 
 *
 */
public class HistoryTest {
  /**
   * Stores the instance of the file system being worked on
   */
  FileSystem fileSystem;
  /**
   * Stores the instance of History being tested
   */
  History history;

  @Before
  /**
   * Sets up file system and history
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    fileSystem = FileSystem.createFileSystemInstance(new Directory("root", null));
    history = new History(fileSystem);
  }

  @After
  /**
   * Resets the reference parameter of file system to null, allowing for new instance each test
   * 
   * @throws Exception
   */
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null);
  }


  @Test
  /**
   * Tests whether History is able to return a string with all the commands in the history list
   */
  public void testPrintAllHistory() {
    fileSystem.populateList("command");
    fileSystem.populateList("cd /Dir1");
    fileSystem.populateList("mkdir Dir2 Dir3");
    fileSystem.populateList("history");
    String[] userInput = {"history"};
    try {
      assertEquals("1.command\n" + "2.cd /Dir1\n" + "3.mkdir Dir2 Dir3\n" + "4.history\n",
          history.runCommand(userInput));
    } catch (InvalidArgumentException e) {
      fail("shouldn't throw an exception");
    }
  }

  @Test
  /**
   * Tests whether History is able to recognize when to return an empty string
   */
  public void testNoHistory() {
    fileSystem.populateList("mkdir Dir1 Dir2");
    fileSystem.populateList("loadJShell");
    String[] userInput = {"history", "0"};
    try {
      assertEquals("", history.runCommand(userInput));
    } catch (InvalidArgumentException e) {
      fail("shouldn't throw an exception");
    }
  }

  @Test
  /**
   * Tests whether History is able to return the n most recent commands when n < total number of
   * commands in history list
   */
  public void testPrintSmallNRecentHistory() {
    fileSystem.populateList("command");
    fileSystem.populateList("cd /Dir1");
    fileSystem.populateList("mkdir Dir2 Dir3");
    fileSystem.populateList("echo \"noodles\"");
    fileSystem.populateList("history");
    String[] userInput = {"history", "2"};
    try {
      assertEquals("4.echo \"noodles\"\n" + "5.history\n", history.runCommand(userInput));
    } catch (InvalidArgumentException e) {
      fail("shouldn't throw an exception");
    }
  }

  @Test
  /**
   * Tests whether History is able to return all the commands in history list when N > total number
   * of commands in the history list
   */
  public void testPrintLargeNRecentHistory() {
    fileSystem.populateList("command");
    fileSystem.populateList("cd /Dir1");
    fileSystem.populateList("mkdir Dir2 Dir3");
    fileSystem.populateList("echo \"noodles\"");
    fileSystem.populateList("history");
    String[] userInput = {"history", "500"};
    try {
      assertEquals("1.command\n" + "2.cd /Dir1\n" + "3.mkdir Dir2 Dir3\n" + "4.echo \"noodles\"\n"
          + "5.history\n", history.runCommand(userInput));
    } catch (InvalidArgumentException e) {
      fail("shouldn't throw an exception");
    }
  }

  @Test
  /**
   * Tests whether History recognizes when arguments are non-numeric
   */
  public void testNonNumericArgument() {
    String[] userInput = {"history", "history"};
    try {
      history.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("history: " + userInput[1] + ": numeric argument " + "required\n",
          e.getMessage());
    }

  }

  @Test
  /**
   * Tests whether History recognizes when there are too many arguments
   */
  public void testTooManyArguments() {
    String[] userInput = {"history", "5", "more"};
    try {
      history.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("history: too many arguments\n", e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether History recognizes argument is non-negative
   */
  public void testNegativeArgument() {
    String[] userInput = {"history", "-10"};
    try {
      history.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("history: " + userInput[1] + ": argument must be non-negative\n",
          e.getMessage());
    }
  }
}
