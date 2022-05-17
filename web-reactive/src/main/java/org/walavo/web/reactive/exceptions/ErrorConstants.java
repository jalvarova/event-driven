package org.walavo.web.reactive.exceptions;

public final class ErrorConstants {

    public static final String NAME_GENERIC = "DAD Generic Error";

    public static final String ERROR_GENERIC = "# Unhandled \n" +
            "No se ha manejado una excepción correctamente y este error indica que no hay más";

    public static final String ERROR_VALIDATION = "Ocurrió un error de validación al ejecutar la solicitud\n" +
            "# Causa\n" +
            "Uno o más datos de la entidad de solicitud contiene errores de validación, la excepción entregará un campo details con el detalle del error";

    public static final String BAD_REQUEST = "El request body es inválido";

    public static final String UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type";

    public static final String NOT_FOUND_EXCEPTION = "# NotFound\n" +
            "Solicitó acceso a un recurso que no esta disponible" +
            "# Causa\n" +
            "Generado cuando intenta consultar, editar o eliminar un recurso que es inexistente.";

    public static final String PRECONDITION_EXCEPTION =
            "# NotFound\n" +
                    "Solicitó acceso a un recurso que no esta disponible" +
                    "# Causa\n" +
                    "Generado cuando intenta consultar, editar o eliminar un recurso que es inexistente.";

    public static final String JSON_ERROR = "Error writing JSON output";
}
