package com.isaac.ehub.di;

import com.google.firebase.auth.FirebaseAuth;
import com.isaac.ehub.data.repository.FirebaseAuthRepositoryImpl;
import com.isaac.ehub.domain.repository.FirebaseAuthRepository;
import com.isaac.ehub.domain.usecase.auth.CheckAuthenticatedUserUseCase;
import com.isaac.ehub.domain.usecase.auth.LoginWithEmailUseCase;
import com.isaac.ehub.domain.usecase.auth.LoginWithGoogleUseCase;
import com.isaac.ehub.domain.usecase.auth.RegisterWithEmailUseCase;
import com.isaac.ehub.domain.usecase.home.SignOutUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Dagger Hilt que proporciona las dependencias necesarias
 * para la autenticación con Firebase y los casos de uso asociados.
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    /**
     * Proporciona una instancia singleton de {@link FirebaseAuth}.
     *
     * @return Instancia de FirebaseAuth
     */
    @Provides
    @Singleton
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    /**
     * Proporciona una implementación de {@link FirebaseAuthRepository}.
     *
     * @param firebaseAuth Instancia de FirebaseAuth
     * @return Implementación de FirebaseAuthRepository
     */
    @Provides
    @Singleton
    public static FirebaseAuthRepository provideFirebaseAuthRepository(FirebaseAuth firebaseAuth) {
        return new FirebaseAuthRepositoryImpl(firebaseAuth);
    }

    /**
     * Proporciona el caso de uso para iniciar sesión con email y contraseña.
     *
     * @param firebaseAuthRepository Repositorio de autenticación
     * @return Instancia de LoginWithEmailUseCase
     */
    @Provides
    @Singleton
    public static LoginWithEmailUseCase provideLoginWithEmailUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        return new LoginWithEmailUseCase(firebaseAuthRepository);
    }

    /**
     * Proporciona el caso de uso para iniciar sesión con una cuenta de Google.
     *
     * @param firebaseAuthRepository Repositorio de autenticación
     * @return Instancia de LoginWithGoogleUseCase
     */
    @Provides
    @Singleton
    public static LoginWithGoogleUseCase provideLoginWithGoogleUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        return new LoginWithGoogleUseCase(firebaseAuthRepository);
    }

    /**
     * Proporciona el caso de uso para registrar un usuario con email y contraseña.
     *
     * @param firebaseAuthRepository Repositorio de autenticación
     * @return Instancia de RegisterWithEmailUseCase
     */
    @Provides
    @Singleton
    public static RegisterWithEmailUseCase provideRegisterWithEmailUseCase(FirebaseAuthRepository firebaseAuthRepository) {
        return new RegisterWithEmailUseCase(firebaseAuthRepository);
    }

    /**
     * Proporciona el caso de uso para comprobar si un usuario ya está logeado para saltarse la fase de login.
     *
     * @param firebaseAuthRepository Repositorio de autenticación
     * @return Instancia de CheckAuthenticatedUseCase
     */
    @Provides
    @Singleton
    public static CheckAuthenticatedUserUseCase provideCheckAuthenticatedUserUseCase(FirebaseAuthRepository firebaseAuthRepository){
        return new CheckAuthenticatedUserUseCase(firebaseAuthRepository);
    }

    @Provides
    @Singleton
    public static SignOutUseCase provideSignoutUseCase(FirebaseAuthRepository firebaseAuthRepository){
        return new SignOutUseCase(firebaseAuthRepository);
    }
}
