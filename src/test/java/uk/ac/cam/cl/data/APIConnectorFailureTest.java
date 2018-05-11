package uk.ac.cam.cl.data;

import java.nio.file.Paths;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.google.common.truth.Truth.*;
import static org.mockito.Mockito.*;

/**
 * API connector failure mode unit tests
 * @author Nathan Corbyn
 */
public class APIConnectorFailureTest {
    @Test
    public void apiConnector_throwsException_noConfig() {
        assertThrows(APIFailure.class, 
                () -> new APIConnector(Paths.get("nonconfig.json")),
                "Could not load configuration nonconfig.json"); 
    }

    @Test
    public void apiConnector_throwsException_cannotConstructCache() 
            throws ConfigurationException {
        Config mockConfig = mock(Config.class);
        when(mockConfig.get("cache")).thenReturn("");
        
        assertThrows(APIFailure.class, 
                () -> new APIConnector(mockConfig),
                "Could not load configuration nonconfig.json"); 
    }
}

