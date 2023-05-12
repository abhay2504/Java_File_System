package commands;


import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This class deals with all of the output to the StandardOutput.
 * 
 *
 */
public class OutputHandler {
  /**
   * Stores the instance of the fileSystem being worked on
   */
  private FileSystem fileSystem;

  /**
   * A custom constructor of OutputHandler that gets the current instance of the file system
   * 
   * @param fileSystem
   * @throws InvalidArgumentException
   */
  public OutputHandler() throws InvalidArgumentException {
    this.fileSystem = FileSystem.createFileSystemInstance(new Directory("root", null));
  }

  /**
   * Prints output to the console
   * 
   * @param output Output to be printed
   * @return Void
   */
  public void print(String output) {
    System.out.println(output);
  }

  /**
   * This method appends the output to the given fileName, otherwise it creates a new file with the
   * given output.
   * 
   * @param output Output to be appended to file
   * @param fileName File that will be appended/created
   * @throws InvalidArgumentException When fileName is invalid
   */
  public void appendToFile(String output, String fileName) throws InvalidArgumentException {
    // Checks to see if fileName is a valid file name
    checkValidFileName(fileName);
    // Appends output to fileName
    if (fileSystem.pathExists(fileName) instanceof File) {
      File file = (File) fileSystem.pathExists(fileName);
      output = file.getFileData() + "\n" + output;
      file.storeFileData(output);
      return;
    } // Else creates a new file
    else
      createNewFile(output, fileName);
  }

  /**
   * This method overwrites fileName with output, otherwise it creates a new file with the given
   * output.
   * 
   * @param output Output to be overwritten to file
   * @param fileName File that will be overwritten/created
   * @throws InvalidArgumentException When fileName is invalid
   */
  public void overwriteToFile(String output, String fileName) throws InvalidArgumentException {
    // Checks to see if fileName is a valid file name
    checkValidFileName(fileName);
    // Overwrites output to fileName
    if (fileSystem.pathExists(fileName) instanceof File) {
      File file = (File) fileSystem.pathExists(fileName);
      file.storeFileData(output);
      return;
    } // Else it creates a new file
    else
      createNewFile(output, fileName);
  }

  /**
   * This method creates a new file with the file name fileName and output
   * 
   * @param output The output to be stored into fileName
   * @param fileName The name of the newly created file
   * @throws InvalidArgumentException When the file name is invalid
   */
  public void createNewFile(String output, String fileName) throws InvalidArgumentException {
    // Checks to see if the file name is valid
    checkValidFileName(fileName);
    // Stores the parent directory of the file
    Directory workingDirectory = null;
    // Deals with absolute and relative paths containing slashes
    if (fileName.contains("/")) {
      if (fileName.substring(0, fileName.lastIndexOf("/")).equals("")) {
        workingDirectory = fileSystem.getRootDirectory();
      } else if (fileSystem
          .pathExists(fileName.substring(0, fileName.lastIndexOf("/"))) instanceof Directory)
        workingDirectory =
            (Directory) fileSystem.pathExists(fileName.substring(0, fileName.lastIndexOf("/")));
      if (workingDirectory instanceof Directory) {
        File file = new File(fileName.substring(fileName.lastIndexOf("/") + 1), workingDirectory);
        file.storeFileData(output);
      } else
        throw new InvalidArgumentException("invalid path provided");
    } // File will be stored directly in the current working directory
    else {

      File file = new File(fileName.substring(fileName.lastIndexOf("/") + 1),
          fileSystem.getCurrentDirectory());
      file.storeFileData(output);
    }
  }

  /**
   * This method determines whether fileName is a valid file name
   * 
   * @param fileName Name of file to be checked
   * @throws InvalidArgumentException When the file name is invalid
   */
  public void checkValidFileName(String fileName) throws InvalidArgumentException {
    if (fileName.equals("")) {
      throw new InvalidArgumentException("No path provided");
    }
    if (fileSystem.pathExists(fileName) instanceof Directory) {
      throw new InvalidArgumentException(fileName + ": is a directory");
    }
    if (fileName.endsWith("/")) {
      throw new InvalidArgumentException("Invalid Path");
    }
    if (fileName.contains(">")) {
      throw new InvalidArgumentException("Extra Angular Brackets");
    }
  }

}
