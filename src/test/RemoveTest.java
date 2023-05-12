package test;


import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Remove;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This is a JUnit class that tests Remove
 * 
 * 
 *
 */
public class RemoveTest {
  /**
   * Stores the instance of the file system being worked on
   */
  FileSystem fileSystem;
  /**
   * Stores the instance of Remove being worked on
   */
  Remove remove;
  /**
   * Stores the instance of the first directory in root
   */
  Directory dir1;
  /**
   * Stores the instance of the second directory in root
   */
  Directory dir2;
  /**
   * Stores the instance of the first directory in dir2
   */
  Directory dir3;
  /**
   * Stores the instance of the first file in dir1
   */
  File file1;

  @Before
  /**
   * Sets up the file system and remove
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    Directory root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    remove = new Remove(fileSystem);
    dir1 = new Directory("dir1", root);
    dir2 = new Directory("dir2", root);
    dir3 = new Directory("dir3", dir2);
    file1 = new File("file1", dir1);
  }

  @After
  /**
   * Resets the reference parameter of file system to null, allowing for new instance each test
   * 
   * @throws Exception
   */
  public void tearDown() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null);
    System.setOut(System.out);
  }

  @Test
  /**
   * Tests whether Remove removes the directory and all it's subchildren
   */
  public void testRemovesDirectoryAndSubChildren() {
    String[] userInput = {"rm", "dir1"};
    try {
      remove.runCommand(userInput);
      assertEquals(1, fileSystem.getRootDirectory().getChildDirectories().size());
      assertEquals(0, fileSystem.getRootDirectory().getChildFiles().size());
      assertEquals("dir2",
          fileSystem.getRootDirectory().getChildDirectories().get(0).getDirectoryName());
      assertEquals(null, fileSystem.pathExists("/dir1/file1"));
    } catch (InvalidPathException | InvalidArgumentException e) {
      fail("should not throw an exception");
    }
  }

  @Test
  /**
   * Tests whether Remove recognizes wrong number of arguments passed
   */
  public void testWrongNumberOfArguments() {
    String[] userInput = {"rm", "/", "dir1"};
    try {
      remove.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("cd: too many arguments", e.getMessage());
    }
    String[] secondUserInput = {"rm"};
    try {
      remove.runCommand(secondUserInput);
      fail("should throw an excpetion");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("cd: not enough arguments", e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether Remove removes the root directory
   */
  public void testRemovesRoot() {
    String[] userInput = {"rm", "/"};
    try {
      remove.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals(
          "rm: /: ancestor directory of current working directory or root directory cannot be "
              + "removed",
          e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether Remove deletes the current directory and/or it's ancestors
   */
  public void testRemovesCurrentWorkingDirectoryAncestors() {
    String[] userInput = {"rm", "/dir2"};
    fileSystem.setCurrentDirectory(dir3);
    try {
      remove.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals(
          "rm: /dir2: ancestor directory of current working directory or root directory cannot be"
              + " removed",
          e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether Remove deletes a directory that doesn't exist
   */
  public void testRemovesNonExistingDirectory() {
    String[] userInput = {"rm", "/dir2/dir5"};
    try {
      remove.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("rm: /dir2/dir5: No such file or directory", e.getMessage());

    }
  }

}
