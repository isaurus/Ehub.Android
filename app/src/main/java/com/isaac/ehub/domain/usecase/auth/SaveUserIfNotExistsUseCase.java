package com.isaac.ehub.domain.usecase.auth;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import javax.inject.Inject;

public class SaveUserIfNotExistsUseCase {

    private final FirestoreUserRepository firestoreUserRepository;

    @Inject
    public SaveUserIfNotExistsUseCase(FirestoreUserRepository firestoreUserRepository) {
        this.firestoreUserRepository = firestoreUserRepository;
    }

    public LiveData<Resource<Boolean>> execute(UserModel userModel){
        return firestoreUserRepository.createUserIfNotExists(userModel);
    }
}
