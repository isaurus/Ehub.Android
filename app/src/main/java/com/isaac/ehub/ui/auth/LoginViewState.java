package com.isaac.ehub.ui.auth;

import com.isaac.ehub.core.Resource;

/**
 * Clase que representa el estado actual de la vista del {@link LoginFragment}.
 * Utiliza la clase {@link Resource} para gestionar los diferentes estados
 * de una operación asíncrona (validando, cargando, éxito, error).
 *
 * Además, contiene información sobre la validez de los campos de formulario
 * (email y contraseña), permitiendo mostrar errores de validación en la UI.
 */
public class LoginViewState {

    private final Resource<?> resource;
    private final boolean isEmailValid;
    private final boolean isPasswordValid;

    /**
     * Constructor privado que inicializa el estado de la vista con un recurso y
     * la validez de los campos de email y contraseña.
     *
     * @param resource recurso que representa el estado asíncrono actual
     * @param isEmailValid indica si el email es válido
     * @param isPasswordValid indica si la contraseña es válida
     */
    public LoginViewState(Resource<?> resource, boolean isEmailValid, boolean isPasswordValid) {
        this.resource = resource;
        this.isEmailValid = isEmailValid;
        this.isPasswordValid = isPasswordValid;
    }

    /**
     * Crea un estado de validación con la información sobre la validez de email y contraseña.
     *
     * @param isEmailValid validez del email
     * @param isPasswordValid validez de la contraseña
     * @return instancia de LoginViewState en estado VALIDATING
     */
    public static LoginViewState validating(boolean isEmailValid, boolean isPasswordValid){
        return new LoginViewState(Resource.validating(), isEmailValid, isPasswordValid);
    }

    /**
     * Crea un estado que indica que la operación de login está en proceso (cargando).
     * Los campos se asumen válidos para no mostrar errores durante la carga.
     *
     * @return instancia de LoginViewState en estado LOADING
     */
    public static LoginViewState loading(){
        return new LoginViewState(Resource.loading(), true, true);
    }

    /**
     * Crea un estado que indica que la operación de login fue exitosa.
     *
     * @param <T> tipo de dato que se puede retornar (en este caso, un booleano verdadero)
     * @return instancia de LoginViewState en estado SUCCESS
     */
    public static <T> LoginViewState success(){
        return new LoginViewState(Resource.success(true), true, true);
    }

    /**
     * Crea un estado que indica que la operación de login falló con un mensaje de error.
     *
     * @param message mensaje de error para mostrar en la UI
     * @return instancia de LoginViewState en estado ERROR
     */
    public static LoginViewState error(String message){
        return new LoginViewState(Resource.error(message), true, true);
    }

    /**
     * Obtiene el recurso que representa el estado asíncrono actual.
     *
     * @return recurso asociado a este estado
     */
    public Resource<?> getResource() {
        return resource;
    }

    /**
     * Obtiene el estado enumerado actual (VALIDATING, LOADING, SUCCESS, ERROR).
     *
     * @return estado actual del recurso
     */
    public Resource.Status getStatus() {
        return resource.getStatus();
    }

    /**
     * Obtiene el mensaje asociado al recurso, generalmente usado para mensajes de error.
     *
     * @return mensaje del recurso o null si no hay mensaje
     */
    public String getMessage() {
        return resource.getMessage();
    }

    /**
     * Obtiene el dato contenido en el recurso, si existe.
     *
     * @param <T> tipo esperado del dato
     * @return dato contenido en el recurso o null
     */
    public <T> T getData() {
        return (T) resource.getData();
    }

    /**
     * Indica si el email es válido según la última validación.
     *
     * @return true si el email es válido, false en caso contrario
     */
    public boolean isEmailValid() {
        return isEmailValid;
    }

    /**
     * Indica si la contraseña es válida según la última validación.
     *
     * @return true si la contraseña es válida, false en caso contrario
     */
    public boolean isPasswordValid() {
        return isPasswordValid;
    }

    /**
     * Indica si el estado actual corresponde a una carga en proceso.
     *
     * @return true si está cargando, false en caso contrario
     */
    public boolean isLoading() {
        return resource.getStatus() == Resource.Status.LOADING;
    }

    /**
     * Indica si el estado actual corresponde a una operación exitosa.
     *
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean isSuccess() {
        return resource.getStatus() == Resource.Status.SUCCESS;
    }

    /**
     * Indica si el estado actual corresponde a un error.
     *
     * @return true si hay un error, false en caso contrario
     */
    public boolean isError() {
        return resource.getStatus() == Resource.Status.ERROR;
    }
}
