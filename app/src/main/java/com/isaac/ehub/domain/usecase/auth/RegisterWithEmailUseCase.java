package com.isaac.ehub.domain.usecase.auth;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

public class RegisterWithEmailUseCase {

    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject
    public RegisterWithEmailUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public LiveData<Resource<Boolean>> execute(String email, String password){
        return firebaseAuthRepository.registerWithEmail(email, password);
    }
}
