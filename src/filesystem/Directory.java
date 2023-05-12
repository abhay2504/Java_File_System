package filesystem;


import exceptions.InvalidArgumentException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the directory object in the file systems. It can hold
 * name of the directory, the parent directory if applicable and files and
 * directories that is placed under the directory This class is also
 * responsible for some operations regarding the directory object
 *
 * 
 */
public class Directory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /**
   * Represents the name of the directory
   */
  private String directoryName;
  /**
   * Stores the parent directory of the directory Set to null if parent
   * doesn't exist (root directory)
   */
  private Directory parentDirectory;
  /**
   * Stores the list of directories that are contents of this directory
   * (Directories that reside directly under this directory)
   */
  private ArrayList<Directory> childDirectories = new ArrayList<Directory>();
  /**
   * Stores the list of files that are contents of this directory (Files that
   * reside directly under this directory)
   */
  private ArrayList<File> childFiles = new ArrayList<File>();

  /**
   * Custom Constructor for the instance. It takes the name of the directory
   * and the parent directory (It adds the directory to the childDirectories
   * of parent directory)
   * 
   * @param directoryName
   * @param parentDirectory
   */
  public Directory(String directoryName, Directory parentDirectory)
      throws InvalidArgumentException {
    if (!validName(directoryName))
      throw new InvalidArgumentException("invalid characters for the "
          + "directory name");
    this.directoryName = directoryName;
    if (parentDirectory != null) {
      parentDirectory.addChildDirectory(this);
    }
    this.parentDirectory = parentDirectory;
  }

  /**
   * This method returns the directoryName of self
   * 
   * @return directory name
   */
  public String getDirectoryName() {
    return directoryName;
  }

  /**
   * This method returns the parentDirectory of self
   * @return parentDirectory
   */
  public Directory getParentDirectory() {
    return parentDirectory;
  }

  /**
   * This method returns the childDirectories of self
   * 
   * @return childDirectories
   */
  public ArrayList<Directory> getChildDirectories() {
    return childDirectories;
  }

  /**
   * This method sets the childDrectories
   * @param childDirectories new list of directories to be set
   */
  public void setChildDirectories(ArrayList<Directory> childDirectories) {
    this.childDirectories = childDirectories;
  }

  /**
   * This method returns the childFiles of self
   * 
   * @return childFiles
   */
  public ArrayList<File> getChildFiles() {
    return childFiles;
  }

  /**
   * This method check if a input contains a invalid characters for name of
   * files and directories
   * 
   * @param input This is the potentialFile/Directory name
   * @return return false if input contains invalidChars and return true
   * otherwise
   */
  private boolean validName(String input) {
    String invalidChars = "/. !@#$%^&*(){}~|<>?";
    for (int i = 0; i < invalidChars.length(); i++) {
      if (input.contains(invalidChars.substring(i, i + 1))) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method adds a directory to childDirectories
   * 
   * @param directory This is the child directory to be added
   * @return return true if successfully added, otherwise return false
   */
  public boolean addChildDirectory(Directory directory) {
    for (Directory childDir : childDirectories) {
      if (childDir.getDirectoryName().equals(directory.getDirectoryName())) {
        return false;
      }
    }
    for (File childFile : childFiles) {
      if (childFile.getFileName().equals(directory.getDirectoryName())) {
        return false;
      }
    }
    childDirectories.add(directory);
    return true;
  }

  /**
   * This method adds a file to childFiles
   * 
   * @param file This is the child file to be added
   * @return return true if successfully added, otherwise return false
   */
  public boolean addChildFile(File file) {
    for (Directory childDir : childDirectories) {
      if (childDir.getDirectoryName().equals(file.getFileName())) {
        return false;
      }
    }
    for (File childFile : childFiles) {
      if (childFile.getFileName().equals(file.getFileName())) {
        return false;
      }
    }
    childFiles.add(file);
    return true;
  }

  /**
   * This method returns the absolute path of the directory
   * 
   * @return return string formatted absolute path of the directory (this)
   */
  public String getAbsolutePath() {
    if (parentDirectory == null) {
      return "/";
    }
    String absolutePath = "/" + directoryName;
    Directory parent = parentDirectory;
    while (parent.parentDirectory != null) {
      absolutePath = "/" + parent.directoryName + absolutePath;
      parent = parent.parentDirectory;
    }
    return absolutePath;
  }


  /**
   * Caution: This method is not meant to be used by other than FileSystem
   * and remove
   * Class Please use
   * FileSystem.pathExists() method to check if a give n path is valid
   * 
   * This method checks if a path exists under Directory
   * 
   * @param pathString This is the string of path to be checked if it reside
   *                   under this directory
   * @return return Object of file or directory if path is found, otherwise
   *  return null
   */
  public Object pathExistUnder(String pathString) {
    String[] paths = pathString.split("/");
    // Create copy of self object
    Directory currDir = this;
    // Loop through each components of the path
    mainloop: for (int index = 0; index < paths.length; index++) {
      // Check if it uses either "." or ".."
      if (paths[index].equals("."))
        continue mainloop;
      else if (paths[index].equals("..")) {
        currDir = handleDoubleDot(currDir);
        if (currDir == null)
          return null;
        continue mainloop;
      }
      // Check if it's a child directory (if so go to next iteration and
      // move currDir to Child)
      Directory foundDir = searchForDirectory(currDir, paths[index]);
      if (foundDir != null) {
        if (index == paths.length - 1)
          return foundDir;
        currDir = foundDir;
        pathString = pathString.substring(pathString.indexOf("/")+1);
        continue mainloop;
      }
      // Check if it's a child file (only if it's the last index)
      else if (index == paths.length - 1){
        return searchForFile(currDir, paths[index]);
      }
      else {
        return null;
      }
    }
    return currDir;
  }


  /**
   * This method handles part of pathExitUnder operation when it has ..
   * notation. This method will move currDir to the parent of currDir
   * 
   * @param currDir This is the directory that we are checking on
   * @return return parent directory of currDir if it exists, if not return null
   */
  private Directory handleDoubleDot(Directory currDir) {
    if (currDir.parentDirectory == null) {
      return null;
    }
    return currDir.parentDirectory;
  }


  /**
   * This method handles part of pathExitUnder operation This method will
   *  check if directory with directoryName exists under childDirecories of
   *  currDir
   * 
   * @param currDir This is the directory that we are checking on
   * @return return the corresponding child directory, if not return null
   */
  private Directory searchForDirectory(Directory currDir, String directoryName) {
    for (int j = 0; j < currDir.getChildDirectories().size(); j++) {
      Directory childDir = currDir.getChildDirectories().get(j);
      if (directoryName.equals(childDir.getDirectoryName())) {
        return childDir;
      }
    }
    return null;
  }

  /**
   * This method handles part of pathExitUnder operation This method will
   * check if file with fileName exists under childFiles of currDir
   * 
   * @param currDir This is the directory that we are checking on
   * @return return the corresponding child file, if not return null
   */
  private File searchForFile(Directory currDir, String fileName) {
    for (int j = 0; j < currDir.getChildFiles().size(); j++) {
      File childFile = currDir.getChildFiles().get(j);
      if (fileName.equals(childFile.getFileName())) {
        return childFile;
      }
    }
    return null;
  }
}
