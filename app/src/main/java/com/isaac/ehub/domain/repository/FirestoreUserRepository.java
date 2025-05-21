package com.isaac.ehub.domain.repository;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;

public interface FirestoreUserRepository {

    LiveData<Resource<Boolean>> createUserIfNotExists(UserModel userModel);
    LiveData<Resource<Boolean>> editUser(UserModel userModel);

    LiveData<Resource<UserModel>> getCurrentUser();
}
