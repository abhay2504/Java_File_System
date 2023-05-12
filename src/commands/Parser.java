package commands;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import exceptions.AlreadyExistsException;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.FileSystem;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses the user input from the shell line, determining whether the commands are valid,
 * separating the commands from the arguments and calling the appropriate command.
 *
 * 
 */
public class Parser {
  /**
   * Stores the path of the file for redirection
   */
  private String filePath = "";
  /**
   * This stores the type of redirection that will occur
   */
  private String redirection = "";

  /**
   * Stores the command keyword to the appropriate class.
   */
  private HashMap<String, String> validCommands;

  /**
   * Stores the instance of the current FileSystem being worked on.
   */
  private FileSystem fileSystem = null;

  /**
   * This constructor creates an object of Parser, initializing the valid commands.
   * 
   * @param fSystem Is stored in this.fileSystem.
   * @throws InvalidArgumentException
   */
  public Parser() throws InvalidArgumentException {
    this.validCommands = new HashMap<String, String>();
    this.fileSystem = FileSystem.createFileSystemInstance(new Directory("root", null));
    initializeCommandMap();
  }

  /**
   * This method parses the user input, stores the user input in a list, and calls the appropriate
   * method based on the given command.
   * 
   * @param input The string that JShell retrieves from user that will be parsed
   * @return Void
   * @throws InvalidPathException On invalid path input
   * @throws InvalidArgumentException On invalid argument input
   * @throws AlreadyExistsException On a directory/file that already exists
   * @throws ClassNotFoundException When no class with specified name is found
   * @throws NoSuchMethodException When no method with specified name is found
   * @throws InvocationTargetException When the underlying method throws an exception
   * @throws IllegalArgumentException On illegal argument passed in newInstance
   * @throws IllegalAccessException When method does not have access to the definition of specified
   *         class/method
   * @throws InstantiationException When specified class object cannot be instantiated
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void parseUserInput(String input) throws InvalidPathException, InvalidArgumentException,
      AlreadyExistsException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
      ClassNotFoundException, FileNotFoundException, IOException {
    // Remove potential whitespace at the beginning and end of the user input
    input = input.trim();
    // Add input to a list of the history of commands
    fileSystem.populateList(input);
    // Split the input into a list of strings based on the whitespace
    String userInput[] = input.split("\\s+");
    // Give an error and return if the command is not valid
    if (!(isValidCommand(userInput[0])))
      return;
    if (userInput[0].equals("loadJShell") && fileSystem.getHistory().size() == 1) {
      callCommand(userInput);
      return;
    } else if (userInput[0].equals("loadJShell"))
      throw new InvalidArgumentException("command disabled");
    OutputHandler output = new OutputHandler();
    // Check if paths contain multiple slashes except for the mkdir command
    if (input.contains("//") && !userInput[0].equals("mkdir") && !userInput[0].equals("curl"))
      throw new InvalidPathException("path(s) contains multiple slashes");
    // Check if the input uses redirection and parse input accordingly
    if (input.contains(">")) {
      checkRedirectionRequirements(input);
      return;
    } else if (userInput[0].equals("echo")) {
      output.print(checkEchoPrintCommand(input.split(" ", 2)));
      return;
    }
    // Call the appropriate command
    String newOutput = callCommand(userInput);
    if (supportsRedirection(userInput[0]))
      output.print(newOutput);
  }

  /**
   * This method handles the redirection output, passing on the output to either be appended,
   * overwritten, or printed to the console.
   * 
   * @param newOutput List of strings containing output
   * @throws InvalidArgumentException When the given argument is invalid
   * @return Void
   */
  private void handleRedirectionOutput(String[] newOutput) throws InvalidArgumentException {
    OutputHandler output = new OutputHandler();
    if (redirection.equals("append"))
      output.appendToFile(newOutput[0], newOutput[1]);
    else if (redirection.equals("overwrite"))
      output.overwriteToFile(newOutput[0], newOutput[1]);
    else
      output.print(newOutput[0]);
  }

