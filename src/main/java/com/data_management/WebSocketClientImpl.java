package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * A WebSocket client implementation that connects to a WebSocket server,
 * receives real-time patient data messages, parses them, and stores them
 * in a shared DataStorage instance.
 */
public class WebSocketClientImpl extends WebSocketClient {

    private final DataStorage dataStorage;

    /**
     * Constructs a WebSocketClientImpl instance.
     *
     * @param serverUri   The URI of the WebSocket server to connect to.
     * @param dataStorage The DataStorage instance to store incoming data.
     * @throws URISyntaxException If the provided URI is invalid.
     */
    public WebSocketClientImpl(String serverUri, DataStorage dataStorage) throws URISyntaxException {
        super(new URI(serverUri));
        this.dataStorage = dataStorage;
    }

    /**
     * Called when the WebSocket connection is successfully established.
     *
     * @param handshake The server handshake data.
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server.");
    }

    /**
     * Called when a message is received from the server.
     * Parses the message and stores it as a patient record.
     *
     * @param message The incoming message in CSV format.
     */
    @Override
    public void onMessage(String message) {
        try {
            // Example message format: "1,1714376789050,HeartRate,85.0"
            String[] parts = message.split(",");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid message format: " + message);
            }

            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);

            // Store the parsed data in DataStorage
            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numeric values in message: " + message);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid message received: " + message);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error while processing message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     * Attempts to reconnect in a new thread.
     *
     * @param code   The closure code.
     * @param reason The reason the connection was closed.
     * @param remote Whether the closure was initiated by the remote peer.
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server. Reason: " + reason);
        new Thread(this::reconnect).start(); // run reconnect in a new thread
    }

    /**
     * Called when an error occurs on the WebSocket connection.
     *
     * @param ex The exception describing the error.
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Reconnects to the WebSocket server in case of disconnection.
     * Ensures the reconnect logic runs outside of the WebSocket thread.
     */
    public void reconnect() {
        try {
            if (!this.isOpen()) {
                System.out.println("Attempting to reconnect to WebSocket server...");
                this.reconnectBlocking();
            }
        } catch (InterruptedException e) {
            System.err.println("Reconnection attempt interrupted.");
            e.printStackTrace();
        }
    }

    /**
     * Main method for standalone testing.
     * Creates a WebSocket client, connects to the server, and listens for messages.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            // Example usage
            DataStorage storage = DataStorage.getInstance();
            WebSocketClientImpl client = new WebSocketClientImpl("ws://localhost:8080", storage);
            client.connect();
        } catch (URISyntaxException e) {
            System.err.println("Invalid WebSocket URI.");
            e.printStackTrace();
        }
    }
}