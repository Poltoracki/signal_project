package com.cardio_generator.outputs;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

/**
 * An implementation of OutputStrategy that sends patient data to connected clients over WebSocket.
 * It starts a WebSocket server on the given port and broadcasts data to all clients.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Constructs a WebSocketOutputStrategy that starts a WebSocket server on the specified port.
     *
     * @param port The port number on which the WebSocket server will listen for connections
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Sends patient data as a formatted string to all connected WebSocket clients.
     *
     * @param patientId  The unique identifier of the patient
     * @param timestamp  The timestamp at which the data was recorded, in milliseconds since epoch
     * @param label      A descriptive label for the type of data being outputted
     * @param data       The actual data value to output, formatted as a String
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (label == null || label.isEmpty() || data == null || data.isEmpty()) {
            System.err.println("Invalid data: Missing label or data value.");
            return;
        }

        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            try {
                conn.send(message);
            } catch (Exception e) {
                System.err.println("Failed to send message to client: " + conn.getRemoteSocketAddress());
                e.printStackTrace();
            }
        }
    }

    /**
     * A simple WebSocket server that handles connection lifecycle events.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        /**
         * Constructs a WebSocket server bound to the specified address.
         *
         * @param address The address and port for the WebSocket server to bind to
         */
        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        /**
         * Called when a new client opens a WebSocket connection.
         *
         * @param conn       The WebSocket connection object
         * @param handshake  The handshake data sent by the client
         */
        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a WebSocket connection is closed.
         *
         * @param conn    The WebSocket connection that was closed
         * @param code    The closure code
         * @param reason  A description of why the connection was closed
         * @param remote  Whether the closure was initiated remotely
         */
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a message is received from a client.
         * Not used in this implementation.
         *
         * @param conn     The WebSocket connection from which the message was received
         * @param message  The message received from the client
         */
        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        /**
         * Called when an error occurs with a connection or during server operation.
         *
         * @param conn  The WebSocket connection that encountered the error (may be null)
         * @param ex    The exception that was thrown
         */
        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        /**
         * Called once the WebSocket server has started successfully.
         */
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
