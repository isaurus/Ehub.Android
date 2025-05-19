package com.isaac.ehub.domain.usecase.auth;

import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

public class GetAuthenticatedUserUseCase {

    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject
    public GetAuthenticatedUserUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public UserModel execute(){
        return firebaseAuthRepository.getAuthenticatedUser();
    }
}
