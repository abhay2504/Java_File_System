package test;


import commands.MakeDirectory;
import exceptions.AlreadyExistsException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.FileSystem;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class that tests MakeDirectory
 * 
 * 
 *
 */
public class MakeDirectoryTest {
  private FileSystem fileSystem;
  private Directory root;
  private MakeDirectory mkdir;

  @Before
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    mkdir = new MakeDirectory(fileSystem);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (fileSystem.getClass()).getDeclaredField("fileSystemReference");
    field.setAccessible(true);
    field.set(null, null); // setting the ref parameter to null
  }

  @Test
  /**
   * Tests whether MakeDirectory can make multiple directories with valid paths
   */
  public void testMakeMultipleDirectories() {
    String userInput[] = {"mkdir", "/dir1", "/dir2"};
    try {
      mkdir.runCommand(userInput);
      assertEquals(2, fileSystem.getRootDirectory().getChildDirectories().size());
      assertTrue(fileSystem.pathExists("/dir1") instanceof Directory);
      assertTrue(fileSystem.pathExists("/dir2") instanceof Directory);

    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      e.printStackTrace();
      fail("Should not throw any exception");
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory can make nested directories
   */
  public void testMakeNestedDirectories() {
    String userInput[] = {"mkdir", "/dir1", "/dir1/dir2"};
    try {
      mkdir.runCommand(userInput);
      assertEquals(1, fileSystem.getRootDirectory().getChildDirectories().size());
      assertEquals(1,
          fileSystem.getRootDirectory().getChildDirectories().get(0).getChildDirectories().size());
      assertTrue(fileSystem.pathExists("/dir1") instanceof Directory);
      assertTrue(fileSystem.pathExists("/dir1/dir2") instanceof Directory);
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      e.printStackTrace();
      fail("Should not throw any exception");
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory can make directory with relative path
   */
  public void testMakeRelativeDirectory() {
    String userInput[] = {"mkdir", "dir1"};
    try {
      mkdir.runCommand(userInput);
      assertEquals(1, fileSystem.getRootDirectory().getChildDirectories().size());
      assertTrue(fileSystem.pathExists("/dir1") instanceof Directory);
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      e.printStackTrace();
      fail("Should not throw any exception");
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory can throw proper error with invalid given path
   */
  public void testMakeDirectoryWithBadPath() {
    String userInput[] = {"mkdir", "/dir1/dr2"};
    try {
      mkdir.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      assertEquals("mkdir: /dir1: no such file " + "or directory ", e.getMessage());
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory can throw proper error with path that already exists. Test it by
   * creating same directory twice.
   */
  public void testMakeAlreadyExistDirectory() {
    String userInput[] = {"mkdir", "/dir1", "/dir1"};
    try {
      mkdir.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      assertEquals("mkdir: /dir1: File exists", e.getMessage());
      assertEquals(1, fileSystem.getRootDirectory().getChildDirectories().size());
      assertTrue(fileSystem.pathExists("/dir1") instanceof Directory);
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory works fine with mix of valid and invalid paths. It should make
   * directory until it hits invalid path
   */
  public void testMixValidAndInvalidPath() {
    String userInput[] = {"mkdir", "/dir1", "/dir2", "/dir1", "dir3"};
    try {
      mkdir.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      assertEquals("mkdir: /dir1: File exists", e.getMessage());
      assertEquals(2, fileSystem.getRootDirectory().getChildDirectories().size());
      assertTrue(fileSystem.pathExists("/dir1") instanceof Directory);
      assertTrue(fileSystem.pathExists("/dir2") instanceof Directory);
      assertTrue(fileSystem.pathExists("/dir3") == null);
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory works fine with mix of valid and invalid paths. It should make
   * directory until it hits invalid path
   */
  public void testPathWithDoubleSlash() {
    String userInput[] = {"mkdir", "//dir1"};
    try {
      mkdir.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      assertEquals("mkdir: //dir1: path can not contain //", e.getMessage());
      assertEquals(0, fileSystem.getRootDirectory().getChildDirectories().size());
    }
  }

  @Test
  /**
   * Tests whether MakeDirectory works fine with mix of valid and invalid paths. It should make
   * directory until it hits invalid path
   */
  public void testNotEnoughArgs() {
    String userInput[] = {"mkdir"};
    try {
      mkdir.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException | InvalidArgumentException | AlreadyExistsException e) {
      assertEquals("mkdir: not enough arguments", e.getMessage());
    }
  }

}
