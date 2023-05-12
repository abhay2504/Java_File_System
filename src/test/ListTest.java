package test;


/**
 * This is a JUnit class that tests List
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
import commands.List;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class ListTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of List being tested
   */
  private List ls;

  @SuppressWarnings("unused")
  @Before
  /**
   * Sets up file system and List
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    ls = new List(fileSystem);

    Directory Dir1 = new Directory("Dir1", root);
    Directory Dir2 = new Directory("Dir2", root);
    Directory testDir = new Directory("testDir", root);
    File File1 = new File("File1", root);
    Directory Dir3 = new Directory("Dir3", testDir);
    Directory Dir4 = new Directory("Dir4", testDir);
    File File2 = new File("File2", testDir);
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
   * Tests the List command without arguments
   */
  public void testCommand() throws InvalidArgumentException, InvalidPathException {
    String[] commandInputArray = {"ls"};
    String testExpectedOutput = "/:\nDir1\nDir2\ntestDir\nFile1\n";
    String testCommandOutput = ls.runCommand(commandInputArray);
    assertEquals(testCommandOutput, testExpectedOutput);
  }

  @Test
  /**
   * Tests the List command with a directory as argument
   */
  public void testArgumentDir() {
    String[] commandInputArray = {"ls", "testDir"};
    String testExpectedOutput = "testDir:\nDir3\nDir4\nFile2\n";
    try {
      String testCommandOutput = ls.runCommand(commandInputArray);
      assertEquals(testCommandOutput, testExpectedOutput);
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  @Test
  /**
   * Tests the List command with a file as argument
   */
  public void testArgumentFile() {
    String[] commandInputArray = {"ls", "File1"};
    String testExpectedOutput = "File1\n";
    try {
      String testCommandOutput = ls.runCommand(commandInputArray);
      assertEquals(testCommandOutput, testExpectedOutput);
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  @Test
  /**
   * Tests the List command with an invalid path
   */
  public void testInvalidPath() {
    String[] commandInputArray = {"ls", "File3"};
    String testExpectedOutput = "ls: " + "File3" + ": no such file " + "or directory";
    try {
      String testCommandOutput = ls.runCommand(commandInputArray);
      assertEquals(testCommandOutput, testExpectedOutput);
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  @Test
  /**
   * Tests the List command's recursive function
   */
  public void testRecursive() {
    String[] commandInputArray = {"ls", "-R"};
    String testExpectedOutput = "/:\nDir1\nDir2\ntestDir\nFile1\n/Dir1:\n/Dir2"
        + ":\n/testDir:\nDir3\nDir4\nFile2\n/testDir/Dir3:\n/testDir/Dir4:\n";
    try {
      String testCommandOutput = ls.runCommand(commandInputArray);
      assertEquals(testCommandOutput, testExpectedOutput);
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  @Test
  /**
   * Tests the List command's recursive function with an argument
   */
  public void testRecursiveArguments() {
    String[] commandInputArray = {"ls", "-R", "testDir"};
    String testExpectedOutput = "testDir:\nDir3\nDir4\nFile2\ntestDir/Dir3:" + "\ntestDir/Dir4:\n";
    try {
      String testCommandOutput = ls.runCommand(commandInputArray);
      assertEquals(testCommandOutput, testExpectedOutput);
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }
}