  /**
   * This method discerns whether the input given is a valid redirection input and handles it
   * accordingly.
   * 
   * @param input The string that JShell retrieves from user that will be parsed
   * @return Void
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void checkRedirectionRequirements(String input) throws InvalidArgumentException,
      InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, ClassNotFoundException,
      InvalidPathException, AlreadyExistsException, FileNotFoundException, IOException {
    try {
      // Split input into command name, and arguments
      String[] userInput = input.split(" ", 2);
      // Special case where all the angular brackets are apart of the string
      if (userInput[0].equals("echo") && input.indexOf(">") < input.lastIndexOf("\"")) {
        String[] echoInput = {userInput[0], input.substring(input.indexOf(" ") + 1)};
        String[] newOutput = {callCommand(echoInput)};
        handleRedirectionOutput(newOutput);
      } else {
        String[] newOutput = parseForRedirection(input);
        if (supportsRedirection(userInput[0]))
          handleRedirectionOutput(newOutput);
      } // Handles the case with successful output, but existing error
    } catch (InvalidPathException e) {
      String errorMessage = e.getMessage();
      if (errorMessage.endsWith("&")) {
        // Takes input from exception error message, redirects it, and throws the error message
        String output = errorMessage.substring(0, errorMessage.lastIndexOf("&\n"));
        String[] newOutput = {output, this.filePath};
        handleRedirectionOutput(newOutput);
        throw new InvalidPathException(errorMessage.substring(errorMessage.lastIndexOf("&\n") + 1,
            errorMessage.lastIndexOf("&")));
      } else
        throw e;
    }
    return;
  }

  /**
   * This method determines whether or not the given command supports redirection
   * 
   * @param command Command name
   * @return boolean True if and only if command supports redirection
   */
  private boolean supportsRedirection(String command) {
    String[] commands = {"ls", "pwd", "cat", "man", "echo", "history", "tree", "search"};
    for (int i = 0; i < commands.length; i++) {
      if (command.equals(commands[i]))
        return true;
    }
    return false;
  }

  /**
   * This method initializes the validCommands HashMap with the appropriate values.
   * 
   * @param None
   * @return Void
   */
  private void initializeCommandMap() {
    validCommands.put("cd", "commands.ChangeDirectory");
    validCommands.put("cat", "commands.Concatenate");
    validCommands.put("cp", "commands.Copy");
    validCommands.put("curl", "commands.Curl");
    validCommands.put("echo", "commands.Echo");
    validCommands.put("exit", "commands.Exit");
    validCommands.put("history", "commands.History");
    validCommands.put("ls", "commands.List");
    validCommands.put("loadJShell", "commands.LoadJShell");
    validCommands.put("mkdir", "commands.MakeDirectory");
    validCommands.put("man", "commands.Manual");
    validCommands.put("mv", "commands.Move");
    validCommands.put("popd", "commands.PopDirectory");
    validCommands.put("pwd", "commands.PrintWorkingDirectory");
    validCommands.put("pushd", "commands.PushDirectory");
    validCommands.put("rm", "commands.Remove");
    validCommands.put("saveJShell", "commands.SaveJShell");
    validCommands.put("search", "commands.Search");
    validCommands.put("tree", "commands.Tree");
  }

  /**
   * This method gets the appropriate object from validCommands and calls its runCommand method.
   * 
   * @param input A list containing the command and given user arguments.
   * @return Void
   * @throws IOException
   * @throws FileNotFoundException
   */
  private String callCommand(String[] input) throws InvalidPathException, InvalidArgumentException,
      AlreadyExistsException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
      ClassNotFoundException, FileNotFoundException, IOException {
    @SuppressWarnings("rawtypes")
    Class[] parameterType = new Class[1];
    parameterType[0] = FileSystem.class;
    Command command = (Command) Class.forName(validCommands.get(input[0]))
        .getDeclaredConstructor(parameterType).newInstance(fileSystem);
    try {
      String output = command.runCommand(input);
      return output;
      // Handles successful output with error with no redirection
    } catch (InvalidPathException e) {
      if ((input[0].equals("ls") | input[0].equals("cat") | input[0].equals("search"))
          && e.getMessage().endsWith("&") && redirection.equals("")) {
        String errorMessage = e.getMessage();
        String appropriateMessage;
        if (errorMessage.indexOf("&\n") == 0)
          appropriateMessage = errorMessage.substring(1, errorMessage.lastIndexOf("&"));
        else
          appropriateMessage = errorMessage.substring(0, errorMessage.indexOf("&\n")) + errorMessage
              .substring(errorMessage.indexOf("&\n") + 1, errorMessage.lastIndexOf("&"));
        throw new InvalidPathException(appropriateMessage);
      }
      throw e;
    }
  }

