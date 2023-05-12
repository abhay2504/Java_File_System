package commands;


import exceptions.InvalidArgumentException;
import filesystem.FileSystem;

/**
 * This class implements the exit command, which upon being called quits the JShell
 * 
 * 
 */
public class Exit implements Command {

  @SuppressWarnings("unused")
  /**
   * Stores the current instance of the filesystem being worked on
   */
  private FileSystem fileSystem;

  /**
   * Initializes fileSystem with the instance of the current FileSystem being worked on.
   */
  public Exit(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * This method quits the current JShell instance. Throws exception if there are arguments given by
   * user
   * 
   * @param userInput A list containing the command and possibly arguments given by the user
   * @exception InvalidArgumentException on too many user arguments
   * @return Nothing
   */
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    if (userInput.length > 1)
      throw new InvalidArgumentException("There are too many " + "arguments\n");
    // Quit the program
    System.exit(0);
    return "\n";
  }

}
