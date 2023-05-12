package commands;


import exceptions.AlreadyExistsException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.FileSystem;

/**
 * This class creates a new directories with given path and throws appropriate exceptions when there
 * is an error
 *
 * 
 */
public class MakeDirectory implements Command {
  /**
   * Stores the instance of the current FileSystem being worked on.
   */
  FileSystem fileSystem;

  /**
   * This method is a constructor for MakeDirectory and initializes the fileSystem instance variable
   * 
   * @param fileSystem
   */
  public MakeDirectory(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }


  /**
   * This method is creates as many directory as given in userInput. (userInput.length -1) If first
   * directory could not be created successfully, the second directory will not be created. In the
   * case of either directory failed to be created, it throws an Exception. If there is no path
   * given, it throws InvalidArgumentException
   * 
   * @param userInput list of userInputs
   * @exception InvalidArgumentException on not enough user input passed to the method
   * @exception AlreadyExistsException on directory path that already exists
   * @exception InvalidPathException on path that does not exist in the fileSystem
   * @return empty string
   */
  public String runCommand(String[] userInput)
      throws InvalidArgumentException, AlreadyExistsException, InvalidPathException {
    if (userInput.length < 2) {
      throw new InvalidArgumentException("mkdir: not enough arguments");
    }
    try {
      for (int i = 1; i < userInput.length; i++) {
        makeDirectory(userInput[i]);
      }
    } catch (InvalidPathException e) {
      throw new InvalidPathException(e.getMessage());
    } catch (AlreadyExistsException e) {
      throw new AlreadyExistsException(e.getMessage());
    }
    return "";
  }

  /**
   * This method is creates a new directory with path of newPathString. If directory could not be
   * created successfully, it throws an Exception
   * 
   * @param newPathString This is the path representation of new directory
   * @exception AlreadyExistsException on directory path that already exists
   * @exception InvalidPathException on path that does not exist in the fileSystem
   */
  private void makeDirectory(String newPathString)
      throws AlreadyExistsException, InvalidPathException, InvalidArgumentException {
    if (newPathString.contains("//")) {
      throw new InvalidPathException("mkdir: " + newPathString + ": path can " + "not contain //");
    }
    int indexSlash = newPathString.lastIndexOf('/');
    String newDirName = newPathString.substring(indexSlash + 1);
    String newDirPath = "";
    if (indexSlash > 0) {
      newDirPath = newPathString.substring(0, indexSlash);
    }
    // Add directly under
    if (newDirPath.equals("")) {
      Directory parentDir = fileSystem.getCurrentDirectory();
      if (parentDir.pathExistUnder(newDirName) != null) {
        throw new AlreadyExistsException("mkdir: " + newPathString + ": File " + "exists");
      }
      new Directory(newDirName, parentDir);
      return;
    }
    // Other cases
    Object pathObj = fileSystem.pathExists(newDirPath);
    if (pathObj instanceof Directory) {
      Directory parentDir = (Directory) pathObj;
      if (parentDir.pathExistUnder(newDirName) != null) {
        throw new AlreadyExistsException("mkdir: " + newPathString + ": File " + "exists");
      }
      new Directory(newDirName, parentDir);
      return;
    }
    // If not path found
    // Error: Not accurate path was given
    throw new InvalidPathException(
        "mkdir: " + newPathString.substring(0, indexSlash) + ": no such file " + "or directory ");
  }
}
