package uk.ac.cam.cl.data;

import static com.google.common.truth.Truth.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.nio.file.Paths;
import java.util.Random;
import org.json.simple.JSONObject;
import org.junit.*;
import uk.ac.cam.cl.data.apis.Meteomatics;

/**
 * API connector unit tests
 *
 * @author Nathan Corbyn
 */
public class APIConnectorTest {
  @Test
  @SuppressWarnings("unchecked")
  public void apiConnector_returnsCache_apiRequestFails() throws ConfigurationException {
    int nonce = (new Random()).nextInt(10000);
    Cache mockCache = mock(Cache.class);
    JSONObject mockData = new JSONObject();
    mockData.put("nonce", nonce);
    when(mockCache.getData()).thenReturn(mockData);
    APIConnector<DataSequence> target =
        new APIConnector<>(new Meteomatics(), new Config("/config.json"), mockCache);

    JSONObject response = target.getData(new Location("", 50, 50));

    assertThat((int) response.get("nonce")).isEqualTo(nonce);
    verify(mockCache, times(1)).getData();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void apiConnector_updatesCache_apiRequestSucceeds() throws ConfigurationException {
    // This is probably flaky as HTTP request might fail due to network
    Cache mockCache = mock(Cache.class);
    Config config = new Config("/config.json");
    APIConnector<DataSequence> target = new APIConnector<>(new Meteomatics(), config, mockCache);

    JSONObject response = target.getData(new Location("", 50, 50));

    verify(mockCache, times(1)).update(any(JSONObject.class));
  }

  @Test
  public void apiConnector_returnsCache_invalidCoordinates() throws ConfigurationException {
    Cache mockCache = mock(Cache.class);
    Config config = new Config("/config.json");
    APIConnector<DataSequence> target = new APIConnector<>(new Meteomatics(), config, mockCache);

    target.getData(new Location("", -180, 0)); // Valid
    target.getData(new Location("", 0, 180));
    target.getData(new Location("", 180, 0));
    target.getData(new Location("", 180, 180));
    target.getData(new Location("", -280, 100)); // Invalid
    target.getData(new Location("", -320, 190));
    target.getData(new Location("", Double.MAX_VALUE, Double.MAX_VALUE));

    verify(mockCache, times(4)).update(any(JSONObject.class));
  }

  @Test
  public void apiConnector_throwsException_noConfig() {
    assertThrows(
        APIFailure.class,
        () -> new APIConnector<DataSequence>(new Meteomatics(), "/nonconfig.json"),
        "Could not load configuration /nonconfig.json");
  }

  @Test
  public void apiConnector_throwsException_cannotConstructCache() throws ConfigurationException {
    Config mock = mock(Config.class);
    when(mock.get("cache")).thenReturn("");

    assertThrows(APIFailure.class, () -> new APIConnector<DataSequence>(new Meteomatics(), mock));
  }
}
