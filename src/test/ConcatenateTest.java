package test;


/**
 * This is a JUnit class that tests Concatenate
 * 
 * 
 *
 */
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Concatenate;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class ConcatenateTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Concatenate being tested
   */
  private Concatenate cat;

  @Before
  /**
   * Sets up file system and Concatenate
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    cat = new Concatenate(fileSystem);
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
   * @throws InvalidArgumentException Missing Arguments, no file names
   */
  @Test
  public void testNotEnoughArgs() throws InvalidArgumentException, InvalidPathException {
    String userInput[] = {"cat"};
    try {
      cat.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Missing Arguments, no file names", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when the file path is invalid
   * 
   * @throws InvalidPathException
   */
  public void testInvalidPath() throws InvalidArgumentException, InvalidPathException {
    String userInput[] = {"cat", "newfile"};
    try {
      cat.runCommand(userInput);
      fail("should throw an exception");
    } catch (InvalidPathException e) {
      assertEquals("File:newfile doesnot exist", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of when only 1 file is passed as argument or when 2 files
   * are passed but only 1 file exists
   * 
   * @throws InvalidPathException If file does not exist
   */
  @Test
  public void testSingleFile() throws InvalidArgumentException, InvalidPathException {
    String userInput1[] = {"cat", "newfile"};

    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");

    try {
      assertEquals("ABCD", cat.runCommand(userInput1));
    } catch (InvalidPathException e) {
      fail("should not throw an exception");
      e.getStackTrace();
    }

    String userInput2[] = {"cat", "newfile", "file1"};
    try {
      cat.runCommand(userInput2);
      fail("should throw an exception");
    } catch (InvalidPathException e) {
      assertEquals("ABCD&\nFile:file1 doesnot exist&", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand when multiple files are passed as arguments
   */
  @Test
  public void testMultipleFile() throws InvalidArgumentException, InvalidPathException {
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    File file1 = new File("file1", root);
    file1.storeFileData("DEF");
    String userInput[] = {"cat", "newfile", "file1"};
    try {
      assertEquals("ABCD\n\n\nDEF", cat.runCommand(userInput));
    } catch (InvalidPathException e) {
      fail("should not throw an exception");
      e.getStackTrace();
    }
  }

}
