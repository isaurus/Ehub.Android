package com.isaac.ehub.domain.repository;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;

/**
 * Abstracción de la capa de Dominio inyectada en las implementaciones de los repositorios. Permiten
 * a la capa de Presentación utilizar funciones de la capa de Datos sin necesidad de conocerla.
 */
public interface FirestoreUserRepository {

    /**
     * Persiste a un usuario en Firestore Database la primera vez que el usuario se logea con Google
     * o se registra manualmente. El UserModel se recibe desde 'getAuthenticatedUser()', que se mapea
     * a UserModel obteniendo su firebaseUid y su email.
     *
     * @param userModel El modelo de datos que será persistido en Firestore Database.
     * @return true en caso de éxito, false en caso contrario, encapsulado en un Resource.
     */
    LiveData<Resource<Boolean>> createUser(UserModel userModel);

    /**
     * Persiste al usuario actualizado en Firestore Database.
     *
     * @param userModel El usuario con las nuevas propiedades a persistir.
     * @return true si la acción es exitosa, false en caso contrario, encapsulado en Resource.
     */
    LiveData<Resource<Boolean>> updateUser(UserModel userModel);

    /**
     * Obtiene el usuario persistido en Firestore Database.
     *
     * @return UserModel para pintar sus propiedades en la UI.
     */
    LiveData<Resource<UserModel>> getCurrentUser();
}
