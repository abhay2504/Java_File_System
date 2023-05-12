package commands;


import java.util.HashMap;
import exceptions.InvalidArgumentException;
import filesystem.FileSystem;

/**
 * This class implements the man command, initializing a command map that maps every command to its
 * respective documentation, which is then printed to the shell on user command
 * 
 * 
 */
public class Manual implements Command {
  @SuppressWarnings("unused")
  private FileSystem fileSystem;

  /**
   * Initializes fileSystem with the instance of the current FileSystem being worked on.
   */
  public Manual(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * This method initializes the command map and prints the documentation of the requested
   * command(s). Throws exception if the command is not found in the command map
   * 
   * @param userInput A list containing the command and possibly arguments given by the user
   * @return Nothing
   */
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    /**
     * Initializing the man method
     */
    commandMap();
    String documentation = null;
    /**
     * Check for only one command
     */
    if (userInput.length > 2) {
      throw new InvalidArgumentException("Too many arguments.");
    }
    if (userInput.length == 1) {
      throw new InvalidArgumentException("Not enough arguments.");
    }
    /**
     * Retrieve argument
     */
    String getArgument = userInput[1].trim();
    /**
     * Get the documentation matched to the argument (command)
     */
    String commandInfo = manmap.get(getArgument);
    /**
     * Check that stack is non-empty
     */
    if (commandInfo != null) {
      documentation = (getArgument + " - " + commandInfo + "\n");
    } else {
      throw new InvalidArgumentException("Invalid argument. No such command " + "found.");
    }
    return documentation;
  }

  /**
   * Creates a HashMap that maps each command to its respective documentation
   */
  private HashMap<String, String> manmap = new HashMap<String, String>();

  /**
   * This method maps every command to its respective documentation using a HashMap
   * 
   * @return Nothing
   */
  private void commandMap() {
    // man command
    manmap.put("man", "Prints detailed documentation for requested " + "command."
        + "\n\n\tArgument Requirements: ‘man [CMD]'");
    // echo command
    manmap.put("echo", "Prints STRING." + "\n\n\tArgument Requirements: 'echo [STRING]'");
    // cat command
    manmap.put("cat",
        "Prints the contents of a specific concatenated " + "file(s) "
            + "\n      in the JavaShell separated by three line breaks for every " + "file "
            + "\n      printed. The command will stop at the first invalid file."
            + "\n\n\tArgument Requirements: 'cat [FILE1] [FILE2]...'");
    // history command
    manmap.put("history",
        "Prints out the recent commands the user has " + "used one "
            + "\n\t  per line. The number specifies the number of commands that " + "will be "
            + "\n\t  printed with the highest number associated with the most " + "recent "
            + "\n\t  command." + "\n\n\tArgument Requirements: history[NUMBER]'");
    // popd command
    manmap.put("popd", "Remove the top entry from the directory stack " + "and change "
        + "\n       the current working directory to that directory.");
    // pushd command
    manmap.put("pushd",
        "Stores the current working directory to the " + "directory "
            + "\n\tstack and changes the new current working directory to the " + "requested "
            + "\n\tdirectory." + "\n\n\tArgument Requirements: 'pushd [DIR]'");
    // pwd command
    manmap.put("pwd", "Print the current absolute working directory to " + "the shell.");
    // ls command
    manmap.put("ls", "If no path(s) are given, print the current " + "files/directories "
        + "\n     of the current working directory separated by a newline " + "after every "
        + "\n   " + "  file/directory. Otherwise, for each path p, the order listed:"
        + "\n\n\t- If p refers to a file, print p to the shell."
        + "\n\t- If p refers to a directory, print the directory " + "followed by a "
        + "\n\tcolon, then the files/directories in that directory " + "followed by a "
        + "\n\tnewline." + "\n\t- If p does not exist, print an error message saying "
        + "that p is " + "\n\t\tnot a valid argument.\n\n"
        + "     If -R is present as an argument with a directory, \n"
        + "     recursively list all sub-directories of that directory. \n"
        + "     If not, recursively list all sub-directories of the current " + "working directory."
        + "\n\n\tArgument Requirements: ‘ls " + "[-R (optional)] [PATH (optional)... ]");
    // cd command
    manmap.put("cd", "Change the current working directory to the " + "requested "
        + "\n     directory." + "\n\n\tArgument Requirements: ‘cd [DIR (optional)]’");
    // mkdir command
    manmap.put("mkdir",
        "Creates requested directories that are " + "relative of a "
            + "full path or an absolute path. \n\tIf creating DIR1 results in "
            + "any kind of error does not create "
            + "\n\tDIR2. If creating DIR1 was successful and creating DIR2 " + "results in any "
            + "\n\tkind of error, create DIR1 and display an error message " + "specific to DIR2."
            + "\n\n\tArgument Requirements: ‘mkdir [DIR1] [DIR2]...’");
    // exit command
    manmap.put("exit", "Quit the shell.");
    // remove command
    manmap.put("rm",
        "Removes the requested directory from the file system. "
            + "\n     Also removes all the sub-directories and files of that " + "directory."
            + "\n\n\tArgument Requirements: 'rm [DIR]'");
    // move command
    manmap.put("mv",
        "Moves item from OLDPATH to NEWPATH. Both paths may "
            + "either be relative paths \n     to the current directory or " + "absolute "
            + "paths. \n     If NEWPATH is a directory, move the item into the " + "directory."
            + "\n\n\t Arguments Requirements: 'mv [OLDPATH] [NEWPATH]'");
    // curl command
    manmap.put("curl", "Retrieves the file at the requested URL adds it to "
        + "the current working directory." + "\n\n\tArguments Requirements: 'curl [validURL]'");
    // saveJShell command
    manmap.put("saveJShell",
        "Writes the entire state of the current program " + "to the file FILENAME \n             "
            + "(Note that FILENAME can be a path). If FILENAME "
            + "already exists, \n             calling saveJShell will completely "
            + "overwrite the existing file with the current program state."
            + "\n\n\tArgument Requirements: 'saveJShell [FILENAME]'");
    // loadJShell command
    manmap.put("loadJShell",
        "Loads the contents of FILENAME and "
            + "reinitializes everything that was previously saved into FILENAME."
            + "\n\n\tArgument Requirements: 'loadJShell [FILENAME]'"
            + "\n\n\t(Note that this command will be disabled if any other "
            + "command is used after reinitializing FILENAME)");
    // search command
    manmap.put("search",
        "Returns all the paths that contain a specific "
            + "directory of file that have the matching expression requested the "
            + "by the user. \n         The paths can be absolute or relative to "
            + "the current directory."
            + "\n\n\tArgument Requirements: 'search [PATH1] [PATH2] ... [-type] "
            + "[f|d] [-name] [EXPRESSION]'");
    // tree command
    manmap.put("tree", "Displays the entire file system as a tree.");
    // copy command
    manmap.put("copy",
        "Moves item from OLDPATH to NEWPATH. Both paths may "
            + "either be relative paths to the current directory or absolute "
            + "paths. \n       Does not remove OLDPATH after command is " + "executed. "
            + "If NEWPATH is a directory, move the item into the directory.\n   "
            + "    If OLDPATH is a directory, recursively copy the contents to " + "NEWPATH."
            + "\n\n\tArgument Requirements: 'cp [OLDPATH] [NEWPATH]'");
  }
}
