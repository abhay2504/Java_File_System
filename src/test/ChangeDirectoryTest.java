package test;


import commands.ChangeDirectory;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class that tests ChangeDirectory
 * 
 * 
 */

public class ChangeDirectoryTest {
  private FileSystem fileSystem;
  private Directory root;
  private ChangeDirectory cd;

  @Before
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    cd = new ChangeDirectory(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  @Test
  /**
   * Tests whether ChangeDirectory can throw appropriate exception when there are too many arguments
   */
  public void testTooManyArgument() {
    String userInput[] = {"cd", "dir1", "dir2"};
    try {
      cd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("cd: too many arguments", e.getMessage());
      assertEquals(fileSystem.getCurrentDirectory(), root);
    }
  }

  @Test
  /**
   * Tests whether ChangeDirectory can throw appropriate exception when there are not enough
   * arguments
   */
  public void testNotEnoughArgument() {
    String userInput[] = {"cd"};
    try {
      cd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("cd: not enough arguments", e.getMessage());
      assertEquals(fileSystem.getCurrentDirectory(), root);
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether ChangeDirectory can throw appropriate exeption when the path doesn't exist
   */
  public void testMoveToInvalidPath() {
    String userInput[] = {"cd", "/dir2"};
    Directory dir1;
    Directory dir2;
    try {
      dir1 = new Directory("dir1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("cd: no such file or directory: /dir2", e.getMessage());
      assertEquals(fileSystem.getCurrentDirectory(), root);
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether ChangeDirectory can throw appropriate exeption when the path is pointing to a
   * file
   */
  public void testMoveToFile() {
    String userInput[] = {"cd", "/file1"};
    Directory dir1;
    File file1;
    try {
      dir1 = new Directory("dir1", root);
      file1 = new File("file1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("cd: not a directory: /file1", e.getMessage());
      assertEquals(fileSystem.getCurrentDirectory(), root);
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether ChangeDirectory can move one depth of directory
   */
  public void testMoveOneDepth() {
    String userInput[] = {"cd", "/dir1"};
    Directory dir1;
    Directory dir2;
    try {
      dir1 = new Directory("dir1", root);
      dir2 = new Directory("dir2", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), dir1);
    } catch (InvalidArgumentException | InvalidPathException e) {
      fail("should not throw any exception");
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether ChangeDirectory can move one depth of directory with relative path
   */
  public void testMoveOneDepthRelative() {
    String userInput[] = {"cd", "dir2"};
    Directory dir1;
    Directory dir2;
    try {
      dir1 = new Directory("dir1", root);
      dir2 = new Directory("dir2", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), dir2);
    } catch (InvalidArgumentException | InvalidPathException e) {
      fail("should not throw any exception");
    }
  }

  @Test
  /**
   * Tests whether ChangeDirectory can move multiple depth of directory
   */
  public void testMoveMultiDepth() {
    String userInput[] = {"cd", "/dir1/dir2/dir3"};
    Directory dir1;
    Directory dir2;
    Directory dir3;
    try {
      dir1 = new Directory("dir1", root);
      dir2 = new Directory("dir2", dir1);
      dir3 = new Directory("dir3", dir2);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), dir3);
    } catch (InvalidArgumentException | InvalidPathException e) {
      fail("should not throw any exception");
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether ChangeDirectory can move with . or .. symbol
   */
  public void testMoveWithDots() {
    String userInput[] = {"cd", "/dir1"};
    Directory dir1;
    Directory dir2;
    Directory dir3;
    try {
      dir1 = new Directory("dir1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    try {
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), dir1);
      userInput = new String[] {"cd", "."};
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), dir1);
      userInput = new String[] {"cd", ".."};
      cd.runCommand(userInput);
      assertEquals(fileSystem.getCurrentDirectory(), root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      fail("should not throw any exception");
    }
  }
}
