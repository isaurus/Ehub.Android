package com.isaac.ehub.ui.auth;

import com.isaac.ehub.core.Resource;

/**
 * Clase que representa el estado actual de la vista para el {@link RegistrationFragment}.
 * Utiliza la clase {@link Resource} para gestionar los diferentes estados
 * de una operación asíncrona (validando, cargando, éxito, error) durante el proceso
 * de registro de usuario.
 *
 * Además, contiene información sobre la validez de los campos del formulario:
 * email, contraseña y confirmación de contraseña. Esto permite gestionar
 * errores de validación y mostrar feedback adecuado en la interfaz de usuario.
 */
public class RegisterViewState {

    private final Resource<?> resource;
    private final boolean isEmailValid;
    private final boolean isPasswordValid;
    private final boolean isConfirmPasswordValid;

    /**
     * Constructor privado para inicializar el estado de la vista con el recurso y la validez
     * de los campos de email, contraseña y confirmación de contraseña.
     *
     * @param resource recurso que representa el estado asíncrono actual
     * @param isEmailValid indica si el email es válido
     * @param isPasswordValid indica si la contraseña es válida
     * @param isConfirmPasswordValid indica si la confirmación de contraseña es válida
     */
    private RegisterViewState(Resource<?> resource, boolean isEmailValid,
                              boolean isPasswordValid, boolean isConfirmPasswordValid) {
        this.resource = resource;
        this.isEmailValid = isEmailValid;
        this.isPasswordValid = isPasswordValid;
        this.isConfirmPasswordValid = isConfirmPasswordValid;
    }

    /**
     * Crea un estado que representa la validación en curso, con la información
     * sobre la validez de cada campo del formulario.
     *
     * @param isEmailValid validez del email
     * @param isPasswordValid validez de la contraseña
     * @param isConfirmPasswordValid validez de la confirmación de contraseña
     * @return instancia de RegisterViewState en estado VALIDATING
     */
    public static RegisterViewState validating(boolean isEmailValid,
                                               boolean isPasswordValid,
                                               boolean isConfirmPasswordValid) {
        return new RegisterViewState(
                Resource.validating(),
                isEmailValid,
                isPasswordValid,
                isConfirmPasswordValid);
    }

    /**
     * Crea un estado que indica que la operación de registro está en proceso (cargando).
     * Los campos se asumen válidos para no mostrar errores durante la carga.
     *
     * @return instancia de RegisterViewState en estado LOADING
     */
    public static RegisterViewState loading() {
        return new RegisterViewState(
                Resource.loading(),
                true,
                true,
                true);
    }

    /**
     * Crea un estado que indica que la operación de registro fue exitosa.
     *
     * @param <T> tipo de dato que se puede retornar (en este caso, un booleano verdadero)
     * @return instancia de RegisterViewState en estado SUCCESS
     */
    public static <T> RegisterViewState success() {
        return new RegisterViewState(Resource.success(true),
                true,
                true,
                true);
    }

    /**
     * Crea un estado que indica que la operación de registro falló con un mensaje de error.
     *
     * @param message mensaje de error para mostrar en la UI
     * @return instancia de RegisterViewState en estado ERROR
     */
    public static RegisterViewState error(String message) {
        return new RegisterViewState(
                Resource.error(message),
                true,
                true,
                true);
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
     * Indica si la confirmación de contraseña es válida según la última validación.
     *
     * @return true si la confirmación de contraseña es válida, false en caso contrario
     */
    public boolean isConfirmPasswordValid() {
        return isConfirmPasswordValid;
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
