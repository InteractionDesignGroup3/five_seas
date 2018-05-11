package uk.ac.cam.cl.data;

/**
 * Thrown in the wake of an exception during configuration handling.
 * @author Nathan Corbyn
 */
public class ConfigurationException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * @param message passed to the super class
     */
    public ConfigurationException(String message) {
        super(message);
    }
}

