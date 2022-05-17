package org.walavo.web.reactive.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.walavo.web.reactive.exceptions.ErrorConstants.*;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<?> handleAllExceptions(Throwable ex) {
        String stackTrace = Arrays.toString(ex.getStackTrace());
        Error dadError = Error.builder()
                .issue(stackTrace)
                .location(ex.getLocalizedMessage())
                .build();
        ErrorException error = new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_GENERIC, Collections.singletonList(dadError), ex);
        return Mono.just(error);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the ApiError object
     */
    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<?> handleConstraintViolation(WebExchangeBindException ex) {

        List<Error> error = ex.getFieldErrors()
                .stream()
                .map(e -> Error
                        .builder()
                        .field(e.getField())
                        .value(Objects.nonNull(e.getRejectedValue()) ? e.getRejectedValue().toString() : "")
                        .issue(e.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());
        ErrorException apiError = new ErrorException(HttpStatus.BAD_REQUEST, ERROR_VALIDATION, error, ex);
        return Mono.just(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Mono<?>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return new ResponseEntity<>(Mono.just(new ErrorException(HttpStatus.CONFLICT, "Database error", ex.getCause())), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(Mono.just(new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, ex)), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String s = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        ErrorException apiError = new ErrorException(HttpStatus.BAD_REQUEST, s);
        apiError.setDebugMessage(ex.getMessage());
        return Mono.just(apiError);
    }

    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<?> handleHttpMessageNotWritable(HttpMessageNotWritableException ex) {
        ErrorException apiError = new ErrorException(HttpStatus.INTERNAL_SERVER_ERROR, JSON_ERROR, ex);
        return Mono.just(apiError);
    }
//
//    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
//    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//    public Mono<?> handleHttpMediaTypeNotSupported(HttpMediaTypeNotAcceptableException ex) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(UNSUPPORTED_MEDIA_TYPE);
//        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
//        return Mono.just(new DadException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Mono<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<Error> error = ex.getFieldErrors()
                .stream()
                .map(e -> Error
                        .builder()
                        .field(e.getField())
                        .value(Objects.nonNull(e.getRejectedValue()) ? e.getRejectedValue().toString() : "")
                        .issue(e.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        List<Error> dadAllGlobalError = ex.getGlobalErrors()
                .stream()
                .map(e -> Error
                        .builder()
                        .field(e.getObjectName())
                        .value("")
                        .issue(e.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        error.addAll(dadAllGlobalError);

        ErrorException apiError = new ErrorException(HttpStatus.BAD_REQUEST, "Validation error", error);

        return Mono.just(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<?> handleEntityNotFound(NotFoundException ex) {
        ErrorException apiError = new ErrorException(HttpStatus.NOT_FOUND, NOT_FOUND_EXCEPTION);
        return Mono.just(apiError);
    }

    @ExceptionHandler(PreconditionException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public Mono<?> handlePreCondition(PreconditionException ex) {
        ErrorException apiError = new ErrorException(HttpStatus.PRECONDITION_FAILED, PRECONDITION_EXCEPTION);
        return Mono.just(apiError);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected Mono<?> handleConflict(RuntimeException ex) {
        String bodyOfResponse = "This should be application specific";
        ErrorException apiError = new ErrorException(HttpStatus.CONFLICT, bodyOfResponse, ex);
        return Mono.just(apiError);
    }

}
