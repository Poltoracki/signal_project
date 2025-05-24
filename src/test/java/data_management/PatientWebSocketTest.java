package data_management;

import org.junit.jupiter.api.*;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.PatientWebSocketServer;
import com.data_management.WebSocketClientImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Unit test for verifying WebSocket-based communication between
 * WebSocket server and client, and ensuring correct data storage
 * in the DataStorage system.
 */
public class PatientWebSocketTest {

    private static PatientWebSocketServer server;
    private static final int PORT = 8081; // Use a test port
    private static final String SERVER_URI = "ws://localhost:" + PORT;

    /**
     * Initializes and starts the WebSocket server before all tests.
     * Ensures the server is running and ready to accept client connections.
     */
    @BeforeAll
    public static void startServer() {
        DataStorage storage = DataStorage.getInstance();
        server = new PatientWebSocketServer(PORT, storage);
        server.start();
        try {
            Thread.sleep(1000); // Give the server time to start
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the WebSocket server after all tests have completed.
     *
     * @throws Exception if the server fails to shut down properly
     */
    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
    }

    /**
     * Tests the end-to-end communication between a WebSocket client and server.
     * Verifies that data sent from the client is received by the server,
     * stored correctly in DataStorage, and can be retrieved accurately.
     *
     * @throws Exception if connection or data processing fails
     */
    @Test
    public void testClientServerConnectionAndDataStorage() throws Exception {
        DataStorage storage = DataStorage.getInstance();
        WebSocketClientImpl client = new WebSocketClientImpl(SERVER_URI, storage);
        client.connectBlocking(); // Synchronous connect

        // Send a test message
        String message = "101,1714376789050,HeartRate,88.5";
        client.send(message);

        // Allow time for message to process
        Thread.sleep(1000);

        // Validate that the data is stored
        List<PatientRecord> records = storage.getRecords(101, 1714376789040L, 1714376789060L);
        assertEquals(1, records.size(), "One record should be stored");

        PatientRecord record = records.get(0);
        assertEquals(101, record.getPatientId());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(88.5, record.getMeasurementValue(), 0.001);

        client.close();
    }
}