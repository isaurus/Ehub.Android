package com.isaac.ehub.ui.home.userprofile;

import com.isaac.ehub.core.Resource;

public class EditUserProfileViewState {

    private final Resource<?> resource;
    private final boolean isAvatarValid;
    private final boolean isNameValid;
    boolean isBirthDateValid;
    private final boolean isCountryValid;

    public EditUserProfileViewState(Resource<?> resource, boolean isAvatarValid, boolean isNameValid, boolean isBirthDateValid, boolean isCountryValid) {
        this.resource = resource;
        this.isAvatarValid = isAvatarValid;
        this.isNameValid = isNameValid;
        this.isBirthDateValid = isBirthDateValid;
        this.isCountryValid = isCountryValid;
    }

    public static EditUserProfileViewState validating(boolean isAvatarValid, boolean isNameValid, boolean isBirthDateValid, boolean isCountryValid){
        return new EditUserProfileViewState(
                Resource.validating(),
                isAvatarValid,
                isNameValid,
                isBirthDateValid,
                isCountryValid);
    }

    public static EditUserProfileViewState loading(){
        return new EditUserProfileViewState(
                Resource.loading(),
                true,
                true,
                true,
                true);
    }

    public static <T> EditUserProfileViewState success(){
        return new EditUserProfileViewState(
                Resource.success(true),
                true,
                true,
                true,
                true);
    }

    public static EditUserProfileViewState error(String message){
        return new EditUserProfileViewState(
                Resource.error(message),
                true,
                true,
                true,
                true);
    }

    public Resource.Status getStatus(){ return resource.getStatus(); }

    public boolean isAvatarValid(){ return isAvatarValid; }

    public boolean isNameValid(){ return isNameValid; }

    public boolean isBirthDateValid(){ return isBirthDateValid; }

    public boolean isCountryValid(){ return isCountryValid; }
}
