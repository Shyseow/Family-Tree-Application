package FamilyTreeApp;
import java.io.Serializable;

/**
 *File Name: Address.java
 *Purpose: Serializable Address class for storing records as objects, 
 *         Also perform error checking for allowed input value
 *  
 **Assumption: All field are not null, and input type is allowed value  
 *@author Seow Hui Yin
 *@version 1.0
 *@since Submission due date: 22/11/2018 
 */

public class Address implements Serializable{
    
    private String streetNumber;
    private String streetName;
    private String suburb;
    private String postCode;

    /**
     * constructor initializes address with parameter streetNumber, streetName, 
     * Suburb and postCode
     * @param streetNumber
     * @param streetName
     * @param Suburb
     * @param postCode
     */
    public Address(String streetNumber, String streetName, String Suburb, String postCode) {
        this.setStreetNumber(streetNumber);
        this.setStreetName(streetName);
        this.setSuburb(Suburb);
        this.setPostCode(postCode);
    }

    /**
     * Get new address street number
     * @return street number with omitted string with no leading and trailing spaces
     */
    public String getStreetNumber() {
        return streetNumber;
    }
    
    /**
     * Get new address street name
     * @return street name with omitted string with no leading and trailing spaces
     */
    public String getStreetName() {
        return streetName;
    }
    
    /**
     * Get new address suburb
     * @return suburb with omitted string with no leading and trailing spaces
     */
    public String getSuburb() {
        return suburb;
    }
    
        /**
     * Get new address postcode 
     * @return postcode with omitted string with no leading and trailing spaces
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Sets street number, and check at least 1 digit in street number
     * @param streetNumber streetNumber of address
     */
    public final void setStreetNumber(String streetNumber) {
        if (streetNumber.trim().matches("^[\\d]+$") && streetNumber.trim().matches(".*\\d.*")) {
            this.streetNumber = streetNumber.trim();
        }else{
            throw new IllegalArgumentException("Invalid Street Number.");
        }        
    }

    /**
     * Sets streetName
     * @param streetName street name of a person
     */
    public final void setStreetName(String streetName) {
        this.streetName = streetName; 
    }
   
    /**
     * Set suburb and check allowed values for suburb
     * @param suburb suburb of address
     */
    public final void setSuburb(String suburb) {
        
        if (suburb.trim().matches("^[a-zA-z' ]+$")){
            this.suburb = suburb.trim();
        }else{
            throw new IllegalArgumentException("Invalid Suburb Name");
        }
    }

    /**
     * Set postcode and check postcode is at least 6 numbers
     * @param postCode postCode of address
     */
    public final void setPostCode(String postCode) {
        if (postCode.trim().matches("\\d{6}")){
            this.postCode = postCode.trim();
        }else{
            throw new IllegalArgumentException("Post code must be a positive 6 digit");
        } 
    }
    

    @Override 
    /**
     * 
     * @return string representation of the address object
     */
    public String toString() {
        return streetNumber + " " + streetName + ", " + suburb + ", " + postCode;
    }
}
