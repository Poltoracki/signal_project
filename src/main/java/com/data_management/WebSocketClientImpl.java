package com.data_management;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

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

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server.");
    }

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

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server. Reason: " + reason);
        // Attempt to reconnect
        reconnect();
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Reconnects to the WebSocket server in case of disconnection.
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