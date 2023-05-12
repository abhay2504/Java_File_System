package test;


/**
 * This is a JUnit class that tests Echo
 * 
 * 
 *
 */
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commands.Echo;
import exceptions.InvalidArgumentException;

import java.lang.reflect.Field;

import filesystem.Directory;
import filesystem.FileSystem;

public class EchoTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Echo being tested
   */
  private Echo echo;
  private String userInput[] = new String[4];

  @Before
  /**
   * Sets up file system and Echo
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    echo = new Echo(fileSystem);
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
   * This part tests the method is_valid_getfData of Echo class
   */
  @Test
  public void isValidGetFDataTest() {
    userInput[0] = "echo";
    userInput[1] = " bdjdse ";
    try {
      echo.isValidGetFData(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("STRING must be wrapped in double quotes", e.getMessage());
    }

    userInput[1] = " \"ABSDGS\" ";
    try {
      assertEquals("ABSDGS", echo.isValidGetFData(userInput));
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.printStackTrace();
    }

    userInput[1] = " \"ABS\"DGS\" ";
    try {
      echo.isValidGetFData(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Double quotes inside String not allowed", e.getMessage());
    }

    userInput[1] = "";
    try {
      echo.isValidGetFData(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("File data Argument is missing", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Echo class
   */
  @Test
  public void runCommandTest() {
    userInput[0] = "echo";
    userInput[1] = "ABSDGSabdjdse";
    try {
      echo.isValidGetFData(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("STRING must be wrapped in double quotes", e.getMessage());
    }
    userInput[1] = " \"ABCD\" ";
    try {
      assertEquals("ABCD", echo.isValidGetFData(userInput));
    } catch (InvalidArgumentException e) {
      fail("should not throw an exception");
      e.printStackTrace();
    }
    userInput[1] = "\"ABCD\"AD\"";
    try {
      echo.isValidGetFData(userInput);
      fail("should throw an exception");
    } catch (InvalidArgumentException e) {
      assertEquals("Double quotes inside String not allowed", e.getMessage());
    }
  }

}
