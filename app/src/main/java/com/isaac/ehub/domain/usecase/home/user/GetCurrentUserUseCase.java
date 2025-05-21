package com.isaac.ehub.domain.usecase.home.user;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import javax.inject.Inject;

public class GetCurrentUserUseCase {

    private final FirestoreUserRepository firestoreUserRepository;

    @Inject
    public GetCurrentUserUseCase(FirestoreUserRepository firestoreUserRepository) {
        this.firestoreUserRepository = firestoreUserRepository;
    }

    public LiveData<Resource<UserModel>> execute(){
        return firestoreUserRepository.getCurrentUser();
    }
}
