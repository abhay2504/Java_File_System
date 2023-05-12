package commands;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.FileSystem;
import filesystem.StackNode;

/**
 * This class saves the session of the file system that is being worked on to a file for later use.
 * 
 * 
 *
 */
public class SaveJShell implements Command {

  /**
   * 
   */
  @SuppressWarnings("unused")
  private static final long serialVersionUID = 1L;
  /**
   * Stores the instance of the fileSystem being worked on
   */
  private FileSystem fileSystem;
  /**
   * Stores all of the files and directories in the file system
   */
  private ArrayList<Object> filesAndDirectories = new ArrayList<Object>();
  /**
   * Stores all of the directories in the stack
   */
  private ArrayList<StackNode> stackList = new ArrayList<StackNode>();

  /**
   * Custom constructor that initializes the fileSystem with the one currently being worked on
   * 
   * @throws InvalidArgumentException
   */
  public SaveJShell(FileSystem fileSystem) throws InvalidArgumentException {
    this.fileSystem = fileSystem;
  }

  /**
   * This method creates a new file that contains the serialized files, directories, and stack list
   * of the working file system and saves it in the savedfiles directory.
   * 
   * @param userInput A list containing the command and given user arguments
   * @return String An empty string
   * @throws IOException When there is an error with the input/output file
   * @throws InvalidArgumentException When there are too many arguments
   */
  public String runCommand(String[] userInput) throws IOException, InvalidArgumentException {
    if (userInput.length != 2)
      throw new InvalidArgumentException("saveJShell: requires one argument that is a file name");
    if (!userInput[1].contains("/") && userInput[1].matches("[a-zA-Z0-9_]+")) {
      // Stores the file we'll be writing too in the user home directory
      // String userDesktop = System.getProperty("user.home");
      FileOutputStream file = new FileOutputStream(new File("src/savedfiles/", userInput[1]));
      // Writes Java objects to file
      ObjectOutputStream writer = new ObjectOutputStream(file);
      // Write the current file system instance to the file
      writer.writeObject(this.fileSystem);
      // Write the history list to the file
      writer.writeObject(fileSystem.getHistory());
      // Populate the filesAndDirectories list and the stackList
      createFilesAndDirectoriesList(fileSystem.getRootDirectory());
      getStackList(fileSystem.getTop());
      // Write the filesAndDirectories and stackList to the file
      writer.writeObject(filesAndDirectories);
      writer.writeObject(stackList);
    } else
      throw new InvalidArgumentException("saveJShell: file name can only contain characters from "
          + "A-Z, a-z, or 1-9, slashes are not allowed either");
    return "";
  }

  /**
   * This method gets all of the files and directories in the file system and stores them in a list
   * 
   * @param dir The directory that we'll be traversing through
   * @return Void
   */
  private void createFilesAndDirectoriesList(Directory dir) {
    // Stores all of the children directories of dir
    ArrayList<Directory> childDir = dir.getChildDirectories();
    // Stores all of the children files of dir
    ArrayList<filesystem.File> childFiles = dir.getChildFiles();
    // Goes through both lists and adds them to the filesAndDirectories list and recursively looks
    // through the sub directory of directory
    for (Directory directory : childDir) {
      filesAndDirectories.add(directory);
      createFilesAndDirectoriesList(directory);
    }
    for (filesystem.File file : childFiles) {
      filesAndDirectories.add(file);
    }
  }

  /**
   * This method gets the stack list that PushDirectory and PopDirectory utilize.
   * 
   * @param node The top node of the stack
   * @return Void
   */
  private void getStackList(StackNode node) {
    // Goes through each node in the stack and adds them to the list
    if (node == null)
      return;
    stackList.add(node);
    while (node.getNext() != null) {
      node = node.getNext();

      stackList.add(node);
    }
  }

}
