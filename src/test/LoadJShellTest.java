package test;


import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.LoadJShell;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.FileSystem;

/**
 * This is a JUnit class that tests LoadJShell
 * 
 * 
 *
 */
public class LoadJShellTest {
  /**
   * Stores the instance of the file system being worked on
   */
  FileSystem fileSystem;
  /**
   * Stores the instance of LoadJShell being tested
   */
  LoadJShell load;


  @Before
  /**
   * Sets up the file system, LoadJShell, and mock SaveJshell command
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    fileSystem = FileSystem.createFileSystemInstance(new Directory("root", null));
    load = new LoadJShell(fileSystem);
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
    field.set(null, null);
  }

  @Test
  /**
   * Tests LoadJShell on a file that doesn't exist in the user's home directory
   */
  public void testNonExistingFile() {
    String[] userInput = {"loadJShell", "doesnotexit"};
    try {
      load.runCommand(userInput);
      fail("should throw an exception");
    } catch (ClassNotFoundException | InvalidArgumentException | IOException e) {
      assertEquals("loadJShell: file not found", e.getMessage());
    }
  }

  @Test
  /**
   * Tests a file that exists in the user's home directory, but is invalid
   */
  public void testExistingInvalidFile() {
    String[] loadUserInput = {"loadJShell", "invalidtestfile"};
    try {
      load.runCommand(loadUserInput);
      fail("should throw an exception");
    } catch (ClassNotFoundException | InvalidArgumentException | IOException e) {
      assertEquals("loadJShell: file not compatible", e.getMessage());
    }
  }

  @Test
  /**
   * Tests a valid file in the user's home directory
   */
  public void testValidFile() {
    String[] loadUserInput = {"loadJShell", "validtestfile"};
    try {

      load.runCommand(loadUserInput);
      fileSystem = FileSystem.createFileSystemInstance(null);
      assertEquals("Dir1", fileSystem.getCurrentDirectory().getDirectoryName());
      assertEquals("[mkdir Dir1 Dir2, pushd /Dir1, pwd, saveJShell validtestfile]",
          fileSystem.getHistory().toString());
      assertEquals("root", fileSystem.getTop().getDir().getDirectoryName());
      assertEquals(2, fileSystem.getRootDirectory().getChildDirectories().size());
      assertEquals(true, fileSystem.getRootDirectory().getChildFiles().isEmpty());
    } catch (ClassNotFoundException | InvalidArgumentException | IOException e) {
      fail("should not throw an exception");
    }
  }
}