  /**
   * This method determines whether command is valid based on the keys in validCommands
   * 
   * @param command A string given by the user
   * @return boolean This returns true or false if command is valid/invalid
   * @throws InvalidArgumentException Command is not a valid command
   */
  private boolean isValidCommand(String command) throws InvalidArgumentException {
    if (validCommands.containsKey(command))
      return true;
    throw new InvalidArgumentException("'" + command + "' is not a valid command\n");
  }

  /**
   * This method parses the input that contains redirection into four parts.
   * 
   * @param input The string that JShell retrieves from user that will be parsed
   * @return Void
   * @throws IOException
   * @throws FileNotFoundException
   */
  private String[] parseForRedirection(String input) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, ClassNotFoundException, InvalidPathException, InvalidArgumentException,
      AlreadyExistsException, FileNotFoundException, IOException {
    // Split input into a list of two strings, command and the rest
    String[] userInput = input.split(" ", 2);
    // Captures everything before the angle brackets that signify redirection
    String beforeAngle = userInput[1].substring(0, userInput[1].indexOf(">")).trim();
    // Captures everything after the angle brackets
    String restOfString =
        userInput[1].substring(userInput[1].indexOf(">"), userInput[1].length()).trim();
    // Accounts for angle brackets inside quotation marks
    if (input.indexOf(">") < input.lastIndexOf("\"") && userInput[0].equals("echo")) {
      beforeAngle = userInput[1]
          .substring(0, userInput[1].indexOf(">", userInput[1].lastIndexOf("\""))).trim();
      restOfString =
          userInput[1].substring(userInput[1].indexOf(">", userInput[1].lastIndexOf("\""))).trim();
    }
    // Captures the name of the path and the type of redirection
    if (restOfString.startsWith(">>")) {
      this.redirection = "append";
      this.filePath = restOfString.substring(restOfString.indexOf(">>") + 2).trim();
      restOfString = ">>";
    } else {
      this.redirection = "overwrite";
      this.filePath = restOfString.substring(restOfString.indexOf(">") + 1).trim();
      restOfString = ">";
    }
    // Stores the command name and parameters
    List<String> parsedCommand = storeCommandsAndParameters(userInput[0], beforeAngle);
    // Calls command
    String[] output = {callCommand(parsedCommand.toArray(new String[0])), this.filePath};
    return output;
  }

  /**
   * This method stores the command name and parameters into a List<String>
   * 
   * @param commandName Contains the name of the command
   * @param beforeAngle Contains the input of the parameters before the angular bracket(s)
   * @return List<String> Containing the command name and its parameters
   */
  private List<String> storeCommandsAndParameters(String commandName, String beforeAngle) {
    // Stores command name and parameters
    List<String> fullCommand = new ArrayList<String>();
    // Adds command name first
    fullCommand.add(commandName);
    // Split potential paths and store in list
    if (!beforeAngle.equals("") && !commandName.equals("echo")) {
      String[] splitArguments = beforeAngle.split("\\s+");
      for (int i = 0; i < splitArguments.length; i++)
        fullCommand.add(splitArguments[i]);
    }
    // Else store input into list
    else if (!beforeAngle.equals(""))
      fullCommand.add(beforeAngle);
    return fullCommand;
  }

  /**
   * This method determines whether the arguments for Echo are valid for the case where Echo is to
   * print the STRING to the user.
   * 
   * @param userInput A list containing the command and given user arguments
   * @return Void
   * @throws IOException
   * @throws FileNotFoundException
   */
  private String checkEchoPrintCommand(String[] userInput) throws InvalidArgumentException,
      InvalidPathException, AlreadyExistsException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException,
      ClassNotFoundException, FileNotFoundException, IOException {
    if (userInput[1].startsWith("\"") && userInput[1].endsWith("\"")) {
      return callCommand(userInput);
    }
    throw new InvalidArgumentException("argument must be in double quotes\n");
  }
}
