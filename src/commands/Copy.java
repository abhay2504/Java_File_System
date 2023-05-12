package commands;


import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This class implements the copy command, it traverser userInput and throws exceptions if the file
 * does not exist/ the userInput has arguments missing
 * 
 * 
 */
public class Copy implements Command {
  /**
   * Stores the current instance of the filesystem being worked on
   */
  private FileSystem fileSystem;

  /**
   * Initializes fileSystem with the instance of the current FileSystem being worked on.
   * 
   * @param fSystem Is stored in this.fileSystem.
   * @return Nothing
   */
  public Copy(FileSystem fSystem) {
    fileSystem = fSystem;
  }

  /**
   * This method copies a directory from one location to another location and return errors if the
   * destination path is not a directory/invalid or the name of the new directory is invalid.
   * 
   * @param srcPath The path of the directory to be copied
   * @param dstPath The path where directory is to be copied
   * @param pDir The parent directory of the destination path if any
   * @throws InvalidPathException If the destination path is not a directory
   * @throws InvalidPathException If the destination path has more than one directories that do not
   *         exist
   * @throws InvalidArgumentException If Directory names are invalid
   */
  public void copyDirectory(String srcPath, String dstPath, Directory pDir)
      throws InvalidPathException, InvalidArgumentException {
    if (fileSystem.pathExists(dstPath) instanceof File) {
      throw new InvalidPathException("cp: " + dstPath + " is not a directory");
    }
    Directory srcDir = (Directory) fileSystem.pathExists(srcPath);
    Directory dstDir = null, newDir = null;
    if (fileSystem.pathExists(dstPath) == null) {
      if (dstPath.contains("/")) {
        String path = dstPath.substring(0, dstPath.lastIndexOf("/"));
        if (fileSystem.pathExists(path) == null) {
          throw new InvalidPathException("Invalid Path");
        }
      }
      String dName = dstPath.substring(dstPath.lastIndexOf("/") + 1);
      newDir = new Directory(dName, pDir);
    } else {
      dstDir = (Directory) fileSystem.pathExists(dstPath);
      newDir = new Directory(srcDir.getDirectoryName(), dstDir);
    }
    for (int i = 0; i < srcDir.getChildFiles().size(); i++) {
      newDir.addChildFile(srcDir.getChildFiles().get(i));
    }
    for (int i = 0; i < srcDir.getChildDirectories().size(); i++) {
      newDir.addChildDirectory(srcDir.getChildDirectories().get(i));
    }
  }

  /**
   * This method copies a file from one location to another location and return errors if the
   * destination path is invalid or the name of the new directory is invalid.
   * 
   * @param srcPath The path of the file to be copied
   * @param dstPath The path where file is to be copied
   * @param pDir The parent directory of the destination path if any
   * @throws InvalidArgumentException If file name is invalid
   * @throws InvalidPathException If the destination path has more than one directories that do not
   *         exist
   */
  public void copyFile(String srcPath, String dstPath, Directory pDir)
      throws InvalidArgumentException, InvalidPathException {
    String fName = "";
    File newfile = (File) fileSystem.pathExists(srcPath);
    if (fileSystem.pathExists(dstPath) != null) {
      if (fileSystem.pathExists(dstPath) instanceof Directory) {
        Directory tempDir = (Directory) fileSystem.pathExists(dstPath);
        tempDir.addChildFile(newfile);
      } else {
        String Data = newfile.getFileData();
        File dstFile = (File) fileSystem.pathExists(dstPath);
        dstFile.storeFileData(Data);
      }
    } else {
      if (dstPath.contains("/") && dstPath.substring(0, dstPath.lastIndexOf("/")).contains("/")) {
        throw new InvalidPathException("Invalid Path");
      }
      fName = dstPath.substring(dstPath.lastIndexOf("/") + 1);
      File copiedFile = new File(fName, pDir);
      copiedFile.storeFileData(newfile.getFileData());
    }
  }

  /**
   * This method copies the contents of source file either into the new file or copies the file into
   * the provided directory
   * 
   * @throws InvalidArgumentException If no paths in userInput
   * @throws InvalidArgumentException If copy directory is not a recursive call
   * @throws InvalidPathException If the source file/directory does not exist
   */
  public String runCommand(String[] userInput)
      throws InvalidPathException, InvalidArgumentException {
    if (userInput.length <= 2) {
      throw new InvalidArgumentException("Missing Arguments,paths provided are insufficient");
    }
    if (userInput.length > 3) {
      throw new InvalidArgumentException("Extra Arguments provided");
    }
    String srcPath = "", dstPath = "";
    srcPath = userInput[1];
    dstPath = userInput[2];
    Directory parentDir = fileSystem.getCurrentDirectory();
    if (dstPath.contains("/")) {
      if (dstPath.length() == 1) {
        parentDir = null;
      } else if (dstPath.substring(0, dstPath.lastIndexOf("/")).equals("")) {
        parentDir = fileSystem.getCurrentDirectory();
      } else {
        parentDir =
            (Directory) fileSystem.pathExists(dstPath.substring(0, dstPath.lastIndexOf("/")));
      }
    }
    if (fileSystem.pathExists(srcPath) == null) {
      throw new InvalidPathException("cp:" + srcPath + " no such file or directory");
    }
    if (fileSystem.pathExists(srcPath) instanceof Directory) {
      copyDirectory(srcPath, dstPath, parentDir);
    } else {
      copyFile(srcPath, dstPath, parentDir);
    }
    return "";
  }

}
