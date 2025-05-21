package com.isaac.ehub.domain.repository;

import androidx.lifecycle.LiveData;

import com.isaac.ehub.core.Resource;
import com.isaac.ehub.domain.model.UserModel;

/**
 * Abstracción de la capa de Dominio inyectada en las implementaciones de los repoitorios. Permiten
 * a la capa de Presentación utilizar funciones de la capa de Datos sin necesidad de conocerla.
 */
public interface FirebaseAuthRepository {

    /**
     * Registro del usuario con email y contraseña. Méto-do tradicional.
     *
     * @param email El correo del usuario.
     * @param password La contraseña del usuario.
     * @return true o false según si la operación es o no exitosa encapsulado en un Resource
     */
    LiveData<Resource<Boolean>> registerWithEmail(String email, String password);

    /**
     * Login del usuario con email y contraseña. Méto-do tradicional.
     *
     * @param email El correo del usuario.
     * @param password La contraseña del usuario.
     * @return true o false según si la operación es o no exitosa encapsulado en un Resource.
     */
    LiveData<Resource<Boolean>> loginWithEmail(String email, String password);

    /**
     * Registro del usuario con email y contraseña. Méto-do tradicional.
     *
     * @param idToken El token recibido de Google para el proceso de autenticación en Firebase.
     * @return true o false según si la operación es o no exitosa encapsulado en un Resource.
     */
    LiveData<Resource<Boolean>> loginWithGoogle(String idToken);

    /**
     * Obtiene el usuario autenticado (FirebaseUser) mapeado a un UserModel.
     *
     * @return El usuario autenticado mapeado a UserModel.
     */
    UserModel getAuthenticatedUser();

    /**
     * Comprueba si el usuario ha iniciado sesión previamente en la aplicación para lanzar las
     * funcionalidades principales y saltarse la fase de login.
     *
     * @return true o false según si el usuario está o no logeado.
     */
    boolean isUserAuthenticated();

    /**
     * Cierra la sesión del usuario activo para devolverle a la fase de login/registro.
     */
    void signOut();
}
