package filesystem;

import java.io.Serializable;


/**
 * This class creates instances of nodes for Stack
 * 
 * 
 */
@SuppressWarnings("serial")
public class StackNode implements Serializable {
  /**
   * An Object of type Directory, stores the directory of the node
   */
  private Directory Dir;
  /**
   * An Object of type StackNode, stores the object of next node in the stack
   */
  private StackNode next;

  /**
   * This constructor creates an object of StackNode,initializing it's instance variables.
   * 
   * @param Dir Object of type Directory, the Directory that is to be pushed
   * @param next Object of type StackNode, the next node in stack
   * @return Nothing
   */
  public StackNode(Directory Dir, StackNode next) {
    this.Dir = Dir;
    this.next = next;
  }

  /**
   * This method returns the node after this node in the stack
   * 
   * @return An object of StackNode
   */
  public StackNode getNext() {
    return this.next;
  }

  /**
   * This method returns the directory of the node
   * 
   * @return An object of Directory
   */
  public Directory getDir() {
    return this.Dir;
  }
}
