package commands;



import java.util.ArrayList;
import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;
/**
* This class implements the search command that takes in pathnames and then 
* returns the paths (absolute or relative) in which the matching file/directory 
* is found.
* 
* 
*/
public class Search implements Command {
  private ArrayList<Directory> Directories = new ArrayList<Directory>();
  private FileSystem fileSystem;
  /**
   * Initializes fileSystem with the instance of the 
   * current FileSystem being worked on.
   */
  public Search(FileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }
  
  /**
   * This helper function adds sub-directories into the general array list and 
   * checks if the directory is already present. If it is, it does not add it.
   * 
   * @param dir A directory
   * @return Nothing
   */
  public void addDirectories(Directory dir) {
	  ArrayList<Directory> childDirectories = dir.getChildDirectories();
	  for (Directory childDir : childDirectories) { 
		  if(!Directories.contains(childDir)) {
			  Directories.add(childDir);
			  addDirectories(childDir);
		  }
	  }
  }
  
  /**
   * This helper function loops through every path in the user argument and 
   * attempts to find a matching directory name as the given expression and adds
   * that absolute path to a string of paths.
   *  
   * @param userInput A list containing the command and possibly arguments given
   * @return A string of paths (absolute) separated by a newline 
   */
  public String searchDirectories(String[] userInput) throws 
  InvalidPathException {
	  String paths = "";
	  for(int i=1; i<= userInput.length-5; i++) {
		  if(fileSystem.pathExists(userInput[i]) != null && 
				  fileSystem.pathExists(userInput[i]) instanceof Directory) {  
			Directory dir = (Directory) fileSystem.pathExists(userInput[i]);
			if(!Directories.contains(dir)) {
				Directories.add(dir);
				addDirectories(dir);
			}
		  String expression = userInput[userInput.length-1];
		  String dirName = expression.substring(1, expression.length()-1);
		  for (Directory childDir : Directories) { 
			  if(childDir.getDirectoryName().equals(dirName)) {
				  paths += childDir.getAbsolutePath().substring(1) + "\n";
			  }
		  }
		 } else {
		   if(paths == "") {
		     throw new InvalidPathException("\nNo such directory.");
	       } else {
	         throw new InvalidPathException(paths + "\nNo such directory.");
	         }
		   }
		  Directories.clear();
	  }
	  return paths;
  }
  
  /**
   * This helper function loops through every path in the user argument and 
   * attempts to find a matching file name as the given expression and adds
   * that absolute path to a string of paths.
   *  
   * @param userInput A list containing the command and possibly arguments given
   * @return A string of paths (absolute) separated by a newline 
   */
  public String searchFiles(String[] userInput) throws InvalidPathException {
	  String paths = "";
	  for(int i=1; i<= userInput.length-5; i++) {
		  if(fileSystem.pathExists(userInput[i]) != null && 
				  fileSystem.pathExists(userInput[i]) instanceof Directory) {  
			Directory dir = (Directory) fileSystem.pathExists(userInput[i]);
			if(!Directories.contains(dir)) {
				Directories.add(dir);
				addDirectories(dir);
			}
			 String expression = userInput[userInput.length-1];
			 String fName = expression.substring(1, expression.length()-1);
			 for (Directory childDir : Directories) { 
			   for(File file : childDir.getChildFiles()) {
			     if(file.getFileName().equals(fName)) {
			       paths+="path"+i+": "+childDir.getAbsolutePath().
			           substring(1)+"/"+fName+"\n";
				 }
			   }
			 }
		  } else {
		    if(paths == "") {
		      throw new InvalidPathException("\nNo such directory.");
		    } else {
		      throw new InvalidPathException(paths + "\nNo such directory.");
	        }
		  }
		  Directories.clear();
	  }
	  return paths;
  }
  /**
   * This main function of the Search class checks the validity of the user 
   * argument and returns the paths in which the requested expression is matched
   * by either directories or files in those specific paths (absolute or 
   * relative)
   * 
   * @param userInput A list containing the command and possibly arguments given
   * @return A string of paths (absolute) separated by a newline
   */
  public String runCommand (String[] userInput) throws InvalidArgumentException,
  InvalidPathException {
    String noMatches = "Error - expression not found in given paths.";
    String result = "";
	int check = checkParameterValidity(userInput);
	if (check == 0) {
	    throw new 
	    InvalidArgumentException("Invalid argument(s). Check format.");
	  } else if (check == 1) {
	    result = searchDirectories(userInput);
	  } else {
	   	result = searchFiles(userInput);
    }
	if (result == "") {
	  return noMatches;
	}
	return result;
  }
  
  /**
   * This method ensures that the user argument is valid by checking the 
   * parameter positions
   * 
   * @param userInput A list containing the command and possibly arguments given
   * @return An integer that indicates whether the input is valid, and it if is,
   * to search for files, or directories
   */
  private int checkParameterValidity(String[] userInput) {
	String expression = userInput[userInput.length-1];
    if (userInput.length < 6) {
      return 0;
    }
    if (!userInput[userInput.length-2].equals("-name")) {
      return 0;
    }
    if (!userInput[userInput.length-4].equals("-type")) {
      return 0;
    }
    if (userInput[userInput.length-3].equals("d")) {
    	if (expression.indexOf("\"") == 0 && expression.lastIndexOf("\"") 
    	    == expression.length() - 1) {
    		return 1;
    	}
    	else {
    		return 0;
    	}
    } else if (userInput[userInput.length-3].equals("f")) {
    	if (expression.indexOf("\"") == 0 && expression.lastIndexOf("\"") 
    	    == expression.length() - 1) {
    		return 2;
    	}
    	else {
    		return 0;
    	}
    } else {
      return 0;
    }
  }
}
