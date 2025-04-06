package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Implements the OutputStrategy interface to output patient data over a TCP connection.
 * This class sets up a TCP server to accept connections from clients and sends data to connected clients.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Creates a new TCP output strategy on the specified port.
     * Initializes a server socket to accept client connections asynchronously.
     *
     * @param port The port number on which the TCP server will listen for connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to avoid blocking the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends formatted patient data to the connected TCP client.
     *
     * @param patientId  The unique identifier of the patient
     * @param timestamp  The timestamp at which the data was recorded, in milliseconds since epoch
     * @param label      A descriptive label for the type of data being outputted (e.g., heart rate)
     * @param data       The actual data value to output, formatted as a String
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
