package com.isaac.ehub.core;

/**
 * Wrapper genérico que representa el estado de una operación asíncrona,
 * como puede ser una llamada a API, consulta a base de datos, validación, etc.
 *
 * Esta clase es ampliamente utilizada en arquitecturas MVVM para comunicar
 * desde el ViewModel hacia la View el estado actual de una operación,
 * permitiendo manejar fácilmente los casos de carga, éxito, error y validación.
 *
 * @param <T> Tipo de dato esperado como resultado de la operación, por ejemplo,
 *            una lista de objetos, un usuario, un booleano, etc.
 */
public class Resource<T> {

    /**
     * Enumeración que define los posibles estados de la operación.
     */
    public enum Status {
        /**
         * Estado que indica que la operación está en progreso.
         */
        LOADING,

        /**
         * Estado que indica que la operación fue exitosa.
         * En este caso el atributo {@code data} contendrá el resultado.
         */
        SUCCESS,

        /**
         * Estado que indica que la operación falló.
         * En este caso el atributo {@code message} contendrá el mensaje de error.
         */
        ERROR,

        /**
         * Estado adicional para procesos de validación,
         * por ejemplo, la validación de formularios antes de enviar datos.
         */
        VALIDATING
    }

    private final Status status;    // Estado actual de la operación
    private final T data;           // Datos retornados (solo si es exitosa)
    private final String message;   // Mensaje de error (solo si hay fallo)

    /**
     * Constructor privado para asegurar la creación mediante métodos estáticos
     * que garantizan la consistencia del estado y datos asociados.
     *
     * @param status Estado actual de la operación
     * @param data Datos retornados por la operación (o null)
     * @param message Mensaje de error (o null)
     */
    private Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    //--- Métodos estáticos factory (patrón factory) ---//

    /**
     * Crea un Resource que representa una operación en progreso (loading).
     *
     * @param <T> Tipo del dato esperado
     * @return instancia Resource en estado LOADING
     */
    public static <T> Resource<T> loading() {
        return new Resource<>(Status.LOADING, null, null);
    }

    /**
     * Crea un Resource que representa una operación exitosa.
     *
     * @param data Datos resultantes de la operación
     * @param <T> Tipo del dato
     * @return instancia Resource en estado SUCCESS con los datos
     */
    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    /**
     * Crea un Resource que representa una operación fallida.
     *
     * @param msg Mensaje descriptivo del error ocurrido
     * @param <T> Tipo del dato esperado
     * @return instancia Resource en estado ERROR con mensaje de error
     */
    public static <T> Resource<T> error(String msg) {
        return new Resource<>(Status.ERROR, null, msg);
    }

    /**
     * Crea un Resource que representa un estado de validación en curso.
     *
     * @param <T> Tipo del dato esperado
     * @return instancia Resource en estado VALIDATING
     */
    public static <T> Resource<T> validating() {
        return new Resource<>(Status.VALIDATING, null, null);
    }

    //--- Getters ---//

    /**
     * Obtiene el estado actual de la operación.
     *
     * @return estado (Status)
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Obtiene los datos resultantes de la operación, si existen.
     *
     * @return datos de tipo T, o null si no hay datos disponibles
     */
    public T getData() {
        return data;
    }

    /**
     * Obtiene el mensaje de error, si la operación falló.
     *
     * @return mensaje de error o null si no hay error
     */
    public String getMessage() {
        return message;
    }

    //--- Métodos de conveniencia para consultar el estado ---//

    /**
     * Indica si la operación está actualmente en progreso (loading).
     *
     * @return true si está cargando, false en caso contrario
     */
    public boolean isLoading() {
        return status == Status.LOADING;
    }

    /**
     * Indica si la operación fue exitosa.
     *
     * @return true si fue exitosa, false en caso contrario
     */
    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    /**
     * Indica si la operación terminó en error.
     *
     * @return true si hubo error, false en caso contrario
     */
    public boolean isError() {
        return status == Status.ERROR;
    }
}
