package test;


/**
 * This is a JUnit class that tests File
 * 
 * 
 *
 */
import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;


public class FileTest {
  /**
   * Stores the instance of Echo being tested
   */
  private Directory root;
  private FileSystem fileSystem;

  @Before
  /**
   * Sets up file system and Echo
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
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

  /**
   * When the parameter are all valid
   */
  @SuppressWarnings("unused")
  @Test
  public void testConstructor1() {
    try {
      File file = new File("newfile", root);
      File check = (File) fileSystem.pathExists("newfile");
      assertEquals("newfile", check.getFileName());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.printStackTrace();
    }
  }

  /**
   * When the file name is invalid
   */
  @SuppressWarnings("unused")
  @Test
  public void testConstructor2() {
    try {
      File file = new File("newf ile.", root);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Some charcaters in file name cannot be used in naming of the file",
          e.getMessage());
    }
  }

  /**
   * Testing the method get file data of class file
   */
  @Test
  public void testGetFileData1() {
    try {
      File file = new File("newfile", root);
      file.storeFileData("ABCD");
      assertEquals("ABCD", file.getFileData());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.getMessage();
    }
  }

  /**
   * Testing the method get file data of class file, if the file has not data
   */
  @Test
  public void testGetFileDat2a() {
    try {
      File file = new File("newfile", root);
      assertEquals(null, file.getFileData());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.getMessage();
    }
  }

  /**
   * Testing the method get file name of class file
   */
  @Test
  public void testGetFileName() {
    try {
      File file = new File("newfile", root);
      assertEquals("newfile", file.getFileName());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.getMessage();
    }
  }

  /**
   * Testing the method set file name of class file
   */
  @Test
  public void testSetFileName() {
    try {
      File file = new File("newfile", root);
      file.setFileName("file");
      assertEquals("file", file.getFileName());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.getMessage();
    }
  }
}
