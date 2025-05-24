package com.data_management;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval of medical records based on specified criteria.
 */
public class Patient {
    private final int patientId;
    private final List<PatientRecord> patientRecords;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); // Lock for thread-safe access

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId The unique identifier for the patient.
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and timestamp.
     *
     * @param measurementValue The measurement value to store in the record.
     * @param recordType       The type of record, e.g., "HeartRate", "BloodPressure".
     * @param timestamp        The time at which the measurement was taken, in milliseconds since UNIX epoch.
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        lock.writeLock().lock(); // Acquire write lock for thread-safe updates
        try {
            PatientRecord record = new PatientRecord(patientId, measurementValue, recordType, timestamp);
            patientRecords.add(record);
        } finally {
            lock.writeLock().unlock(); // Release write lock
        }
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within
     * a specified time range.
     *
     * @param startTime The start of the time range, in milliseconds since UNIX epoch.
     * @param endTime   The end of the time range, in milliseconds since UNIX epoch.
     * @return A list of PatientRecord objects that fall within the specified time range.
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        lock.readLock().lock(); // Acquire read lock for thread-safe access
        try {
            List<PatientRecord> filteredRecords = new ArrayList<>();
            for (PatientRecord record : patientRecords) {
                if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                    filteredRecords.add(record);
                }
            }
            return filteredRecords;
        } finally {
            lock.readLock().unlock(); // Release read lock
        }
    }

    /**
     * Retrieves all records associated with this patient.
     * This method was added to support alerts that need to examine patterns
     * or trends across multiple consecutive records, regardless of timestamp.
     *
     * @return A list containing all patient records.
     */
    public List<PatientRecord> getRecords() {
        lock.readLock().lock(); // Acquire read lock for thread-safe access
        try {
            return new ArrayList<>(patientRecords);
        } finally {
            lock.readLock().unlock(); // Release read lock
        }
    }

    /**
     * Returns the unique identifier for the patient.
     *
     * @return The patient ID.
     */
    public int getId() {
        return patientId;
    }
}