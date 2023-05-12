package commands;

import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This class is responsible for displaying the tree structure of the given filesystem
 * 
 * 
 */
public class Tree implements Command {

  /**
   * This is the file system we are interested in for displaying tree
   */
  FileSystem fileSystem;

  /**
   * This is the custom constructor for Tree class
   * 
   * @param fileSystem the fileSystem that we are interested in
   */
  public Tree(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * This method displays the tree structure. It takes no argument(userInput .length==1)
   * 
   * @param userInput A list containing the command and given user arguments.
   * @throws InvalidArgumentException on too many arguments
   * @return String formatted tree of given file system
   */
  @Override
  public String runCommand(String[] userInput) throws InvalidArgumentException {
    if (userInput.length > 1) {
      throw new InvalidArgumentException("tree: too many arguments");
    }
    return getTreeStructure(fileSystem.getRootDirectory(), 0);
  }

  /**
   * Return string formatted tree structure. It utilize recursion techinques to print out all
   * directories and files in the file system
   * 
   * @param dirOrFile this the directory or file to be printed in this recursion call
   * @param numTab this is the number of depth the directory or file is in e.g.) if /dir1/dir2 then
   *        dir1 is depth=1 and dir2 is depth=2
   * @return string formatted tree structure
   */
  private String getTreeStructure(Object dirOrFile, int numTab) {
    String formattedTabs = "";
    for (int i = 0; i < numTab; i++) {
      formattedTabs += "\t";
    }
    if (dirOrFile instanceof File) {
      return formattedTabs + ((File) dirOrFile).getFileName();
    } else if (dirOrFile instanceof Directory) {
      Directory directory = (Directory) dirOrFile;
      String formattedString = "";
      for (Directory childDir : directory.getChildDirectories()) {
        formattedString += "\n" + getTreeStructure(childDir, numTab + 1);
      }
      for (File childFile : directory.getChildFiles()) {
        formattedString += "\n" + getTreeStructure(childFile, numTab + 1);
      }
      if (directory.equals(fileSystem.getRootDirectory())) {
        return formattedTabs + "/" + formattedString;
      }
      return formattedTabs + directory.getDirectoryName() + formattedString;
    }
    return "";
  }
}
