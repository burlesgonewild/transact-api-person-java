package com.ts.person;

import java.util.Objects;

public class City {
    private String country;
    private String state;
    private String city;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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
        City city1 = (City) o;
        return Objects.equals(country, city1.country) && Objects.equals(state, city1.state) && Objects.equals(city, city1.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, state, city);
    }
}
