package commands;



import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
import java.util.ArrayList;

/**
 * This class removes a directory and its child from the fileSystem
 *
 * 
 */
public class Remove implements Command {

  /**
   * This is the fileSystem to be modified
   */
  FileSystem fileSystem;

  /**
   * This method is a constructor for Remove and initializes the fileSystem instance variable
   *
   * @param fileSystem
   */
  public Remove(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   *
   * @param userInput A list containing the command and given user arguments.
   * @throws InvalidPathException when given path doesn't exist in the fileSystem
   * @throws InvalidArgumentException when there is too many or not enough useInputs
   * @return empty string
   */
  @Override
  public String runCommand(String[] userInput)
      throws InvalidPathException, InvalidArgumentException {
    if (userInput.length < 2) {
      throw new InvalidArgumentException("cd: not enough arguments");
    } else if (userInput.length > 2) {
      throw new InvalidArgumentException("cd: too many arguments");
    }
    Object pathObject = fileSystem.pathExists(userInput[1]);
    if (pathObject instanceof Directory) {
      Directory trashDir = (Directory) pathObject;
      Directory parentDir = trashDir.getParentDirectory();
      // If dir is ancestor of CWD don't delete
      if (!isRemovable(fileSystem.getCurrentDirectory(), trashDir)) {
        throw new InvalidPathException("rm: " + userInput[1] + ": ancestor "
            + "directory of current working directory or root directory " + "cannot be removed");
      }

      ArrayList<Directory> dirList = parentDir.getChildDirectories();
      dirList.remove(trashDir);
      parentDir.setChildDirectories(dirList);
      return "";
    } else if (pathObject instanceof File) {
      throw new InvalidPathException("rm: " + userInput[1] + ": Not a directory");
    }
    throw new InvalidPathException("rm: " + userInput[1] + ": No such file or " + "directory");
  }

  /**
   * This method checks if trashDir is removable from the filesytem given the current working
   * directory(cwd). It is not removable if trashDir is ancestor of cwd or trashDir is rootDirectory
   * 
   * @param cwd the current working directory of file syetem
   * @param trashDir the directory to be removed
   * @return return true if trashDir doesn't violate any rules, otherwise return false
   */
  private boolean isRemovable(Directory cwd, Directory trashDir) {
    if (cwd.equals(trashDir))
      return false;
    for (int j = 0; j < trashDir.getChildDirectories().size(); j++) {
      Directory childDir = trashDir.getChildDirectories().get(j);
      if (childDir.equals(cwd)) {
        return false;
      }
      if (!isRemovable(cwd, childDir)) {
        return false;
      }
    }
    return true;
  }
}
