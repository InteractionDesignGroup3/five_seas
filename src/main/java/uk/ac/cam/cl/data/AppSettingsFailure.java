package uk.ac.cam.cl.data;

/**
 * Thrown in the event that no app settings could be loaded
 * DO NOT CATCH - FIX!!
 * @author Nathan Corbyn
 */
public class AppSettingsFailure extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message passed to the super class
     */
    public AppSettingsFailure(String message) {
        super(message);
    }
}
