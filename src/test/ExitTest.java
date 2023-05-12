package test;


/**
 * This is a JUnit class that tests Exit
 * 
 *
 *
 */
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.After;

import exceptions.InvalidArgumentException;
import commands.Exit;
import filesystem.Directory;
import filesystem.FileSystem;

public class ExitTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Manual being tested
   */
  private Exit ex;

  @Before
  /**
   * Sets up file system and Exit
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    ex = new Exit(fileSystem);
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
   * Tests Exit with arguments
   */
  public void testArguments() {
    String[] commandInputArray = {"exit", "ls"};
    try {
      ex.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("There are too many " + "arguments\n", e.getMessage());
    }
  }



}
