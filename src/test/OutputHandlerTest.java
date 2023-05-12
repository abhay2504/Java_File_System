package test;


import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.OutputHandler;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.FileSystem;

public class OutputHandlerTest {
  /**
   * Stores the instance of the file system being worked on
   */
  FileSystem fileSystem;
  /**
   * Stores the instance of OutputHandler being tested
   */
  OutputHandler output;
  /**
   * Stores the instance of ByteArrayOutputStream being used
   */
  ByteArrayOutputStream out;

  @Before
  /**
   * Sets up the file system and remove
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    fileSystem = FileSystem.createFileSystemInstance(new Directory("root", null));
    output = new OutputHandler();
    out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

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
   * Tests whether OutputHandler is able to print string to the console
   */
  public void testPrintsOutput() {
    output.print("Hello World");
    assertEquals("Hello World", out.toString().trim());
  }

  @Test
  /**
   * Tests whether OutputHandler is able to append to existing file, or create it if it doesn't
   * exist
   */
  public void testAppendOutput() {
    try {
      output.appendToFile("Hello World", "/intro");
      assertEquals("Hello World",
          fileSystem.getRootDirectory().getChildFiles().get(0).getFileData());
      output.appendToFile("There is more!", "/intro");
      assertEquals("Hello World\nThere is more!",
          fileSystem.getRootDirectory().getChildFiles().get(0).getFileData());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
    }
  }

  @Test
  /**
   * Tests whether OutputHandler is able to overwrite to existing file, or create it if it doesn't
   * exist
   */
  public void testOverwriteOutput() {
    try {
      output.overwriteToFile("Hello World", "/intro");
      assertEquals("Hello World",
          fileSystem.getRootDirectory().getChildFiles().get(0).getFileData());
      output.overwriteToFile("Goodbye World!", "/intro");
      assertEquals("Goodbye World!",
          fileSystem.getRootDirectory().getChildFiles().get(0).getFileData());
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
    }
  }

  @Test
  /**
   * Tests whether OutputHandler is able to recognize invalid file input
   */
  public void testInvalidFilePath() {
    try {
      output.appendToFile("hello", "");
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("No path provided", e.getMessage());
    }
    try {
      output.overwriteToFile("hello", "/hello/");
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Invalid Path", e.getMessage());
    }
    try {
      output.appendToFile("hello", "/");
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("/" + ": is a directory", e.getMessage());
    }

  }

}
