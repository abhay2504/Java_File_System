package exceptions;


@SuppressWarnings("serial")
/**
 * This class creates a new exception with a specific error message based on the checked exceptions.
 * 
 * 
 */
public class InvalidPathException extends Exception {
  /**
   * This method calls the constructor of Exception class to create a new exception with
   * errorMessage.
   * 
   * @param errorMessage The string containing the specified error message.
   */
  public InvalidPathException(String message) {
    super(message);
  }

}
