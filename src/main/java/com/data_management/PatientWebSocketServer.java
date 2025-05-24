package com.data_management;

import java.net.InetSocketAddress;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

/**
 * WebSocket server that accepts incoming patient records from clients
 * and broadcasts them to all connected clients (except the original sender).
 * Received messages are parsed and stored using the DataStorage instance.
 */
public class PatientWebSocketServer extends WebSocketServer {
    private final DataStorage data;

    /**
     * Constructs a PatientWebSocketServer with a provided DataStorage instance.
     *
     * @param port    The port number the WebSocket server will listen on.
     * @param storage The DataStorage instance for storing incoming data.
     */
    public PatientWebSocketServer(int port, DataStorage storage) {
        super(new InetSocketAddress(port));
        this.data = storage;
    }

    /**
     * Constructs a PatientWebSocketServer using the singleton DataStorage instance.
     *
     * @param port The port number the WebSocket server will listen on.
     */
    public PatientWebSocketServer(int port) {
        super(new InetSocketAddress(port));
        this.data = DataStorage.getInstance(); // use singleton fallback
    }

    /**
     * Called when a client establishes a connection with the server.
     *
     * @param conn      The WebSocket connection object.
     * @param handshake The handshake data sent by the client.
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New client connected: " + conn.getRemoteSocketAddress());
    }

    /**
     * Called when a client disconnects from the server.
     *
     * @param conn   The WebSocket connection that was closed.
     * @param code   The closure status code.
     * @param reason A description of why the connection was closed.
     * @param remote Whether the closure was initiated remotely.
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Client disconnected: " + conn.getRemoteSocketAddress() + " Reason: " + reason);
    }

    /**
     * Called when an error occurs on the server or with a client connection.
     *
     * @param conn The WebSocket connection that caused the error (can be null).
     * @param ex   The exception that was thrown.
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Called once the WebSocket server has started successfully.
     */
    @Override
    public void onStart() {
        System.out.println("WebSocket server started on port: " + getPort());
    }

    /**
     * Called when a client sends a message to the server.
     * Parses the message, stores the data in DataStorage, and
     * broadcasts it to all other connected clients.
     *
     * Expected message format:
     * <pre>
     *     patientId,timestamp,recordType,measurementValue
     * </pre>
     *
     * @param sender  The client that sent the message.
     * @param message The message sent from the client.
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
