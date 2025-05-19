package com.isaac.ehub.core;

/**
 * Wrapper genérico que representa el estado de una operación asíncrona (API, BD, etc.).
 * Es usado comúnmente en ViewModels para notificar a la View sobre cambios de estado.
 *
 * @param <T> Tipo de dato esperado como resultado (ej: List<Movie>, User, etc.).
 */
public class Resource<T> {

    /**
     * Estados posibles de la operación.
     */
    public enum Status {
        LOADING,    // Operación en progreso
        SUCCESS,    // Operación exitosa (data contiene el resultado)
        ERROR,      // Operación fallida (message contiene el error)
        VALIDATING  // Estado adicional para validaciones (ej: formularios)
    }

    private final Status status;    // Estado actual
    private final T data;           // Datos retornados (en caso de éxito)
    private final String message;   // Mensaje de error (en caso de fallo)

    // Constructor privado (solo se crea mediante métodos estáticos)
    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    //--- Métodos estáticos factory (patrón factory) ---//

    /**
     * Operación en progreso (loading).
     */
    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    /**
     * Operación exitosa.
     * @param data Datos retornados por la operación.
     */
    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    /**
     * Operación fallida.
     * @param msg Mensaje de error.
     */
    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }

    /**
     * Operación en validación (ej: campos de un formulario).
     */
    public static <T> Resource<T> validating() {
        return new Resource<>(Status.VALIDATING, null, null);
    }

    //--- Getters ---//

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    //--- Métodos de conveniencia ---//

    public boolean isLoading() {
        return status == Status.LOADING;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }
}