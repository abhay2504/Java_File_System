package commands;


import exceptions.InvalidArgumentException;
import filesystem.FileSystem;

/**
 * This class prints the current working directory of the given fileSystem
 *
 * 
 */
public class PrintWorkingDirectory implements Command {
  /**
   * Stores the instance of the current FileSystem being worked on.
   */
  FileSystem fileSystem;

  /**
   * This method is a constructor for PrintWorkingDirectory and initializes the fileSystem instance
   * variable
   * 
   * @param fileSystem fileSystem that we are working on
   */
  public PrintWorkingDirectory(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  @Override

  /**
   * This method prints the currentWorkingDirectory in the form of Absolute Path Exception is thrown
   * on incorrect number of userInputs
   * 
   * @param userInput list of userInputs
   * @exception InvalidArgumentException on too many or not enough user input passed to the method
   * @return String representation of current working directory (absolute path)
   */
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    if (userInput.length > 1) {
      throw new InvalidArgumentException("pwd: too many arguments");
    }
    return fileSystem.getCurrentDirectory().getAbsolutePath();
  }
}
