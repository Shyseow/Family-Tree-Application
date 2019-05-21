package FamilyTreeApp;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *File Name: Person.java
 *Purpose: Serializable Person class for storing records as objects, 
 *         to construct a single family member and their immediate 
 *         specified relative within the class
 *         Also perform error checking for allowed input value
 * 
 *Assumption: All field are not null, and input type is allowed value  
 *@author Seow Hui Yin
 *@version 1.0
 *@since Submission due date: 22/11/2018 
 */

public class Person implements Serializable{

    private String firstName;
    private String lastName;
    private String maidenName;
    private Gender gender;
    private Address address;
    private String textDesc;
    //The pattern defined by the regex may to match 
    private final String expression = "^[\\p{L} .'-]+$";  
    private Person mother;
    private Person father;
    private Person spouse;
    private ArrayList<Person> children;


    /**
     * constructor initializes person with parameter firstName,lastName,gender,address,textDesc
     * @param firstName
     * @param lastName
     * @param gender
     * @param address
     * @param textDesc
     */
    public Person(String firstName, String lastName, Gender gender, Address address, String textDesc) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.maidenName = "";
        this.setGender(gender);
        this.setAddress(address);
        this.setTextDesc(textDesc);
        
        this.mother = null;
        this.father = null;
        this.spouse = null;
        this.children = new ArrayList<>();
    }

    
    /**
     * Declare constants of enum type, to check if person has any of these relation
     */
    public enum Relation {
        FATHER,
        MOTHER,
        CHILDREN,
        SPOUSE,
        MAIDENNAME,
        PARENTS;
    }

    /**
     * Declare constants of enum type, to add immediate relative to a person
     */
    public enum ImmRelative {
        FATHER,
        MOTHER,
        CHILD,
        SPOUSE;
    } 

    /**
     * Declare constants of enum type, to allow only 2 type of gender
     */
    public enum Gender {
        MALE,
        FEMALE;
    }
    
     /**
     * Get first name for person
     * @return first name with omitted string with no leading and trailing spaces
     */
    public String getFirstName() {
        return firstName;
    }
    
     /**
     * Get last name for person
     * @return last name with omitted string with no leading and trailing spaces
     */
    public String getLastName() {
        return lastName;
    }
    
     /**
     * Get maiden name for person
     * @return maiden name with omitted string with no leading and trailing spaces
     */
    public String getMaidenName() {
        return maidenName;
    }
    
     /**
     * Get gender for person
     * @return gender
     */
    public Gender getGender() {
        return gender;
    }
    
     /**
     * Get address for person
     * @return address from Address object
     */
    public Address getAddress() {
        return address;
    }
    
    /**
     * Get life description for person
     * @return textDesc
     */
    public String getTextDesc() {
        return textDesc;
    }
    
    /**
     * Get mother info for person
     * @return mother
     */
    public Person getMother() {
        return mother;
    }
    
    /**
     * Get father info for person
     * @return father
     */
    public Person getFather() {
        return father;
    }
    
    /**
     * Get spouse info for person
     * @return spouse
     */
    public Person getSpouse() {
        return spouse;
    }
      
    /**
     * Get children info for person
     * @return children
     */
    public ArrayList<Person> getChildren() {
        return children;
    }

    /**
     * Sets first name, and check if match regex
     * @param firstName firstName of a person
     */
    public final void setFirstName(String firstName) {
        if (firstName.trim().matches(expression)) {
            this.firstName = firstName.trim();
        }else{
            throw new IllegalArgumentException("Re-enter First Name");
        }
        
    }


    /**
     * Sets last name, and check if match regex
     * @param lastName lastName of a person
     */
    public final void setLastName(String lastName) {
        if (lastName.trim().matches(expression)) {
            this.lastName = lastName.trim();
        }else{
            throw new IllegalArgumentException("Re-enter SurName");
        }
    }

    /**
     * Sets maiden name, and check if match regex
     * to ensure only female can have maiden name
     * @param maidenName maidenName of a person
     */
    public void setMaidenName(String maidenName) {
        if (maidenName.trim().matches(expression)) {
            if (this.gender == Gender.FEMALE){
                this.maidenName = maidenName.trim();
            }else{
                throw new IllegalArgumentException("Maiden name is only for FEMALE");
            }
            
        }else if (maidenName.isEmpty()){
            this.maidenName = "";
        }else{
            throw new IllegalArgumentException("Re-enter Maiden Name");
        }
    }

    /**
     * Sets gender
     * @param gender gender of a person
     */
    public final void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Sets address
     * @param address address from Address object
     */
    public final void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Sets life description, and check if it is empty
     * @param textDesc life description of a person
     */
    public final void setTextDesc(String textDesc) {
        if (!textDesc.trim().isEmpty()) {
            this.textDesc = textDesc;
        }else{
            throw new IllegalArgumentException("Please fill in life description, cannot be empty");
        }
        this.textDesc = textDesc;
        
    }
    
    /**
     * add a child to the family tree, then added the relationship 
     * @param child child to add
     */
    public void addChild(Person child) {
        //father
        if (this.gender == Gender.MALE) {
            if (!child.has(Relation.FATHER)) {
                child.setFather(this);
            }
            if (this.has(Relation.SPOUSE)) {
                if (!child.has(Relation.MOTHER)) {
                    child.setMother(this.getSpouse());
                }
            }
        //mother
        }else if (this.gender == Gender.FEMALE){
            //if the child doesnt have a mother set it
            if (!child.has(Relation.MOTHER)) {
                child.setMother(this);
            }
            if (this.has(Relation.SPOUSE)) {
                if (!child.has(Relation.FATHER)) {
                    child.setFather(this.getSpouse());
                }
            }
        }
        if(!this.getChildren().contains(child)){
            this.getChildren().add(child);
        }
        
    }

    /**
     * Number of children of a person 
     * @return number of children 
     */
    
    public int NoChildren(){
        return this.getChildren().size();
    }


    /**
     * Sets mother of a person, check a person can only have one mother
     * check mother only can be female
     * @param mother mother of a person
     */
    public void setMother(Person mother) {
        if (!this.has(Relation.MOTHER)) {
            if (mother.getGender() == Gender.FEMALE) {
                if (!mother.getChildren().contains(this)){
                    mother.getChildren().add(this);
                }
                this.mother = mother;

                
            }else{
                throw new IllegalArgumentException("Only FEMALE for mother");
            }
           
        }else{
            throw new IllegalArgumentException("You already have a mother");
        }
        
    }



    /**
     * Sets father of a person, check a person can only have one mother
     * check father only can be male
     * @param father father of a person
     */
    public void setFather(Person father) {
        if (!this.has(Relation.FATHER)) {
            if (father.getGender() == Gender.MALE) {
                if (!father.getChildren().contains(this)){
                    father.getChildren().add(this);
                }
                this.father = father;
                
                
            }else{
                throw new IllegalArgumentException("Only MALE for father");
            }
            
        }else{
            throw new IllegalArgumentException("You already have a father");
        }
        
    }

    /**
     * Sets spouse of a person, check a person can only have one spouse
     * check spouse can only be opposite gender
     * @param spouse spouse of a person
     */
    public void setSpouse(Person spouse) {
        if (!this.has(Relation.SPOUSE)) {
            if(spouse.getGender() != this.getGender()){
                spouse.setChildren(this.getChildren());
                this.spouse = spouse;
                if (!this.getSpouse().has(Relation.SPOUSE)) {
                    spouse.setSpouse(this);
                }

            }else{
                throw new IllegalArgumentException("Only opposite gender is allowed!");
            }
        }else{
            throw new IllegalArgumentException("You already have a partner...");
        }
        
    }


    /**
     * Sets children of a person
     * @param children children of a person
     */
    public void setChildren(ArrayList<Person> children) {
        this.children = children;
    }
    
    /**
     * Check if the person has relation
     * @param type check relation
     * @return true if condition met 
     */
    public boolean has(Person.Relation type){
        switch(type){
            case FATHER:
                return this.getFather() != null;
            case CHILDREN:
                return !this.getChildren().isEmpty();
            case MOTHER:
                return this.getMother() != null;
            case SPOUSE:
                return this.getSpouse() != null;
            case MAIDENNAME:
                return !this.getMaidenName().isEmpty();
            case PARENTS:
                return this.has(Relation.FATHER) || this.has(Relation.MOTHER);
        }
        return false;
    }
    
    /**
     * Adds immediate relative
     * @param type immediate relative of a person
     * @param familyMember family member of a person
     */
    public void addRelative(Person.ImmRelative type, Person familyMember){
        switch(type){
            case FATHER:
                this.setFather(familyMember);
                return;
            case CHILD:
                this.addChild(familyMember);
                return;
            case MOTHER:
                this.setMother(familyMember);
                return;
            case SPOUSE:
                this.setSpouse(familyMember);
                return;
        }
    }
    

    @Override
    /**
     * @return a String containing the person's information
     */
    public String toString() {
        String s = null;
        if (this.gender == Gender.MALE){
            s = "♂ ";
        }else if (this.gender == Gender.FEMALE){
            s = "♀ ";
        }
        s += this.getFirstName() + " " + this.getLastName(); 
        if (this.has(Relation.MAIDENNAME)){ 
            s += " (" + this.getMaidenName() + ")";
        }
        return s;
    }
}
