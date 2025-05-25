package de.techfak.gse.template.parsingutils;

import java.io.Serial;

/**
 * The type Deck creation failed exception.
 */
public class DeckCreationFailedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new Deck creation failed exception.
     *
     * @param message the message
     */
    public DeckCreationFailedException(String message) {
        super(message);
    }
}
