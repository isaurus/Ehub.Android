package com.isaac.ehub.ui.auth;

import com.isaac.ehub.core.Resource;

/**
 * Clase que representa el estado de la vista para el LoginFragment. Utiliza la clase Resource
 * (/core/Resource.java) para gestionar estados de la operación asíncrona.
 */
public class LoginViewState {

    private final Resource<?> resource;
    private final boolean isEmailValid;
    private final boolean isPasswordValid;

    public LoginViewState(Resource<?> resource, boolean isEmailValid, boolean isPasswordValid) {
        this.resource = resource;
        this.isEmailValid = isEmailValid;
        this.isPasswordValid = isPasswordValid;
    }

    public static LoginViewState validating(boolean isEmailValid, boolean isPasswordValid){
        return new LoginViewState(Resource.validating(), isEmailValid, isPasswordValid);
    }

    public static LoginViewState loading(){
        return new LoginViewState(Resource.loading(), true, true);
    }

    public static <T> LoginViewState success(){
        return new LoginViewState(Resource.success(true), true, true);
    }

    public static LoginViewState error(String message){
        return new LoginViewState(Resource.error(message), true, true);
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
