package test;


/**
 * This is a JUnit class that tests Manual
 * 
 * 
 *
 */
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import exceptions.InvalidArgumentException;
import commands.Manual;
import filesystem.Directory;
import filesystem.FileSystem;

public class ManualTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Manual being tested
   */
  private Manual man;

  @Before
  /**
   * Sets up file system and Manual
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    man = new Manual(fileSystem);
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
    field.set(null, null); // setting the ref parameter to null
  }

  @Test
  /**
   * Tests Manual with no arguments
   */
  public void testEmptyParameter() {
    String[] commandInputArray = {"man"};
    try {
      man.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Not enough arguments.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests Manual with an invalid command
   */
  public void testNonExistentCommand() {
    String[] commandInputArray = {"man", "home"};
    try {
      man.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Invalid argument. No such command " + "found.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests Manual with multiple arguments
   */
  public void testMultipleCommands() {
    String[] commandInputArray = {"man", "ls", "cd", "man"};
    try {
      man.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Too many arguments.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests if Manual returns the correct matching documentation for that command
   */
  public void testCommand() {
    String[] commandInputArray = {"man", "popd"};
    String testCommandResult = "popd - Remove the top entry from the directory stack and change \n"
        + "       the current working directory to that directory.\n";
    try {
      String testCommandOutput = man.runCommand(commandInputArray);
      assertEquals(testCommandResult, testCommandOutput);
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
    }
  }

}
