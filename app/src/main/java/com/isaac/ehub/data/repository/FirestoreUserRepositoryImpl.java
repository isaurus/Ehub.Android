package com.isaac.ehub.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.isaac.ehub.core.Resource;
import com.isaac.ehub.core.mapper.UserMapper;
import com.isaac.ehub.domain.model.UserModel;
import com.isaac.ehub.domain.repository.FirebaseAuthRepository;
import com.isaac.ehub.domain.repository.FirestoreUserRepository;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FirestoreUserRepositoryImpl implements FirestoreUserRepository {

    private final FirebaseFirestore firestore;
    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject
    public FirestoreUserRepositoryImpl(FirebaseFirestore firestore, FirebaseAuthRepository firebaseAuthRepository) {
        this.firestore = firestore;
        this.firebaseAuthRepository = firebaseAuthRepository;
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

    @Override
    public LiveData<Resource<Boolean>> editUser(UserModel userModel) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        Map<String, Object> map = UserMapper.toMap(userModel);

        firestore.collection("users")
                .document(firebaseAuthRepository.getAuthenticatedUser().getFirebaseUid())
                .update(map)
                .addOnSuccessListener(unused -> result.setValue(Resource.success(true)))
                .addOnFailureListener(e -> result.setValue(Resource.error("Error al editar el usuario " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Resource<UserModel>> getCurrentUser(){
        MutableLiveData<Resource<UserModel>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        String userUid = firebaseAuthRepository.getAuthenticatedUser().getFirebaseUid();

        firestore.collection("users")
                .document(userUid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        if (user != null) {
                            result.setValue(Resource.success(user));
                        } else {
                            result.setValue(Resource.error("Error al mapear el usuario"));
                        }
                    } else {
                        result.setValue(Resource.error("Usuario no encontrado"));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Resource.error("Error al obtener usuario: " + e.getMessage())));

        return result;
    }
}
