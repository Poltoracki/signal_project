package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

//Added missing javadoc block at the beggining of the public class, as specified in sections 7.1.1 and 7.3 of Google Java Style Guide (GJSG)
/**
 * Output strategy that writes patient health data to files.
 * Each label results in a separate text file.
 */

//changed the class name to follow GJSG
public class FileOutputStrategy implements OutputStrategy {
    //changed the name of the variable to be in camelCase to follow GJSG
    private String baseDirectory;
    //changed field name (file_map) to camelCase to follow GJSG
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    //changed declaration of the constructor to follow the new file name in accordance to GJSG
    public FileOutputStrategy(String baseDirectory) {
        //once again, changed the name for baseDirectory to be in camelCase in accordance to GJSG
        this.baseDirectory = baseDirectory;
    }

    //Added missing javadoc block at the beggining of the method, as specified in sections 7.1.1 and 7.3 of Google Java Style Guide (GJSG)
    /**
     * Outputs the patient health data to a file.
     *
     * @param patientId The ID of the patient.
     * @param timestamp The timestamp of the data.
     * @param label     The label of the data.
     * @param data      The health data to be written to the file.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            //once again, changed the name for baseDirectory to be in camelCase in accordance to GJSG
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        //changed the name of the variable to be in camelCase to follow GJSG
        String FilePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        //Unsure if it is neccesary to wrap this newBufferedWriter in multiple lines as section 4.5.1 note only mentions that wrapping should be done
        //when the readibility suffers. I will wrap it but it migh not be necessary.

        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(
                    Paths.get(FilePath), 
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}