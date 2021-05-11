package com.ts.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "person")
@Validated
public class Person implements Serializable {
    private static final long serialVersionUID = 1323272533746093028L;

    private Long id;
    private String firstName;
    private String lastName;
    @NotBlank(message = "An email is required")
    private String email;
    private String zipCode;
    private String city;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "firstname")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastname")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "zipcode")
    public String getZipCode() {
        return zipCode;
    }

    @JsonInclude
    @Transient
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(email, person.email) && Objects.equals(zipCode, person.zipCode) && Objects.equals(city, person.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, zipCode, city);
    }
}
