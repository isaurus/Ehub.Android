package com.isaac.ehub.domain.usecase.home;

import com.isaac.ehub.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;

public class SignOutUseCase {

    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject

    public SignOutUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    public void execute(){
        firebaseAuthRepository.signOut();
    }
}
