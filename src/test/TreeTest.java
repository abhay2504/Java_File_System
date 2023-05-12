package test;


import commands.Tree;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class that tests PrintWorkingDirectory
 * 
 * 
 */
public class TreeTest {
  private FileSystem fileSystem;
  private Directory root;
  private Tree tree;

  @Before
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    tree = new Tree(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  @Test
  /**
   * Tests whether Tree can throw appropriate exception with too many arguments
   */
  public void testTooManyArgument() {
    String userInput[] = {"tree", "house"};
    try {
      tree.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("tree: too many arguments", e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether Tree can print right tree structure for filesytem that contains only root
   * directory
   */
  public void testTreeWithJustRoot() {
    String userInput[] = {"tree"};
    try {
      String output = tree.runCommand(userInput);
      assertEquals("/", output);
    } catch (InvalidArgumentException e) {
      e.getStackTrace();
      fail("Should not throw any exception");
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether Tree can print correct tree structure for fileSystem with root directory and
   * childDirectories directly under rootDirectory
   */
  public void testTreeWithSingleDepth() {
    String userInput[] = {"tree"};
    try {
      Directory dir1 = new Directory("dir1", root);
      Directory dir2 = new Directory("dir2", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
    }
    try {
      String output = tree.runCommand(userInput);
      assertEquals("/" + "\n\tdir1" + "\n\tdir2", output);
    } catch (InvalidArgumentException e) {
      e.getStackTrace();
      fail("Should not throw any exception");
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether Tree can print correct tree structure for fileSystem with root directory and
   * childDirectories that are nested
   */
  public void testTreeWithNestedStructure() {
    String userInput[] = {"tree"};
    try {
      Directory dir1 = new Directory("dir1", root);
      Directory dir2 = new Directory("dir2", root);
      Directory dir3 = new Directory("dir3", dir1);
      Directory dir4 = new Directory("dir4", dir1);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
    }
    try {
      String output = tree.runCommand(userInput);
      assertEquals("/" + "\n\tdir1" + "\n\t\tdir3" + "\n\t\tdir4" + "\n" + "\tdir2", output);
    } catch (InvalidArgumentException e) {
      e.getStackTrace();
      fail("Should not throw any exception");
    }
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether Tree can print correct tree structure for fileSystem with root directory and
   * childDirectories and files that are nested
   */
  public void testTreeWithNestedStructureWithFiles() {
    String userInput[] = {"tree"};
    try {
      Directory dir1 = new Directory("dir1", root);
      Directory dir2 = new Directory("dir2", root);
      File file1 = new File("file1", root);
      File file2 = new File("file2", dir1);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
    }
    try {
      String output = tree.runCommand(userInput);
      assertEquals("/" + "\n\tdir1" + "\n\t\tfile2" + "\n\tdir2" + "\n\tfile1", output);
    } catch (InvalidArgumentException e) {
      e.getStackTrace();
      fail("Should not throw any exception");
    }
  }
}
