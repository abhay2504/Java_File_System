package test;


/**
 * This is a JUnit class that tests Move
 * 
 * 
 *
 */
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Move;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;

import java.lang.reflect.Field;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class MoveTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Move being tested
   */
  private Move mv;

  @Before
  /**
   * Sets up file system and Echo
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    mv = new Move(fileSystem);
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
   * This part tests the method move file of Move class, when input is valid and all the paths
   * exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testMoveFile1() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      mv.moveFile("newfile", "newDir", root);
      File check = (File) fileSystem.pathExists("newDir/newfile");
      assertEquals("newfile", check.getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newfile"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.printStackTrace();
    }
  }

  /**
   * This part tests the method move file of Move class, when the destination path contains more
   * than one parts that donot exist
   */
  @SuppressWarnings("unused")
  @Test
  public void testMoveFile2() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      mv.moveFile("newfile", "newDir/newdir/file", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("Invalid Path", e.getMessage());
    }
  }

  /**
   * This part tests the method move file of Move class, when the file is renamed when moved from
   * one directory to another
   */
  @Test
  public void testMoveFile3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      mv.moveFile("newfile", "newDir/movedFile", newDir);
      File check = (File) fileSystem.pathExists("newDir/movedFile");
      assertEquals("movedFile", check.getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newfile"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }


  /**
   * This part tests the method move file of Move class, when the destination path is a file
   */
  @Test
  public void testMoveFile4() throws InvalidArgumentException {
    File file = new File("file", root);
    file.storeFileData("GHI");
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      mv.moveDirectory("newfile", "file", root);
      assertEquals("ABCD", file.getFileData());
      assertEquals(null, fileSystem.pathExists("newfile"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method move directory of Move class, when input is valid and all the paths
   * exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testMoveDirectory1() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    try {
      mv.moveDirectory("newDir2", "newDir", root);
      Directory check = (Directory) fileSystem.pathExists("newDir/newDir2");
      assertEquals("newDir2", check.getDirectoryName());
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newDir2"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.printStackTrace();
    }
  }

  /**
   * This part tests the method move directory of Move class, when the destination path contains
   * more than one parts that do not exist
   */
  @SuppressWarnings("unused")
  @Test
  public void testMoveDirectory2() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    try {
      mv.moveDirectory("newDir2", "dir/dir2", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("Invlaid Path", e.getMessage());
    }
  }

  /**
   * This part tests the method move directory of Move class, when the directory is renamed when
   * moved from one directory to another
   */
  @Test
  public void testMoveDirectory3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    try {
      mv.moveDirectory("newDir2", "newDir/movedDir", newDir);
      Directory check = (Directory) fileSystem.pathExists("newDir/movedDir");
      assertEquals("movedDir", check.getDirectoryName());
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newDir2"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method move directory of Move class, when the destination path is a file
   */
  @SuppressWarnings("unused")
  @Test
  public void testMoveDirectory4() throws InvalidArgumentException {
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      mv.moveDirectory("newDir2", "newfile", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("mv: newfile is not a directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the userInput has less arguments
   */
  @Test
  public void testRunCommand1() throws InvalidArgumentException {
    String userInput[] = {"mv"};
    try {
      mv.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("Missing Arguments,paths provided are insufficient", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the source path does not exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand2() throws InvalidArgumentException {
    Directory newDir2 = new Directory("newDir2", root);
    String userInput[] = {"mv", "newfile", "newDir2"};
    try {
      mv.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("mv:newfile no such file/ directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the input is valid and source path is a
   * file and destination is a directory
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    String userInput[] = {"mv", "newfile", "newDir"};
    try {
      mv.runCommand(userInput);
      File check = (File) fileSystem.pathExists("newDir/newfile");
      assertEquals("newfile", check.getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newfile"));
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the input is valid and source path is a
   * file and destination is a file
   */
  @Test
  public void testRunCommand4() throws InvalidArgumentException {
    File file = new File("file", root);
    file.storeFileData("GHI");
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    String userInput[] = {"mv", "newfile", "file"};
    try {
      mv.runCommand(userInput);
      assertEquals("ABCD", file.getFileData());
      assertEquals(null, fileSystem.pathExists("newfile"));
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the input is valid and source path is a
   * Directory and destination is a file
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand5() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    String userInput[] = {"mv", "newDir", "newfile"};
    try {
      mv.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("mv: newfile is not a directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Move class, if the input is valid and source path is a
   * Directory and destination is a directory
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand6() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    String userInput[] = {"mv", "newDir2", "newDir"};
    try {
      mv.runCommand(userInput);
      Directory check = (Directory) fileSystem.pathExists("newDir/newDir2");
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newDir2"));
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }
}
