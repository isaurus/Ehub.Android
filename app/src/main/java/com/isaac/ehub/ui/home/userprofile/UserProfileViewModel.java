package com.isaac.ehub.ui.home.userprofile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.usecase.home.user.EditUserProfileUseCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {

    private static final int MAX_NAME_LENGTH = 20;

    private final EditUserProfileUseCase editUserProfileUseCase;

    public final MutableLiveData<UserProfileViewState> userProfileViewState = new MutableLiveData<>();
    public final MutableLiveData<EditUserProfileViewState> editUserProfileViewState = new MutableLiveData<>();

    @Inject
    public UserProfileViewModel(EditUserProfileUseCase editUserProfileUseCase) {
        this.editUserProfileUseCase = editUserProfileUseCase;
    }

    public LiveData<UserProfileViewState> getUserProfileViewState() { return userProfileViewState; }
    public LiveData<EditUserProfileViewState> getEditProfileViewState() { return editUserProfileViewState; }

    public void editUserProfile(UserModel userModel){
        editUserProfileUseCase.execute(userModel).observeForever(resource -> {
            switch (resource.getStatus()){
                case SUCCESS:
                    editUserProfileViewState.setValue(EditUserProfileViewState.success());
                    break;
                case ERROR:
                    editUserProfileViewState.setValue(EditUserProfileViewState.error(resource.getMessage()));
                    break;
                case LOADING:
                    editUserProfileViewState.setValue(EditUserProfileViewState.loading());
                    break;
            }
        });
    }

    public void validateEditUserForm(String avatar, String name, String birthDateStr, String country){
        boolean isAvatarValid = isValidAvatar(avatar);
        boolean isNameValid = isValidName(name);
        boolean isBirthDateValid = isValidBirthDate(birthDateStr);
        boolean isCountryValid = isValidCountry(country);

        editUserProfileViewState.setValue(EditUserProfileViewState.validating(isAvatarValid, isNameValid, isBirthDateValid, isCountryValid));

        if (isAvatarValid && isNameValid && isBirthDateValid && isCountryValid){
            editUserProfileViewState.setValue(EditUserProfileViewState.loading());
            editUserProfile(new UserModel(avatar, name, formatBirthDate(birthDateStr), country));
        }
    }

    private boolean isValidAvatar(String avatar){
        return true;
    }

    private boolean isValidName(String name){
        return !name.isEmpty() && name.length() <= MAX_NAME_LENGTH;
    }

    private boolean isValidBirthDate(String birthDate){
        return !birthDate.isEmpty();        // IMPLEMENTAR VALIDACIÓN, ESTO ES PARA PRUEBAS
    }

    private boolean isValidCountry(String country){
        return !country.isEmpty();        // IMPLEMENTAR VALIDACIÓN, ESTO ES PARA PRUEBAS
    }

    private Date formatBirthDate(String birthDateStr){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date birthDate = null;
        try{
            birthDate = formatter.parse(birthDateStr);
        } catch (ParseException e) {
            // Maneja error de formato inválido
        }

        return birthDate;
    }
}
