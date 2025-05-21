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

/**
 * Implementación de FirestoreUserRepository para inyectar las dependencias necesarias y desarrollar
 * el cuerpo de los métodos declarados.
 */
@Singleton
public class FirestoreUserRepositoryImpl implements FirestoreUserRepository {

    private final FirebaseFirestore firestore;
    private final FirebaseAuthRepository firebaseAuthRepository;

    @Inject
    public FirestoreUserRepositoryImpl(FirebaseFirestore firestore, FirebaseAuthRepository firebaseAuthRepository) {
        this.firestore = firestore;
        this.firebaseAuthRepository = firebaseAuthRepository;
    }

    /**
     * Persiste al usuario en Firestore Database la primera vez que el usuario se logea con Google
     * o se registra manualmente.
     *
     * @param userModel El modelo de datos que será persistido en Firestore Database.
     * @return true en caso de éxito, false en caso contrario, encapsulado en un Resource.
     */
    @Override
    public LiveData<Resource<Boolean>> createUser(UserModel userModel) {
        MutableLiveData<Resource<Boolean>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());

        firestore.collection("users")
                .document(userModel.getFirebaseUid())
                .set(userModel)
                .addOnSuccessListener(unused -> result.setValue(Resource.success(true)))
                .addOnFailureListener(e -> result.setValue(Resource.error("Error al guardar usuario: " + e.getMessage())));

        return result;
    }

    /**
     * Persiste al usuario actualizado en Firestore Database.
     *
     * @param userModel El usuario con las nuevas propiedades a persistir.
     * @return true si la acción es exitosa, false en caso contrario, encapsulado en Resource.
     */
    @Override
    public LiveData<Resource<Boolean>> updateUser(UserModel userModel) {
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
