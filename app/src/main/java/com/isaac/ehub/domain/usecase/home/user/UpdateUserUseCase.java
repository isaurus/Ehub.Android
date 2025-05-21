package com.isaac.ehub.domain.usecase.home.user;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import javax.inject.Inject;

public class UpdateUserUseCase {

    private final FirestoreUserRepository firestoreUserRepository;

    @Inject
    public UpdateUserUseCase(FirestoreUserRepository firestoreUserRepository) {
        this.firestoreUserRepository = firestoreUserRepository;
    }

    public LiveData<Resource<Boolean>> execute(UserModel userModel){
        return firestoreUserRepository.updateUser(userModel);
    }
}
