package commands;


import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This class implements the Move command, it traverser userInput and throws exceptions if the file
 * does not exist/ the userInput has arguments missing
 * 
 * 
 */
public class Move implements Command {
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
  public Move(FileSystem fSystem) {
    fileSystem = fSystem;
  }

  /**
   * This method moves a directory from one location to another location and return errors if the
   * destination path is not a directory/invalid or the name of the new directory is invalid.
   * 
   * @param srcPath The path of the directory to be moved
   * @param dstPath The path where directory is to be moved
   * @param pDir The parent directory of the destination path if any
   * @throws InvalidPathException If the destination path is not a directory
   * @throws InvalidPathException If the destination path has more than one directories that do not
   *         exist
   * @throws InvalidArgumentException If Directory names are invalid
   */
  public void moveDirectory(String srcPath, String dstPath, Directory pDir)
      throws InvalidPathException, InvalidArgumentException {
    if (fileSystem.pathExists(dstPath) instanceof File) {
      throw new InvalidPathException("mv: " + dstPath + " is not a directory");
    }
    Directory srcDir = (Directory) fileSystem.pathExists(srcPath);
    Directory dstDir, newDir = null;
    if (fileSystem.pathExists(dstPath) == null) {
      if (dstPath.contains("/")) {
        String path = dstPath.substring(0, dstPath.lastIndexOf("/"));
        if (fileSystem.pathExists(path) == null) {
          throw new InvalidPathException("Invlaid Path");
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
    removeDirectory(srcPath, dstPath, srcDir);
  }

  /**
   * This method removes the file from source location
   * 
   * @param srcPath The path of the file to be moved
   * @param dstPath The path where file is to be moved
   * @param srcDir The directory at src_path
   */
  public void removeDirectory(String srcPath, String dstPath, Directory srcDir) {
    Directory srcPathDir = fileSystem.getCurrentDirectory();
    if (srcPath.contains("/")) {
      if (srcPath.substring(0, srcPath.lastIndexOf("/")).equals("")) {
        srcPathDir = fileSystem.getCurrentDirectory();
      } else {
        srcPathDir =
            (Directory) fileSystem.pathExists(srcPath.substring(0, srcPath.lastIndexOf("/")));
      }
    }
    for (int i = 0; i < srcPathDir.getChildDirectories().size(); i++) {
      if (srcPathDir.getChildDirectories().get(i) == srcDir) {
        srcPathDir.getChildDirectories().remove(i);
      }
    }
  }

  /**
   * This method moves a file from one location to another location and return errors if the
   * destination path is invalid or the name of the new directory is invalid.
   * 
   * @param srcPath The path of the file to be moved
   * @param dstPath The path where file is to be moved
   * @param pDir The parent directory of the destination path if any
   * @throws InvalidArgumentException If file name is invalid
   * @throws InvalidPathException If the destination path has more than one directories that do not
   *         exist
   */
  public void moveFile(String srcPath, String dstPath, Directory pDir)
      throws InvalidArgumentException, InvalidPathException {
    String fName = dstPath.substring(dstPath.lastIndexOf("/") + 1);
    File srcFile = (File) fileSystem.pathExists(srcPath);
    if (fileSystem.pathExists(dstPath) != null) {
      if (fileSystem.pathExists(dstPath) instanceof File) {
        File tempFile = (File) fileSystem.pathExists(dstPath);
        tempFile.storeFileData(srcFile.getFileData());
      }
      if (fileSystem.pathExists(dstPath) instanceof Directory) {
        Directory tempDir = (Directory) fileSystem.pathExists(dstPath);
        tempDir.addChildFile(srcFile);
      }
    } else {
      if (dstPath.contains("/")) {
        fName = dstPath.substring(dstPath.lastIndexOf("/") + 1);
        if (pDir == null) {
          throw new InvalidPathException("Invlaid Path");
        }
      }
    }
    File copiedFile = new File(fName, pDir);
    copiedFile.storeFileData(srcFile.getFileData());
    removeFile(srcPath, dstPath, srcFile);
  }

  /**
   * This method removes the file from source location
   * 
   * @param srcPath The path of the file to be moved
   * @param dstPath The path where file is to be moved
   * @param srcFile The file at src_path
   */
  public void removeFile(String srcPath, String dstPath, File srcFile) {
    Directory srcPathDir = fileSystem.getCurrentDirectory();
    if (srcPath.contains("/")) {
      if (srcPath.substring(0, srcPath.lastIndexOf("/")).equals("")) {
        srcPathDir = fileSystem.getCurrentDirectory();
      } else {
        srcPathDir =
            (Directory) fileSystem.pathExists(srcPath.substring(0, srcPath.lastIndexOf("/")));
      }
    }
    for (int i = 0; i < srcPathDir.getChildFiles().size(); i++) {
      if (srcPathDir.getChildFiles().get(i) == srcFile) {
        srcPathDir.getChildFiles().remove(i);
      }
    }
  }

  /**
   * This method moves the contents of source file either into the new file or moves the file into
   * the provided directory, thereby removing source file or directory
   * 
   * @throws InvalidArgumentException If no paths in userInput
   * @throws InvalidPathException If the source file or directory does not exist
   */
  public String runCommand(String[] userInput)
      throws InvalidPathException, InvalidArgumentException {
    if (userInput.length <= 2) {
      throw new InvalidArgumentException("Missing Arguments,paths provided are insufficient");
    }
    String srcPath = userInput[1], dstPath = userInput[2];
    if (fileSystem.pathExists(srcPath) == null) {
      throw new InvalidPathException("mv:" + srcPath + " no such file/ directory");
    }
    Directory parentDir = fileSystem.getCurrentDirectory();
    if (userInput[2].contains("/")) {
      if (dstPath.length() == 1) {
        parentDir = null;
      } else if (dstPath.substring(0, dstPath.lastIndexOf("/")).equals("")) {
        parentDir = fileSystem.getCurrentDirectory();
      } else {
        parentDir =
            (Directory) fileSystem.pathExists(dstPath.substring(0, dstPath.lastIndexOf("/")));
      }
    }
    if (fileSystem.pathExists(srcPath) instanceof Directory) {
      moveDirectory(srcPath, dstPath, parentDir);
    } else {
      moveFile(srcPath, dstPath, parentDir);
    }
    return "";
  }

}
