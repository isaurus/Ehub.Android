package com.isaac.ehub.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.isaac.ehub.core.Resource;
import com.isaac.ehub.core.mapper.UserMapper;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirebaseAuthRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirebaseAuthRepositoryImpl implements FirebaseAuthRepository {

    private final FirebaseAuth firebaseAuth;

    @Inject
    public FirebaseAuthRepositoryImpl(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public LiveData<Resource<Boolean>> registerWithEmail(String email, String password) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> result.setValue(Resource.success(true)))
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Resource<Boolean>> loginWithEmail(String email, String password) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> result.setValue(Resource.success(true)))
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Resource<Boolean>> loginWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> result.setValue(Resource.success(true)))
                .addOnFailureListener(e -> result.setValue(Resource.error(e.getMessage())));

        return result;
    }

    /**
     * Implementación para obtener el usuario autenticado mapeado a UserModel.
     *
     * @return El FirebaseUser mapeado a UserModel.
     */
    @Override
    public UserModel getAuthenticatedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        return UserMapper.fromFirebaseUser(firebaseUser);
    }

    /**
     * Implementación para comprobar si el usuario ya está autenticado.
     *
     * @return true si está autenticado, false en caso contrario.
     */
    @Override
    public boolean isUserAuthenticated(){
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void signOut(){
        firebaseAuth.signOut();
    }
}
