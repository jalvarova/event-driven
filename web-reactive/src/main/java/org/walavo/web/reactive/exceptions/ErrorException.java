package org.walavo.web.reactive.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
@JsonIgnoreProperties(value = {"status", "message", "debugMessage", "timestamp", "details"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorException {

    private HttpStatus status;
    private String message;
    private String debugMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private List<Error> details = new ArrayList<>();

    public ErrorException() {

    }

    public ErrorException(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }


    public ErrorException(HttpStatus status, Throwable ex) {
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
        this.status = status;
    }

    public ErrorException(HttpStatus status, String message, List<Error> details) {
        this.message = message;
        this.status = status;
        this.details = details;
    }


    public ErrorException(HttpStatus status, String message, Throwable throwable) {
        this.message = message;
        this.debugMessage = throwable.getLocalizedMessage();
        this.status = status;
    }

    public ErrorException(HttpStatus status, String message, List<Error> details, Throwable throwable) {
        this.message = message;
        this.debugMessage = throwable.getLocalizedMessage();
        this.details = details;
        this.status = status;
    }
}