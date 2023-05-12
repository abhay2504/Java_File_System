package test;


/**
 * This is a JUnit class that tests Search
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
import exceptions.InvalidPathException;
import commands.Search;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class SearchTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Search being tested
   */
  private Search src;

  @Before
  /**
   * Sets up file system and Search
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    src = new Search(fileSystem);

    Directory Dir1 = new Directory("Dir1", root);
    Directory Dir2 = new Directory("Dir2", root);
    Directory testDir = new Directory("testDir", root);
    File File1 = new File("File1", root);
    Directory Dir3 = new Directory("Dir3", testDir);
    Directory Dir4 = new Directory("Dir4", testDir);
    File File2 = new File("File2", testDir);
    root.addChildDirectory(Dir1);
    root.addChildDirectory(Dir2);
    root.addChildDirectory(testDir);
    root.addChildFile(File1);
    testDir.addChildDirectory(Dir3);
    testDir.addChildDirectory(Dir4);
    testDir.addChildFile(File2);
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
   * Tests the Search command with standard arguments for a directory name
   */
  public void testDirectory() throws InvalidArgumentException, InvalidPathException {
    String[] commandInputArray = {"search", "/", "-type", "d", "-name", "\"Dir1\""};
    String testExpectedOutput = "Path 1: Dir1";
    String testCommandOutput = src.runCommand(commandInputArray);
    assertEquals(testExpectedOutput, testCommandOutput.trim());
  }

  @Test
  /**
   * Tests the Search command with standard arguments for a file name
   */
  public void testFile() {
    String[] commandInputArray = {"search", "/", "-type", "f", "-name", "\"File1\""};
    String testExpectedOutput = "Path 1: /File1\n";
    try {
      String testCommandOutput = src.runCommand(commandInputArray);
      assertEquals(testExpectedOutput, testCommandOutput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      fail("should not throw an error");
    }
  }

  @Test
  /**
   * Tests the Search command with no arguments
   */
  public void testInvalidArguments1() {
    String[] commandInputArray = {"search"};
    try {
      src.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("Invalid argument(s). Check format.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the Search command with an invalid type
   */
  public void testInvalidArguments2() {
    String[] commandInputArray = {"search", "/testDir/", "-type", "g", "-name", "\"File2\""};
    try {
      src.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("Invalid argument(s). Check format.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the Search command with invalid argument positions
   */
  public void testInvalidArguments3() {
    String[] commandInputArray = {"search", "/testDir/", "-name", "f", "-type", "\"File2\""};
    try {
      src.runCommand(commandInputArray);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("Invalid argument(s). Check format.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the Search command with an invalid path
   */
  public void testInvalidArguments4() {
    String[] commandInputArray = {"search", "/Dir5", "-type", "f", "-name", "\"File2\""};
    try {
      src.runCommand(commandInputArray);
      fail("should throw an error");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("\nNo such directory.", e.getMessage());
    }
  }

  @Test
  /**
   * Tests the Search command with multiple paths
   */
  public void testMultiplePaths() {
    String[] commandInputArray =
        {"search", "/testDir", "/", "/Dir1", "-type", "d", "-name", "\"Dir2\""};
    String testExpectedOutput = "Path 2: Dir2\n";
    try {
      String testCommandOutput = src.runCommand(commandInputArray);
      assertEquals(testExpectedOutput, testCommandOutput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      fail("should not throw an error");
    }
  }
}
