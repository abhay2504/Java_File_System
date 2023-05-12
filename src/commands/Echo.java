package commands;


import exceptions.InvalidArgumentException;
import filesystem.FileSystem;

/**
 * This class implements the echo command, it traverser userInput and calls appropriate echo method,
 * if there is one '>' replace, if two '>' then add else calls print if no '>' and of length 2.
 * Throws exceptions if the input does not follow the proper format
 * 
 * 
 */
public class Echo implements Command {
  @SuppressWarnings("unused")
  private FileSystem fileSystem;

  /**
   * Initializes fileSystem with the instance of the current FileSystem being worked on.
   */
  public Echo(FileSystem fSystem) {
    fileSystem = fSystem;
  }

  /**
   * This method checks if String is valid and returns the String
   * 
   * @param userInput A list containing the command and given user arguments
   * @return Nothing
   * @throws InvalidArgumentException Not enough arguments
   * @throws InvalidArgumentException STRING must be wrapped in double quotes
   * @throws InvalidArgumentException Invalid Arguments, missing fName/fData
   */
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    String fData;
    if (userInput.length == 1) {
      throw new InvalidArgumentException("Not enough arguments");
    }
    fData = isValidGetFData(userInput);
    return fData;
  }


  /**
   * This method returns the file data that is present in userInput so that either error can be
   * called if file data is not present or the file data can be appended/ overwritten in file or
   * printed at shell depending on userInput
   * 
   * @param userInput A list containing the command and given user arguments
   * @return a string containing file data in userInput
   * @throws InvalidArgumentException File data Argument is missing
   * @throws InvalidArgumentException STRING must be wrapped in double quotes
   * @throws InvalidArgumentException Double quotes inside String not allowed
   */
  public String isValidGetFData(String userInput[]) throws InvalidArgumentException {
    userInput[1] = userInput[1].trim();
    if (userInput[1].equals("")) {
      throw new InvalidArgumentException("File data Argument is missing");
    }
    if (!(userInput[1].startsWith("\"") && userInput[1].endsWith("\""))) {
      throw new InvalidArgumentException("STRING must be wrapped in double quotes");
    }
    String fData =
        userInput[1].substring(userInput[1].indexOf("\"") + 1, userInput[1].lastIndexOf("\""));
    if (fData.contains("\"")) {
      throw new InvalidArgumentException("Double quotes inside String not allowed");
    }
    return fData;
  }


}
