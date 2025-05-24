package com.data_management;

import java.net.InetSocketAddress;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * WebSocket server that accepts incoming patient records
 * and broadcasts them to all connected clients (except the sender).
 */
public class PatientWebSocketServer extends WebSocketServer {
    private final DataStorage data;

    public PatientWebSocketServer(int port, DataStorage storage) {
        super(new InetSocketAddress(port));
        this.data = storage;
    }

    public PatientWebSocketServer(int port) {
        super(new InetSocketAddress(port));
        this.data = DataStorage.getInstance(); // use singleton fallback
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New client connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress() + " Reason: " + reason);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started on port: " + getPort());
    }

    /**
     * Called when a client sends a message.
     * Expects message format: patientId,timestamp,recordType,measurementValue
     */
    @Override
    public void onMessage(WebSocket sender, String message) {
        try {
            String[] parts = message.split(",");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid message format: " + message);
            }

            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String recordType = parts[2];
            double measurementValue = Double.parseDouble(parts[3]);

            // Store in DataStorage
            data.addPatientData(patientId, measurementValue, recordType, timestamp);

            // Re-broadcast to all clients EXCEPT the sender
            for (WebSocket client : getConnections()) {
                if (!client.equals(sender)) {
                    client.send(message);
                }
            }

            System.out.println("Received and broadcasted: " + message);
        } catch (Exception e) {
            System.err.println("Failed to handle message: " + message);
            e.printStackTrace();
        }
    }
}