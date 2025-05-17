package com.isaac.ehub.domain.model;

import ehub.backend.domain.enums.Country;
import java.util.Date;

public class UserModel {
    private String firebaseUid;
    private String profilePicURL;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private Country country;

    public UserModel(String firebaseUid, String profilePicURL, String firstName, String lastName, Date birthDate, Country country) {
        this.firebaseUid = firebaseUid;
        this.profilePicURL = profilePicURL;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.country = country;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}