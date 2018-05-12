package uk.ac.cam.cl.data;

/**
 * Thrown in the event of a non-recoverable API failure mode
 * DO NOT CATCH - FIX!!
 * @author Nathan Corbyn
 */
public class APIFailure extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message passed to the super class
     */
    public APIFailure(String message) {
        super(message);
    }
}
