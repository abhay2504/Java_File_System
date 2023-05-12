package test;


/**
 * This is a JUnit class that tests Curl
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
import commands.Curl;
import filesystem.Directory;
import filesystem.FileSystem;
import filesystem.File;

public class CurlTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Curl being tested
   */
  private Curl cu;

  @Before
  /**
   * Sets up file system and Curl
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    cu = new Curl(fileSystem);
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
   * Tests the Curl command with a valid URL
   */
  public void testCommand() {
    String[] commandInputArray = {"curl", "http://www.cs.cmu.edu/~spok/grimmtmp" + "/073.txt"};
    String testFName = "073txt";
    String testExpectedOutput = "There was once a king who had an illness, "
        + "and no one believed that he\nwould come out of it with his life. ";
    Directory currDirectory = fileSystem.getCurrentDirectory();
    String testFileSample = "";


    try {
      cu.runCommand(commandInputArray);
      if (fileSystem.getCurrentDirectory().getChildFiles().get(0) != null) {
        File testFile = currDirectory.getChildFiles().get(0);
        testFileSample = testFile.getFileData().substring(0, testExpectedOutput.length());
      }
      assertEquals(testFName, currDirectory.getChildFiles().get(0).getFileName());
      assertEquals(testExpectedOutput, testFileSample);
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
    }
  }

  @Test
  /**
   * Tests the List command with too many arguments
   */
  public void testTooManyArguments() {
    String[] commandInputArray = {"curl", "http", "wiki"};
    try {
      cu.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Too many arguments.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the List command with an invalid URL
   */
  public void testNonExistentURL() {
    String[] commandInputArray = {"curl", "http://www.haha.this.does.not.exist"};
    try {
      cu.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Cannot read from requested URL.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the List command with a File that already exists
   */
  public void testAlreadyExistingFile() {
    String[] commandInputArray = {"curl", "http://www.cs.cmu.edu/~spok/grimmtmp" + "/073.txt"};
    try {
      cu.runCommand(commandInputArray);
      cu.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("File already exists.", e.getMessage());
    }
  }
}
