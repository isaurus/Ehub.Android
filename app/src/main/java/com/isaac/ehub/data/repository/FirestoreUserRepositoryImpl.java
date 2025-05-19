package com.isaac.ehub.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreUserRepositoryImpl implements FirestoreUserRepository {

    private final FirebaseFirestore firestore;

    @Inject
    public FirestoreUserRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }


    @Override
    public LiveData<Resource<Boolean>> createUserIfNotExists(UserModel userModel) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        DocumentReference docRef = firestore.collection("users").document(userModel.getFirebaseUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    docRef.set(userModel)
                            .addOnSuccessListener(unused -> result.setValue(Resource.success(true)))
                            .addOnFailureListener(e -> result.setValue(Resource.error("Error al guardar el usuario: " + e.getMessage())));
                } else {
                    result.setValue(Resource.success(true)); // Ya existe, así que es correcto
                }
            } else {
                result.setValue(Resource.error("Error al comprobar el usuario: " + task.getException().getMessage()));
            }
        });

        return result;
    }
}
