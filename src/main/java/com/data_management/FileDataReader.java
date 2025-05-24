package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.http.WebSocket;

/**
 * Implementation of the DataReader interface that reads data from an output file.
 */
public class FileDataReader implements DataReader {

    private final String outputDirectory;

    /**
     * Constructs a FileDataReader with the specified output directory.
     *
     * @param outputDirectory the directory containing the output files
     */
    public FileDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Reads data from the specified output directory and stores it in the provided DataStorage.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    @Override
    public void readData(WebSocket socket) throws IOException {
        File directory = new File(outputDirectory);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Invalid output directory: " + outputDirectory);
        }

        // Iterate through all files in the directory
        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                parseFile(file, socket);
            }
        }
    }

    /**
     * Parses a single file and adds the data to the DataStorage.
     *
     * @param file        the file to parse
     * @param socket the storage where data will be stored
     * @throws IOException if there is an error reading the file
     */
    private void parseFile(File file, WebSocket socket) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Example line format: "Patient ID: 1, Timestamp: 1714376789050, Label: HeartRate, Data: 85.0"
                String[] parts = line.split(", ");
                int patientId = Integer.parseInt(parts[0].split(": ")[1]);
                long timestamp = Long.parseLong(parts[1].split(": ")[1]);
                String recordType = parts[2].split(": ")[1];
                double measurementValue = Double.parseDouble(parts[3].split(": ")[1]);

                // Add the parsed data to the DataStorage
                //socket.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        }
    }
}
