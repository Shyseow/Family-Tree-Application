package FamilyTreeApp;
import java.io.Serializable;

/**
 *File Name: Tree.java
 *Purpose: Act as implementation of Person Class
 *
 *@author Seow Hui Yin
 *@version 1.0
 *@since Submission due date: 22/11/2018 
 */

public class Tree implements Serializable{
    //To ensure same class is loaded during deserialization
    private static final long SerialVersionUID = 1;
    private Person tree;

    /**
     * Default constructor initializes with null
     */
    public Tree() {
        this.tree = null;        
    }
  
    /**
     * Get Person object
     * @return value of root person for Person object
     */
    public Person getRootPerson(){
        return this.tree;
    }
    
     /**
     * Set the value of Person object  
     * @param newPerson Person object
     */
    public void setRootPerson(Person newPerson){
        this.tree = newPerson;
    }
    
    /**
     * To test if the tree is null
     */
    public boolean checkRootPerson(){
        return this.tree !=null;
    }
}
