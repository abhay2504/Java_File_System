package commands;


import exceptions.InvalidArgumentException;
import exceptions.InvalidPathException;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileSystem;

/**
 * This class implements the concatenate command, it traverser userInput and throws exceptions if
 * the file does not exist/ the userInput has arguments missing
 * 
 * 
 */
public class Concatenate implements Command {
  /**
   * Stores the current instance of the filesystem being worked on
   */
  private FileSystem fileSystem;

  /**
   * Initializes fileSystem with the instance of the current FileSystem being worked on.
   * 
   * @param fSystem Is stored in this.fileSystem.
   * @return Nothing
   */
  public Concatenate(FileSystem fSystem) {
    fileSystem = fSystem;
  }

  /**
   * This method prints contents of file/files until the files exist, throws exception when the file
   * does not exists
   * 
   * @param userInput A list containing the command and given user arguments
   * @return Nothing
   * @throws InvalidArgumentException If no files in userInput
   * @throws InvalidPathException If the file does not exist
   */
  public String runCommand(String[] userInput) throws InvalidPathException, InvalidArgumentException {
    if (userInput.length == 1) {
      throw new InvalidArgumentException("Missing Arguments, no file names");
    }
    String pathName, contents = "";
    for (int i = 1; i < userInput.length; i++) {
      pathName = userInput[i];
      Object obj = fileSystem.pathExists(pathName);
      if (obj != null && obj instanceof File) {
    	  if (i!=1) {
    	        contents += "\n\n\n"; 
    	      }
        contents += ((File) fileSystem.pathExists(pathName)).getFileData();
      } else if (obj instanceof Directory) {
    	  if(contents.equals("")) {
    		  throw new InvalidPathException("cat: "+
    	  userInput[1].substring((userInput[1].lastIndexOf("/") + 1))+" is a directory");
    	  }
        throw new InvalidPathException(contents+"&\n"+"cat: "
            +userInput[1].substring((userInput[1].lastIndexOf("/") + 1))+" is a directory"+"&");
      } else {
    	  if(contents.equals("")) {
    		  throw new InvalidPathException("File:"+pathName+ " doesnot exist");
    	  }
        throw new InvalidPathException(contents+"&\n"+"File:"+pathName+ " doesnot exist"+"&");
      }
    }
    return contents;
  }
}
