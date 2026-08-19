package sheppy;

/** Represents an error caused by invalid Sheppy input. */
public class SheppyException extends Exception {
    /**
     * Creates an exception with a user-facing explanation.
     *
     * @param message the explanation of the input error
     */
    public SheppyException(String message) {
        super(message);
    }
}
