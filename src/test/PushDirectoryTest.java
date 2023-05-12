package test;


/**
 * This is a JUnit class that tests PushDirectory
 * 
 * 
 *
 */
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.PushDirectory;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class PushDirectoryTest {
  private FileSystem fileSystem;
  private Directory root;
  private PushDirectory pushd;

  @Before
  /**
   * Sets up file system and PushDirectory
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    pushd = new PushDirectory(fileSystem);
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
   * This part tests the method runCommand when arguments are missing
   * 
   * @throws InvalidArgumentException Argument missing, no directory name
   */
  @Test
  public void testNotEnoughArgs() throws InvalidArgumentException, InvalidPathException {
    String userInput[] = {"pushd"};
    try {
      pushd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Argument missing, no directory name", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when there are too many arguments
   * 
   * @throws InvalidArgumentException Argument missing, no directory name
   */
  @Test
  public void testTooManyArgs() throws InvalidArgumentException, InvalidPathException {
    String userInput[] = {"pushd", "d", "e"};
    try {
      pushd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("too many Arguments", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when the Directory does not exist
   * 
   * @throws InvalidPathException Argument missing, no directory name
   */
  @Test
  public void testDirectoryDoesNotExists() throws InvalidArgumentException, InvalidPathException {
    String userInput[] = {"pushd", "d"};
    try {
      pushd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException e) {
      assertEquals("No such Directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when the argument is a file
   * 
   * @throws InvalidPathException Argument missing, no directory name
   */
  @SuppressWarnings("unused")
  @Test
  public void testArgIsFile() throws InvalidArgumentException, InvalidPathException {
    File newfile = new File("newfile", root);
    String userInput[] = {"pushd", "newfile"};
    try {
      pushd.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException e) {
      assertEquals("newfile is a file, it should be a directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when input is valid
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand() throws InvalidArgumentException, InvalidPathException {
    Directory dir1 = new Directory("dir1", root);
    String userInput[] = {"pushd", "dir1"};
    try {
      pushd.runCommand(userInput);
      assertEquals("dir1", fileSystem.getCurrentDirectory().getDirectoryName());
    } catch (InvalidPathException e) {
      fail("should not throw an exception");
      e.getStackTrace();
    }
  }

}
