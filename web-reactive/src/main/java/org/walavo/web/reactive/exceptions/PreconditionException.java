package org.walavo.web.reactive.exceptions;

public class PreconditionException extends Exception {
    public PreconditionException() {
        super();
    }

    public PreconditionException(String s) {
        super(s);
    }

    public PreconditionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
