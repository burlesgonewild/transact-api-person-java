package com.ts.person;

import java.util.Comparator;

/**
 * Person class for processing person data like simple transferring of data.
 * Class used to prepare data for response from an endpoint or prepare data for store to keep the actual code for the persistence
 * separated away from any processing code
 */
public class PersonProcessObject {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String zipCode;
    private String city;

    public PersonProcessObject(){
        // For serialization
    }

    public PersonProcessObject(Person person){
        id = person.getId();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        email = person.getEmail();
        zipCode = person.getZipCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static class SortByLastName implements Comparator<PersonProcessObject> {
        public int compare(PersonProcessObject a, PersonProcessObject b)
        {
            return a.lastName.compareTo(b.lastName);
        }
    }

}
