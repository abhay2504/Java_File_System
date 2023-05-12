package test;


/**
 * This is a JUnit class that tests Copy
 * 
 * 
 *
 */
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import commands.Copy;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;

import java.lang.reflect.Field;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

public class CopyTest {
  /**
   * Stores the instance of the file system being worked on
   */
  private FileSystem fileSystem;
  private Directory root;
  /**
   * Stores the instance of Copy being tested
   */
  private Copy cp;

  @Before
  /**
   * Sets up file system and Echo
   * 
   * @throws Exception
   */
  public void setUp() throws Exception {
    root = new Directory("root", null);
    fileSystem = FileSystem.createFileSystemInstance(root);
    cp = new Copy(fileSystem);
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
   * This part tests the method copy file of Copy class, when input is valid and all the paths
   * exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testCopyFile1() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      cp.copyFile("newfile", "newDir", root);
      File check = (File) fileSystem.pathExists("newDir/newfile");
      assertEquals("newfile", ((File) fileSystem.pathExists("newDir/newfile")).getFileName());
      assertEquals("newfile", ((File) fileSystem.pathExists("/newfile")).getFileName());
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.printStackTrace();
    }
  }

  /**
   * This part tests the method copy file of Copy class, when the destination path contains more
   * than one parts that do not exist
   */
  @SuppressWarnings("unused")
  @Test
  public void testCopyFile2() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      cp.copyFile("newfile", "newDir/newdir/file", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("Invalid Path", e.getMessage());
    }
  }

  /**
   * This part tests the method copy file of Copy class, when the file is renamed when copied from
   * one directory to another
   */
  @Test
  public void testCopyFile3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      cp.copyFile("newfile", "newDir/movedFile", newDir);
      File check = (File) fileSystem.pathExists("newDir/movedFile");
      assertEquals("movedFile", check.getFileName());
      assertEquals("newfile", ((File) fileSystem.pathExists("/newfile")).getFileName());
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method copy file of Copy class, when the destination path is a file
   */
  @Test
  public void testMoveFile4() throws InvalidArgumentException {
    File file = new File("file", root);
    file.storeFileData("GHI");
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      cp.copyDirectory("newfile", "file", root);
      assertEquals("ABCD", file.getFileData());
      assertEquals(null, fileSystem.pathExists("newfile"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method copy directory of Copy class, when input is valid and all the paths
   * exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testCopyDirectory1() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    try {
      cp.copyDirectory("newDir2", "newDir", root);
      Directory check = (Directory) fileSystem.pathExists("newDir/newDir2");
      assertEquals("newDir2", check.getDirectoryName());
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals("newDir2", ((Directory) fileSystem.pathExists("newDir2")).getDirectoryName());
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.printStackTrace();
    }
  }

  /**
   * This part tests the method copy directory of Copy class, when the destination path contains
   * more than one parts that do not exist
   */
  @SuppressWarnings("unused")
  @Test
  public void testCopyDirectory2() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    try {
      cp.copyDirectory("newDir2", "dir/dir2", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("Invalid Path", e.getMessage());
    }
  }

  /**
   * This part tests the method copy directory of Copy class, when the directory is renamed when
   * moved from one directory to another
   */
  @Test
  public void testCopyDirectory3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    try {
      cp.copyDirectory("newDir2", "newDir/movedDir", newDir);
      Directory check = (Directory) fileSystem.pathExists("newDir/movedDir");
      assertEquals("movedDir", check.getDirectoryName());
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals(null, fileSystem.pathExists("/root/newDir2"));
    } catch (InvalidArgumentException | InvalidPathException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method copy directory of Copy class, when the destination path is a file
   */
  @SuppressWarnings("unused")
  @Test
  public void testCopyDirectory4() throws InvalidArgumentException {
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    try {
      cp.copyDirectory("newDir2", "newfile", root);
    } catch (InvalidArgumentException | InvalidPathException e) {
      assertEquals("cp: newfile is not a directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the userInput has less arguments
   */
  @Test
  public void testRunCommand1() throws InvalidArgumentException {
    String userInput[] = {"cp"};
    try {
      cp.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("Missing Arguments,paths provided are insufficient", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the source path does not exists
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand2() throws InvalidArgumentException {
    Directory newDir2 = new Directory("newDir2", root);
    String userInput[] = {"cp", "newfile", "newDir2"};
    try {
      cp.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("cp:newfile no such file or directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the input is valid and source path is a
   * file and destination is a directory
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand3() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    String userInput[] = {"cp", "newfile", "newDir"};
    try {
      cp.runCommand(userInput);
      File check = (File) fileSystem.pathExists("newDir/newfile");
      assertEquals("newfile", check.getFileName());
      assertEquals("newfile", ((File) fileSystem.pathExists("newfile")).getFileName());
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the input is valid and source path is a
   * file and destination is a file
   */
  @Test
  public void testRunCommand4() throws InvalidArgumentException {
    File file = new File("file", root);
    file.storeFileData("GHI");
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    String userInput[] = {"cp", "newfile", "file"};
    try {
      cp.runCommand(userInput);
      assertEquals("ABCD", file.getFileData());
      assertEquals("newfile", ((File) fileSystem.pathExists("newfile")).getFileName());
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the input is valid and source path is a
   * Directory and destination is a file
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand5() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    File newfile = new File("newfile", root);
    newfile.storeFileData("ABCD");
    String userInput[] = {"cp", "newDir", "newfile"};
    try {
      cp.runCommand(userInput);
    } catch (InvalidPathException | InvalidArgumentException e) {
      assertEquals("cp: newfile is not a directory", e.getMessage());
    }
  }

  /**
   * This part tests the method runCommand of Copy class, if the input is valid and source path is a
   * Directory and destination is a directory
   */
  @SuppressWarnings("unused")
  @Test
  public void testRunCommand6() throws InvalidArgumentException {
    Directory newDir = new Directory("newDir", root);
    Directory newDir2 = new Directory("newDir2", root);
    File newfile = new File("newfile", newDir2);
    newfile.storeFileData("ABCD");
    String userInput[] = {"cp", "newDir2", "newDir"};
    try {
      cp.runCommand(userInput);
      Directory check = (Directory) fileSystem.pathExists("newDir/newDir2");
      assertEquals("newfile", check.getChildFiles().get(0).getFileName());
      assertEquals("newDir2", ((Directory) fileSystem.pathExists("newDir2")).getDirectoryName());
    } catch (InvalidPathException | InvalidArgumentException e) {
      e.getMessage();
    }
  }
}
