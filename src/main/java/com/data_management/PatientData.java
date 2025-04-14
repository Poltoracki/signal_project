package com.data_management;

public class PatientData {
    private int patientId;
    private String recordType;
    private double measurementValue;
    private long timestamp;

    /**
     * Constructs a new PatientData object with the specified parameters.
     *
     * @param patientId        The unique identifier for the patient
     * @param recordType       The type of medical record (e.g., "HeartRate", "BloodPressure")
     * @param measurementValue The value of the measurement
     * @param timestamp        The time at which the measurement was taken, in milliseconds since UNIX epoch
     */
    public PatientData(int patientId, String recordType, double measurementValue, long timestamp) {
        this.patientId = patientId;
        this.recordType = recordType;
        this.measurementValue = measurementValue;
        this.timestamp = timestamp;
    }

    // Getters and setters for each field can be added here if needed
}
