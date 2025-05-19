package com.isaac.ehub.domain.model;

import ehub.backend.domain.enums.Country;
import java.util.Date;

public class UserModel {
    private String firebaseUid;
    private String profilePicURL;
    private String name;
    private String email;
    private Date birthDate;
    private Country country;

    public UserModel(String firebaseUid, String profilePicURL, String name, Date birthDate, Country country) {
        this.firebaseUid = firebaseUid;
        this.profilePicURL = profilePicURL;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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