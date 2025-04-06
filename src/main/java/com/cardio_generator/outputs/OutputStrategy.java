package com.cardio_generator.outputs;

/**
 * Defines a common strategy interface for outputting patient data.
 * Implementations can handle output to various destinations such as files,
 * databases, consoles, or network streams.
 */
public interface OutputStrategy {

    /**
     * Outputs patient data to a specific destination.
     *
     * @param patientId  The unique identifier of the patient
     * @param timestamp  The timestamp at which the data was recorded, in milliseconds since epoch
     * @param label      A descriptive label for the type of data being outputted (e.g., heart rate)
     * @param data       The actual data value to output, formatted as a String
     */
    void output(int patientId, long timestamp, String label, String data);
}
