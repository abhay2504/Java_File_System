package filesystem;

import java.io.Serializable;
// **********************************************************
// Assignment2:
// Student1:Arya Sharma
// UTORID user_name:shar1497
// UT Student #:1005692591
// Author:Arya Sharma
//
// Student2: Yuto Omachi
// UTORID user_name: omachiyu
// UT Student #: 1006005163
// Author: Yuto Omachi
//
// Student3:Jameson Joseph
// UTORID user_name: josep236
// UT Student #: 1006430845
// Author: Jameson Joseph
//
// Student4: Jia Rong (Jerry) Dang
// UTORID user_name: dangjia
// UT Student #: 1005838685
// Author: Jerry Dang
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
import exceptions.InvalidArgumentException;

/**
 * This class creates instances of files
 * 
 * @author Arya Sharma
 */
@SuppressWarnings("serial")
public class File implements Serializable{
  /**
   * Stores file name
   */
  private String fName;
  /**
   * Stores file data
   */
  private String fData;

  /**
   * Initializes fName with the fileName passed as parameter and adds the instance of this file in
   * the list of child files of the parent directory
   * 
   * @param fileName type String, this is the name of file
   * @param parentDirectory an instance of Directory, used to add the file instance to parent
   *        directory
   * @return Nothing
   */
  public File(String fileName, Directory parentDirectory) throws InvalidArgumentException {
    if (fileName.matches("[a-zA-Z0-9_]+")) {
      this.fName = fileName;
      parentDirectory.addChildFile(this);
    } else {
      throw new InvalidArgumentException(
          "Some charcaters in file name cannot be used in naming of the file");
    }
  }

  /**
   * This method determines return the file name
   * 
   * @return file name as String
   */
  public String getFileName() {
    return fName;
  }

  /**
   * This method sets the name of the file
   * 
   * @param the name of the file
   * @return Nothing
   */
  public void setFileName(String fName) {
    this.fName = fName;
  }

  /**
   * This method determines return the file data
   * 
   * @return file data as String
   */
  public String getFileData() {
    return this.fData;
  }

  /**
   * This method stores data in the file
   * 
   * @param the data to be stored in file
   * @return Nothing
   */
  public void storeFileData(String fData) {
    this.fData = fData;
  }

}
