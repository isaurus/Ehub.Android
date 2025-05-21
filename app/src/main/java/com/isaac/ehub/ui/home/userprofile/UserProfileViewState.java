package com.isaac.ehub.ui.home.userprofile;

import com.isaac.ehub.core.Resource;

public class UserProfileViewState {

    private final Resource<?> resource;
    private final boolean isAvatarValid;
    private final boolean isNameValid;
    private final boolean isBirthDateValid;
    private final boolean isCountryValid;


    public UserProfileViewState(Resource<?> resource, boolean isAvatarValid, boolean isNameValid, boolean isBirthDateValid, boolean isCountryValid) {
        this.resource = resource;
        this.isAvatarValid = isAvatarValid;
        this.isNameValid = isNameValid;
        this.isBirthDateValid = isBirthDateValid;
        this.isCountryValid = isCountryValid;
    }


    public static UserProfileViewState validating(boolean isAvatarValid, boolean isNameValid, boolean isBirthDateValid, boolean isCountryValid){
        return new UserProfileViewState(
                Resource.validating(),
                isAvatarValid,
                isNameValid,
                isBirthDateValid,
                isCountryValid);
    }

    public static UserProfileViewState loading(){
        return new UserProfileViewState(
                Resource.loading(),
                true,
                true,
                true,
                true);
    }

    public static <T> UserProfileViewState success(){
        return new UserProfileViewState(
                Resource.success(true),
                true,
                true,
                true,
                true);
    }

    public static UserProfileViewState error(String message){
        return new UserProfileViewState(
                Resource.error(message),
                true,
                true,
                true,
                true);
    }

    public Resource.Status getStatus(){ return resource.getStatus(); }

    public <T> T getData() {
        return (T) resource.getData();
    }

    public boolean isAvatarValid(){ return isAvatarValid; }

    public boolean isNameValid(){ return isNameValid; }

    public boolean isBirthDateValid(){ return isBirthDateValid; }

    public boolean isCountryValid(){ return isCountryValid; }
}
