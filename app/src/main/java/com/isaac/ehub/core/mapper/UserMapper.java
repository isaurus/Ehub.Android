package com.isaac.ehub.core.mapper;

import com.google.firebase.auth.FirebaseUser;
import com.isaac.ehub.domain.model.UserModel;

public class UserMapper {

    public static UserModel fromFirebaseUser(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;

        return new UserModel(
                firebaseUser.getUid(),
                firebaseUser.getEmail()
        );
    }
}
