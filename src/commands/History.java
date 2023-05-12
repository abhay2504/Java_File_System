package commands;


import java.util.ArrayList;
import exceptions.InvalidArgumentException;
import filesystem.FileSystem;

/**
 * This class prints out the recent commands entered by the user.
 *
 * 
 */
public class History implements Command {

  @SuppressWarnings("unused")
  /**
   * Stores the instance of the current fileSystem being worked on
   */
  private FileSystem fileSystem;

  /**
   * Stores all the commands user has inputed in order
   */
  private ArrayList<String> historyList;

  /**
   * This method is the constructor for History
   * 
   * @param fileSystem
   * @return Void
   */
  public History(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
    this.historyList = fileSystem.getHistory();
  }

  /**
   * This method determines the appropriate printHistory method to call based on the contents of
   * userInput.
   * 
   * @param userInput A list containing the command and given user arguments
   * @return Nothing
   * @throws InvalidArgumentException If there are too many arguments
   * @throws InvalidArgumentException If argument is not an integer
   * @throws InvalidArgumentException If Argument is non-negative
   */
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    // Print everything in historyList since userInput only contains 'history'
    if (userInput.length == 1) {
      return printHistory();
    }
    // Check for errors i.e, too many arguments, argument is not an integer or
    // is non-negative
    if (userInput.length > 2)
      throw new InvalidArgumentException("history: too many arguments\n");
    try {
      Integer.valueOf(userInput[1]);
    } catch (NumberFormatException e) {
      throw new InvalidArgumentException(
          "history: " + userInput[1] + ": numeric argument " + "required\n");
    }
    if (Integer.valueOf(userInput[1]) < 0)
      throw new InvalidArgumentException(
          "history: " + userInput[1] + ": argument must be non-negative\n");
    // Return if user wants to see last 0 commands
    if (Integer.valueOf(userInput[1]) == 0)
      return "";
    // Print everything in historyList if user specifies number that's larger
    // than the number of contents in historyList
    if (Integer.valueOf(userInput[1]) >= historyList.size()) {
      return printHistory();
    }
    // Print the specified number of last commands in historyList
    return printHistory(Integer.valueOf(userInput[1]));
  }

  /**
   * This method is used to print the contents of everything in historyList.
   * 
   * @param None
   * @return Nothing
   */
  private String printHistory() {
    String output = "";
    // Go through historyList and print all of its commands
    for (int i = 0; i < historyList.size(); i++) {
      int columnNumber = i + 1;
      output = output + columnNumber + "." + historyList.get(i) + "\n";
    }
    return output;
  }

  /**
   * This method is used to print the last i items in historyList.
   * 
   * @param i, This is the only parameter of printHistory
   * @return Nothing
   */
  private String printHistory(int i) {
    String output = "";
    // startValue stores the index of the command in historyList we start
    // printing from
    int startValue = historyList.size() - i;
    // Go through historyList starting from startValue and print the i commands
    for (int j = startValue; j < historyList.size(); j++) {
      int columnNumber = j + 1;
      output = output + columnNumber + "." + historyList.get(j) + "\n";
    }
    return output;
  }
}
