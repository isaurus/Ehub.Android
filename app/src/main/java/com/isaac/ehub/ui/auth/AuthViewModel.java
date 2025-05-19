package com.isaac.ehub.ui.auth;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.isaac.ehub.domain.usecase.auth.CheckAuthenticatedUserUseCase;
import com.isaac.ehub.domain.usecase.auth.LoginWithEmailUseCase;
import com.isaac.ehub.domain.usecase.auth.LoginWithGoogleUseCase;
import com.isaac.ehub.domain.usecase.auth.RegisterWithEmailUseCase;
import com.isaac.ehub.domain.usecase.auth.SaveUserIfNotExistsUseCase;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel encargado de gestionar la lógica de autenticación de usuarios.
 * Comunica los eventos entre la capa de presentación (UI) y la lógica de negocio (UseCases),
 * y expone estados observables para actualizar la interfaz según la respuesta obtenida.
 */
@HiltViewModel
public class AuthViewModel extends ViewModel {

    private static final int MIN_PASSWORD_LENGTH = 6;

    private final CheckAuthenticatedUserUseCase checkAuthenticatedUserUseCase;
    private final LoginWithEmailUseCase loginWithEmailUseCase;
    private final LoginWithGoogleUseCase loginWithGoogleUseCase;
    private final RegisterWithEmailUseCase registerWithEmailUseCase;
    private final SaveUserIfNotExistsUseCase saveUserIfNotExistsUseCase;

    public final MutableLiveData<LoginViewState> loginViewState = new MutableLiveData<>();
    public final MutableLiveData<RegisterViewState> registerViewState = new MutableLiveData<>();

    /**
     * Constructor con inyección de dependencias que recibe todos los casos de uso
     * necesarios para el proceso de autenticación.
     *
     * @param checkAuthenticatedUserUseCase Use case para comprobar si el usuario está autenticado.
     * @param loginWithEmailUseCase Use case para iniciar sesión con email y contraseña.
     * @param loginWithGoogleUseCase Use case para iniciar sesión con cuenta de Google.
     * @param registerWithEmailUseCase Use case para registrar un usuario con email y contraseña.
     * @param saveUserIfNotExistsUseCase Use case para persistir el usuario cuando se registra.
     */
    @Inject
    public AuthViewModel(
            CheckAuthenticatedUserUseCase checkAuthenticatedUserUseCase,
            LoginWithEmailUseCase loginWithEmailUseCase,
            LoginWithGoogleUseCase loginWithGoogleUseCase,
            RegisterWithEmailUseCase registerWithEmailUseCase,
            SaveUserIfNotExistsUseCase saveUserIfNotExistsUseCase
    ) {
        this.checkAuthenticatedUserUseCase = checkAuthenticatedUserUseCase;
        this.loginWithEmailUseCase = loginWithEmailUseCase;
        this.loginWithGoogleUseCase = loginWithGoogleUseCase;
        this.registerWithEmailUseCase = registerWithEmailUseCase;
        this.saveUserIfNotExistsUseCase = saveUserIfNotExistsUseCase;
    }

    /**
     * @return Estado observable del formulario de login.
     */
    public LiveData<LoginViewState> getLoginViewState() {
        return loginViewState;
    }

    /**
     * @return Estado observable del formulario de registro.
     */
    public LiveData<RegisterViewState> getRegisterViewState() {
        return registerViewState;
    }

    /**
     * Comprueba si hay un usuario autenticado actualmente.
     *
     * @return true si el usuario está autenticado, false en caso contrario.
     */
    public boolean isUserAuthenticated() {
        return checkAuthenticatedUserUseCase.execute();
    }

    /**
     * Llama al caso de uso de login con email y actualiza el estado del ViewModel.
     *
     * @param email Email introducido por el usuario.
     * @param password Contraseña introducida por el usuario.
     */
    public void loginWithEmail(String email, String password) {
        loginWithEmailUseCase.execute(email, password).observeForever(resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    loginViewState.setValue(LoginViewState.success());
                    break;
                case ERROR:
                    loginViewState.setValue(LoginViewState.error(resource.getMessage()));
                    break;
                case LOADING:
                    loginViewState.setValue(LoginViewState.loading());
                    break;
            }
        });
    }

    /**
     * Llama al caso de uso de registro con email y actualiza el estado del ViewModel.
     *
     * @param email Email introducido por el usuario.
     * @param password Contraseña introducida por el usuario.
     */
    public void registerWithEmail(String email, String password) {
        registerWithEmailUseCase.execute(email, password).observeForever(resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    registerViewState.setValue(RegisterViewState.success());
                    break;
                case ERROR:
                    registerViewState.setValue(RegisterViewState.error(resource.getMessage()));
                    break;
                case LOADING:
                    registerViewState.setValue(RegisterViewState.loading());
                    break;
            }
        });
    }

    /**
     * Llama al caso de uso de login con Google y actualiza el estado del ViewModel.
     *
     * @param tokenId Token de autenticación obtenido de Google.
     */
    public void loginWithGoogle(String tokenId) {
        loginViewState.setValue(LoginViewState.loading());

        loginWithGoogleUseCase.execute(tokenId).observeForever(resource -> {
            switch (resource.getStatus()) {
                case SUCCESS:
                    loginViewState.setValue(LoginViewState.success());
                    break;
                case ERROR:
                    loginViewState.setValue(LoginViewState.error(resource.getMessage()));
                    break;
                case LOADING:
                    loginViewState.setValue(LoginViewState.loading());
                    break;
            }
        });
    }

    /**
     * Valida el formulario de login y lanza el login si los datos son correctos.
     *
     * @param email Email introducido por el usuario.
     * @param password Contraseña introducida por el usuario.
     */
    public void validateLoginForm(String email, String password) {
        boolean isEmailValid = isValidEmail(email);
        boolean isPasswordValid = isValidPassword(password);

        loginViewState.setValue(LoginViewState.validating(isEmailValid, isPasswordValid));

        if (isEmailValid && isPasswordValid) {
            loginViewState.setValue(LoginViewState.loading());
            loginWithEmail(email, password);
        }
    }

    /**
     * Valida el formulario de registro y lanza el registro si los datos son válidos.
     *
     * @param email Email introducido.
     * @param password Contraseña introducida.
     * @param confirmPassword Confirmación de contraseña introducida.
     */
    public void validateRegisterForm(String email, String password, String confirmPassword) {
        boolean isEmailValid = isValidEmail(email);
        boolean isPasswordValid = isValidPassword(password);
        boolean isConfirmPasswordValid = isValidConfirmPassword(password, confirmPassword);

        registerViewState.setValue(RegisterViewState.validating(isEmailValid, isPasswordValid, isConfirmPasswordValid));

        if (isEmailValid && isPasswordValid && isConfirmPasswordValid) {
            registerViewState.setValue(RegisterViewState.loading());
            registerWithEmail(email, password);
        }
    }

    /**
     * Verifica que el email tenga un formato válido.
     *
     * @param email Email a validar.
     * @return true si el formato es válido.
     */
    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Verifica que la contraseña tenga al menos una longitud mínima.
     *
     * @param password Contraseña a validar.
     * @return true si la contraseña es válida.
     */
    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * Verifica que la contraseña y su confirmación coincidan y sean válidas.
     *
     * @param password Contraseña original.
     * @param confirmPassword Contraseña de confirmación.
     * @return true si ambas coinciden y son válidas.
     */
    private boolean isValidConfirmPassword(String password, String confirmPassword) {
        return isValidPassword(password) && password.equals(confirmPassword);
    }
}
