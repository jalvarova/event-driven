package org.walavo.web.reactive.exceptions;

public class NotFoundException extends Exception {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String s) {
        super(s);
    }

    public NotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
