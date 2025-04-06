package com.cardio_generator.outputs;

/**
 * An implementation of OutputStrategy that writes output data to the console.
 * Useful for debugging or simple monitoring purposes.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

    /**
     * Outputs patient data to the system console.
     *
     * @param patientId  The unique identifier of the patient
     * @param timestamp  The timestamp at which the data was recorded, in milliseconds since epoch
     * @param label      A descriptive label for the type of data being outputted (e.g., heart rate)
     * @param data       The actual data value to output, formatted as a String
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}
