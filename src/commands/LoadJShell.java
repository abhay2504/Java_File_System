package commands;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import exceptions.InvalidArgumentException;
import filesystem.Directory;
import filesystem.FileSystem;
import filesystem.StackNode;

/**
 * This class loads a previous session of the file system from a saved file.
 * 
 * 
 *
 */
public class LoadJShell implements Command {
  /**
   * Stores the instance of the file system that'll be worked on
   */
  private FileSystem fileSystem;

  public LoadJShell(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * This method opens the specified file and loads the previous session of the file system
   * 
   * @param userInput A list containing the command and given user arguments
   * @return String An empty string throws
   */
  @SuppressWarnings("unchecked")
  public String runCommand(String[] userInput)
      throws InvalidArgumentException, IOException, ClassNotFoundException {
    if (userInput.length != 2)
      throw new InvalidArgumentException("loadJShell: requires one argument that is a file");
    // Obtains input bytes from specified file
    try {
      // String userHomeDirectory = System.getProperty("user.home");
      FileInputStream file = new FileInputStream(new File("src/savedfiles", userInput[1]));
      // Stores the objects that'll be read from file
      ObjectInputStream oi = new ObjectInputStream(file);
      // Stores the file system instance saved in file
      FileSystem savedFileSystem = (FileSystem) oi.readObject();
      // Initializes the current file system instance with the saved file system instance
      FileSystem.changeFileSystem(savedFileSystem);
      this.fileSystem = FileSystem.createFileSystemInstance(savedFileSystem.getRootDirectory());
      // Sets the current directory to that of the saved file system's current directory
      fileSystem.setCurrentDirectory(savedFileSystem.getCurrentDirectory());
      // Sets the history list to that of the saved file system's history list
      fileSystem.setHistory((ArrayList<String>) oi.readObject());
      // Stores all the files and directories of the saved file system
      ArrayList<Object> list = (ArrayList<Object>) oi.readObject();
      // Recreates these files and directories in the file system
      if (list != null)
        createFilesAndDirectories(list);
      // Stores the stack list of the saved file system
      ArrayList<StackNode> stack = (ArrayList<StackNode>) oi.readObject();
      // Recreates the stack list in the file system
      if (stack != null)
        createStackList(stack);
    } catch (FileNotFoundException e) {
      throw new InvalidArgumentException("loadJShell: file not found");
    } catch (ClassNotFoundException | IOException e) {
      throw new InvalidArgumentException("loadJShell: file not compatible");
    }
    return "";
  }

  /**
   * This method recreates all the stack list of the saved file system
   * 
   * @param stack The stack list from the saved file
   * @return Void
   */
  public void createStackList(ArrayList<StackNode> stack) {
    if (stack.size() > 0) {
      StackNode top = stack.get(0);
      fileSystem.setTop(top);
    }
  }

  /**
   * This method recreates all the files and directories of the saved file system
   * 
   * @param list The list containing all the files and directories of the saved file system
   * @return Void
   */
  @SuppressWarnings("unused")
  public void createFilesAndDirectories(ArrayList<Object> list) {

    for (int i = 0; i < list.size(); i++) {
      if (list.get(i) instanceof filesystem.File) {
        filesystem.File newFile = (filesystem.File) list.get(i);

      } else if (list.get(i) instanceof Directory) {
        Directory newDirectory = (Directory) list.get(i);

      }
    }
  }


}
