package de.techfak.gse.template.domain;

public class NoCardException extends RuntimeException {
    public NoCardException(String message) {
        super(message);
    }
}
