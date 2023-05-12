package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This is a JUnit class that tests FileSystem
 * 
 * 
 */
public class FileSystemTest {
  private FileSystem fileSystem;
  private Directory root;

  @Before
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  @Test
  /**
   * Test Constructor only creates one instance of fileSystem (singleton)
   */
  public void testFileSystemSingleton() {
    Directory newRoot;
    try {
      newRoot = new Directory("newRoot", null);
    } catch (InvalidArgumentException e) {
      fail("Something went wrong with set up");
      return;
    }
    FileSystem newFileSystem = FileSystem.createFileSystemInstance(newRoot);
    assertEquals(newFileSystem, fileSystem);
  }

  @Test
  /**
   * Test whether pathExists work with root directory symbol ("/")
   */
  public void testPathExistsWithRoot() {
    Object pathObj = fileSystem.pathExists("/");
    assertEquals(root, pathObj);
  }

  @Test
  /**
   * Test whether pathExists work with . and .. symbols
   */
  public void testPathExistsWithDots() {
    Directory dir1;
    Directory dir2;
    try {
      dir1 = new Directory("dir1", root);
      dir2 = new Directory("dir2", dir1);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    Object pathObj = fileSystem.pathExists("./.");
    assertEquals(root, pathObj);
    fileSystem.setCurrentDirectory(dir2);
    pathObj = fileSystem.pathExists("..");
    assertEquals(dir1, pathObj);
  }

  @Test
  /**
   * Test whether pathExists work with directory
   */
  public void testPathExistsWithDir() {
    Directory dir1;
    try {
      dir1 = new Directory("dir1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    Object pathObj = fileSystem.pathExists("/dir1");
    assertEquals(dir1, pathObj);
    pathObj = fileSystem.pathExists("dir1");
    assertEquals(dir1, pathObj);
  }


  @Test
  /**
   * Test whether pathExists work with File
   */
  public void testPathExistsWithFile() {
    File file1;
    try {
      file1 = new File("file1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    Object pathObj = fileSystem.pathExists("/file1");
    assertEquals(file1, pathObj);
    pathObj = fileSystem.pathExists("file1");
    assertEquals(file1, pathObj);
  }

  @SuppressWarnings("unused")
  @Test
  /**
   * Test whether pathExists work when the given path doesn't exist
   */
  public void testPathExistsWithBadPath() {
    Directory dir1;
    try {
      dir1 = new Directory("dir1", root);
    } catch (InvalidArgumentException e) {
      // When something went wrong with creating directories
      fail("Error setting up directories");
      return;
    }
    Object pathObj = fileSystem.pathExists("/dir2");
    assertEquals(null, pathObj);
    pathObj = fileSystem.pathExists("dir2");
    assertEquals(null, pathObj);
  }

  @Test
  /**
   * Test whether pathExists work when the given path is a nested path with multiple depth
   */
  public void testPathExistsWithMultipleDepth() {
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
    Object pathObj = fileSystem.pathExists("/dir1/dir2/dir3");
    assertEquals(dir3, pathObj);
    pathObj = fileSystem.pathExists("dir1/dir2/dir3");
    assertEquals(dir3, pathObj);
  }
}
