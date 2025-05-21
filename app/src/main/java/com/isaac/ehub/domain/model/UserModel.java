package com.isaac.ehub.domain.model;

import ehub.backend.domain.enums.Country;
import java.util.Date;

public class UserModel {
    private String firebaseUid;
    private String profilePicURL;
    private String name;
    private String email;
    private Date birthDate;
    private String country;

    /**
     * Constructor para deserialización automática con Firestore.
     */
    public UserModel(){}

    /**
     * Constructor para persistir el usuario en Firestore cuando se registra o se logea por primera
     * vez en la aplicación.
     *
     * @param firebaseUid El UID del usuario recién creado.
     * @param email El correo que ha utilizado el usuario para registrarse.
     */
    public UserModel(String firebaseUid, String email){
        this.firebaseUid = firebaseUid;
        this.email = email;
    }

    /**
     * Constructor para completar/editar los detalles del usuario desde su perfil.
     *
     * @param profilePicURL
     * @param name
     * @param birthDate
     * @param country
     */
    public UserModel(String profilePicURL, String name, Date birthDate, String country) {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}