package commands;

import java.io.IOException;
import exceptions.AlreadyExistsException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;

/**
 * This class serves as the interface for all the subclasses of Command.
 *
 *
 */
public interface Command {
  /**
   * This method will execute the appropriate procedures/methods in order for the user given command
   * to be executed.
   * 
   * @param userInput A list containing the command and given user arguments.
   * @return None
   * @throws InvalidPathException On invalid path input
   * @throws InvalidArgumentException On invalid argument input
   * @throws AlreadyExistsException On a directory/file that already exists
   * @throws IOException On inappropriate input/output
   * @throws ClassNotFoundException On classes that cannot be found
   */
  public String runCommand(String[] userInput) throws InvalidPathException,
      InvalidArgumentException, AlreadyExistsException, IOException, ClassNotFoundException;
}
