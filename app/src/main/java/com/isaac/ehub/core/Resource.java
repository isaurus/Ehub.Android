package com.isaac.ehub.core;

/**
 * Wrapper genérico que representa el estado de una operación asíncrona.
 *
 * @param <T> Genérico para representar el tipo de dato que se espera como resultado de una operación.
 */
public class Resource<T> {
    public enum Status { LOADING, SUCCESS, ERROR, VALIDATING }

    private final Status status;    // Estado de la operación
    private final T data;           // Dato devuelto en caso de éxito
    private final String message;   // Mensaje de error

    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static <T> Resource<T> validating() { return new Resource<>(Status.VALIDATING, null, null); }

    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null,  msg);
    }

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