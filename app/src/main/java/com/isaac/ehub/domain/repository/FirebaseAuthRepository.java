package com.isaac.ehub.domain.repository;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;

public interface FirebaseAuthRepository {

    void getIdToken(IdTokenCallback callback);


    interface IdTokenCallback {
        void onTokenReceived(String token);
        void onError(String error);
    }

    LiveData<Resource<Boolean>> registerWithEmail(String email, String password);
    LiveData<Resource<Boolean>> loginWithEmail(String email, String password);
    LiveData<Resource<Boolean>> loginWithGoogle(String idToken);
}
