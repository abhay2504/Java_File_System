package filesystem;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a file system where it organize the directory and file tree
 * structure and hold the current working directory. It also gives some
 * methods that can be used by command classes to retrieve or modify the
 * information stored in this object. This is a singleton object, so there is
 * always only one instance of this class
 *
 * 
 */
public class FileSystem implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * singleton value to keep only one instance of fileSystem is created
   */
  private static FileSystem fileSystemReference = null;
  /**
   * Stores the root directory of the file system
   */
  private Directory rootDirectory;
  /**
   * Stores the current working directory for the file system
   */
  private Directory currentDirectory;
  /**
   * Stores the stacks used for popd and pushd commands
   */
  private StackNode top;
  /**
   * Stores the history of commands used
   */
  private ArrayList<String> historyList;

  /**
   * Private Custom Constructor; it takes in a rootDirectory and set is as
   * the rootDirectory and the currentDirectory for this class
   * 
   * @param rootDirectory this is the directory to be set as root
   */
  private FileSystem(Directory rootDirectory) {
    this.rootDirectory = rootDirectory;
    this.currentDirectory = rootDirectory;
    this.historyList = new ArrayList<String>();
  }

  /**
   * This method will used by external files to construct the fileSystem
   * instance. This method ensures that there is only one instance of
   * fileSystem(Singleton model)
   * 
   * @param rootDirectory rootDirectory of the fileSystem
   * @return If there is already a instance, returns that instance, if not
   * create an instance and return it
   */
  public static FileSystem createFileSystemInstance(Directory rootDirectory) {
    if (fileSystemReference == null) {
      fileSystemReference = new FileSystem(rootDirectory);
    }
    return fileSystemReference;
  }

  /**
   * This method changes the fileSystem that we work on (only can be used
   * with loadJShell)
   * @param fileSystem this is the new FileSystem that we will use
   */
  public static void changeFileSystem(FileSystem fileSystem){
    fileSystemReference = fileSystem;
  }

  /**
   * @return the rootDirectory
   */
  public Directory getRootDirectory() {
    return rootDirectory;
  }

  /**
   * @return the current working directory
   */
  public Directory getCurrentDirectory() {
    return currentDirectory;
  }

  /**
   * this sets the currentDirectory to a new one
   * 
   * @param currentDirectory the new current directory of fileSystem
   */
  public void setCurrentDirectory(Directory currentDirectory) {
    this.currentDirectory = currentDirectory;
  }

  /**
   * @return the top of the StackNode
   */
  public StackNode getTop() {
    return top;
  }

  /**
   * This sets the top of the StackNode
   * 
   * @param top the new top of the StackNode
   */
  public void setTop(StackNode top) {
    this.top = top;
  }

  /**
   * @return the list of commands that were used
   */
  public ArrayList<String> getHistory() {
    return this.historyList;
  }

  /**
   * Sets the historyList to a updated one
   * @param historyList new historyList
   */
  public void setHistory(ArrayList<String> historyList) {
    this.historyList = historyList;
  }

  /**
   * Add a element in the historyList
   * 
   * @param input
   */
  public void populateList(String input) {
    this.historyList.add(input);
  }

  /**
   * Use this command to check if a file or directory exists (absolute or
   * relative path) Returns the  object of File or Directory so use the
   * sample code below to check for the type It returns null if path doesn't
   * exist
   *
   * @param pathName This is the String of path to be checked
   * @return return Object of file or directory if path is found, otherwise
   * return null
   *         <p>
   *         Caution: Absolute Path starts with '/' e.g) /User/username/...
   *         Relative path starts with the directory/file name e.g)
   *         Desktop/CSCB07/...
   *         <p>
   *         [Sample Code] Object child = fileSystem.pathExists
   *         (/Root/dir1/dir2); if(child == null { (Path DNE) }
   *         <p>
   *         else if (child instanceof File){ (Path Exist and it's a File)
   *         File childFile = (File)child; }
   *         <p>
   *         else if (child instanceof Directory){ (Path Exist and it's a
   *         Directory) Directory childDirectory = (Directory)child; }
   */

  public Object pathExists(String pathName) {
    // Absolute path
    if (pathName.charAt(0) == '/') {
      String relativePathString = pathName.substring(1);
      if (relativePathString.length() == 0) {
        return rootDirectory;
      }
      return rootDirectory.pathExistUnder(relativePathString);
    }
    // Relative Path
    return currentDirectory.pathExistUnder(pathName);
  }
}
