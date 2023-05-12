package commands;


import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.util.Arrays;

/**
 * This class will display the list of files and directory names that are in the current working
 * directory of given FileSystem
 *
 * 
 */
public class List implements Command {
  /**
   * Stores the instance of the current FileSystem being worked on.
   */
  FileSystem fileSystem;

  /**
   * This method is a constructor for List and initializes the fileSystem instance variable
   * 
   * @param fileSystem
   */
  public List(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override
  /**
   * For each userInput items which represents paths, this method does following -If item specifies
   * a file, print item - If item specifies a directory, print item colon, then the contents of that
   * directory, then a new line - If item does not exist, print appropriate messages If no path is
   * given, list contents of the currentDirectory for fileSystem
   *
   * @param userInput list of userInputs
   * @return Return the formatted representation of the lists of contents with given userInput
   */
  public String runCommand(String[] userInput)
      throws InvalidArgumentException, InvalidPathException {
    String result = "";
    if (userInput.length == 1) {
      String absolutePath = fileSystem.getCurrentDirectory().getAbsolutePath();
      result = getContentsOfDirectory(absolutePath);
    } else if (userInput[1].equals("-R")) {
      if (userInput.length == 2) {
        String absolutePath = fileSystem.getCurrentDirectory().getAbsolutePath();
        try {
          result += getContentsRecursively(absolutePath, "");
        } catch (InvalidPathException e) {
          throw new InvalidPathException(result + "&\n" + e.getMessage() + "&");
        }
      }
      for (String pathString : Arrays.copyOfRange(userInput, 2, userInput.length)) {
        if (fileSystem.pathExists(pathString) instanceof File) {
          throw new InvalidPathException(
              result + "&\n" + "path to file cannot be" + " used with ls -R" + "&");
        }
        try {
          result += getContentsRecursively(pathString, "");
        } catch (InvalidPathException e) {
          throw new InvalidPathException(result + "&\n" + e.getMessage() + "&");
        }
      }
    } else {
      for (String pathString : Arrays.copyOfRange(userInput, 1, userInput.length)) {
        try {
          result += getContentsOfDirectory(pathString);
        } catch (InvalidPathException e) {
          throw new InvalidPathException(result + "&\n" + e.getMessage() + "&");
        }
      }
    }
    return result;
  }

  /**
   * This method behaves different depending on the type of pathString - If pathString specifies a
   * file, print item - If pathString specifies a directory, print item colon, then the contents of
   * that directory, then a new line - If pathString does not exist, throw InvalidPathException
   *
   * @param pathString This is the path that we are looking for
   * @exception InvalidPathException on path that does not exist in the fileSystem
   */
  private String getContentsOfDirectory(String pathString) throws InvalidPathException {
    String result = "";
    Object pathObject = fileSystem.pathExists(pathString);
    if (pathObject instanceof Directory) {
      result = result + pathString + ":\n";
      Directory directory = (Directory) pathObject;
      for (Directory childDir : directory.getChildDirectories()) {
        result = result + childDir.getDirectoryName() + "\n";
      }
      for (File childFile : directory.getChildFiles()) {
        result = result + childFile.getFileName() + "\n";
      }
    } else if (pathObject instanceof File) {
      result = result + pathString + "\n";
    } else {
      throw new InvalidPathException("ls: " + pathString + ": no such file " + "or directory");
    }
    return result;
  }

  /**
   * This method prints the contents of the directory recursively
   * 
   * @param pathString This is the path that we are looking for
   * @exception InvalidPathException on path that does not exist in the fileSystem
   */
  private String getContentsRecursively(String pathString, String result)
      throws InvalidPathException {
    result += getContentsOfDirectory(pathString);
    Directory directory = (Directory) fileSystem.pathExists(pathString);
    for (Directory childDir : directory.getChildDirectories()) {
      if (pathString.equals("/"))
        result = getContentsRecursively("/" + childDir.getDirectoryName(), result);
      else
        result = getContentsRecursively(pathString + "/" + childDir.getDirectoryName(), result);
    }
    return result;
  }
}
