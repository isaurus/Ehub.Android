package com.isaac.ehub.domain.usecase.home.user;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import javax.inject.Inject;

/**
 * Use case para crear un usuario. Se utiliza la primera vez que el usuario se logea con Google o se
 * registra manualmente.
 */
public class CreateUserUseCase {

    private final FirestoreUserRepository firestoreUserRepository;

    @Inject
    public CreateUserUseCase(FirestoreUserRepository firestoreUserRepository) {
        this.firestoreUserRepository = firestoreUserRepository;
    }

    public LiveData<Resource<Boolean>> execute(UserModel userModel){
        return firestoreUserRepository.createUser(userModel);
    }
}
