package test;


import commands.PrintWorkingDirectory;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
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

public class PrintWorkingDirectoryTest {
  private FileSystem fileSystem;
  private Directory root;
  private PrintWorkingDirectory pwd;

  @Before
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    pwd = new PrintWorkingDirectory(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Tests whether PrintWorkingDirectory can throw appropriate exception with too many arguments
   */
  public void testTooManyArgument() {
    String userInput[] = {"pwd", "/"};
    try {
      String outPut = pwd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("pwd: too many arguments", e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether PrintWorkingDirectory can print valid path with only root
   */
  public void testRootPWD() {
    String userInput[] = {"pwd"};
    try {
      String outPut = pwd.runCommand(userInput);
      assertEquals("/", outPut);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
      fail("Should not throw any exception");
    }
  }

  @Test
  /**
   * Tests whether PrintWorkingDirectory can print valid path when the current working directory is
   * in nested directory
   */
  public void testComplexPWD() {
    String userInput[] = {"pwd"};
    try {
      Directory dir1 = new Directory("dir1", root);
      Directory dir2 = new Directory("dir2", dir1);
      fileSystem.setCurrentDirectory(dir2);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
    }
    try {
      String outPut = pwd.runCommand(userInput);
      assertEquals("/dir1/dir2", outPut);
    } catch (InvalidArgumentException e) {
      e.printStackTrace();
      fail("Should not throw any exception");
    }
  }
}
