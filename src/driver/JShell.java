
package driver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import commands.Parser;
import exceptions.AlreadyExistsException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;

/**
 * This class allows the user to freely manipulate a file system by entering commands into the
 * JShell
 * 
 * 
 */
public class JShell {
  /**
   * Stores true value in order for loop to continuously run
   */
  private boolean promptUser;

  /**
   * JShell Constructor
   */
  private JShell() {
    promptUser = true;
  }

  /**
   * This is the main function of the class that operates the JShell by first booting the JShell
   * then prompting the user continually for input. Throws exceptions if input is invalid and exits
   * the program upon exit command
   * 
   * @param args The arguments from the command line
   * @throws IOException
   * @throws FileNotFoundException
   * @throws InvalidArgumentException
   */
  public static void main(String[] args)
      throws FileNotFoundException, IOException, InvalidArgumentException {
    // Continually prompts the user for input
    JShell shell = new JShell();
    Scanner in = new Scanner(System.in);
    while (shell.promptUser) {
      try {
        Parser input = new Parser();
        input.parseUserInput(in.nextLine());
      } catch (InvalidArgumentException | InvalidPathException | AlreadyExistsException
          | InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException | NoSuchMethodException | SecurityException
          | ClassNotFoundException e) {
        System.out.println(e.getMessage());
      }
    }
    in.close();
  }

}
