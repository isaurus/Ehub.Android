package com.isaac.ehub.domain.usecase.auth;

import com.isaac.ehub.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

public class CheckAuthenticatedUserUseCase {

    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject
    public CheckAuthenticatedUserUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public boolean execute(){
        return firebaseAuthRepository.isUserAuthenticated();
    }
}
