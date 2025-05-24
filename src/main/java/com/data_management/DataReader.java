package com.data_management;

import java.io.IOException;
import java.net.http.WebSocket;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param socket the WebSocket connection to read data from
     * @throws IOException if there is an error reading the data
     */
    void readData(WebSocket socket) throws IOException;
}
