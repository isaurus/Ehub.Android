package com.isaac.ehub.ui.auth;

import com.isaac.ehub.core.Resource;

/**
 * Clase que representa el estado de la vista para el RegistrationFragment
 * Utiliza la clase Resource para gestionar estados de la operación asíncrona.
 */
public class RegisterViewState {

    private final Resource<?> resource;
    private final boolean isEmailValid;
    private final boolean isPasswordValid;
    private final boolean isConfirmPasswordValid;

    private RegisterViewState(Resource<?> resource, boolean isEmailValid,
                                  boolean isPasswordValid, boolean isConfirmPasswordValid) {
        this.resource = resource;
        this.isEmailValid = isEmailValid;
        this.isPasswordValid = isPasswordValid;
        this.isConfirmPasswordValid = isConfirmPasswordValid;
    }

    public static RegisterViewState validating(boolean isEmailValid,
                                                   boolean isPasswordValid,
                                                   boolean isConfirmPasswordValid) {
        return new RegisterViewState(
                Resource.validating(),
                isEmailValid,
                isPasswordValid,
                isConfirmPasswordValid);
    }

    public static RegisterViewState loading() {
        return new RegisterViewState(
                Resource.loading(),
                true,
                true,
                true);
    }

    public static <T> RegisterViewState success() {
        return new RegisterViewState(Resource.success(true),
                true,
                true,
                true);
    }

    public static RegisterViewState error(String message) {
        return new RegisterViewState(
                Resource.error(message),
                true,
                true,
                true);
    }


    public Resource<?> getResource() {
        return resource;
    }

    public Resource.Status getStatus() {
        return resource.getStatus();
    }

    public String getMessage() {
        return resource.getMessage();
    }

    public <T> T getData() {
        return (T) resource.getData();
    }


    public boolean isEmailValid() {
        return isEmailValid;
    }

    public boolean isPasswordValid() {
        return isPasswordValid;
    }

    public boolean isConfirmPasswordValid() {
        return isConfirmPasswordValid;
    }

    public boolean isLoading() {
        return resource.getStatus() == Resource.Status.LOADING;
    }

    public boolean isSuccess() {
        return resource.getStatus() == Resource.Status.SUCCESS;
    }

    public boolean isError() {
        return resource.getStatus() == Resource.Status.ERROR;
    }
}