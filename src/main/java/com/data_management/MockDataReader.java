package com.data_management;

import java.io.IOException;
import java.net.http.WebSocket;

public class MockDataReader implements DataReader{

    public MockDataReader()
    {
        
    }
    @Override
    public void readData(WebSocket dataStorage) throws IOException 
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readData'");
    }
    
}
