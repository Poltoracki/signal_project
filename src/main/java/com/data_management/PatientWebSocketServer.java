package com.data_management;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public class PatientWebSocketServer extends WebSocketServer
{
    private DataStorage data;
    WebSocket koza;
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onOpen'");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClose'");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onMessage'");
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onError'");
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onStart'");
    }
    
}
