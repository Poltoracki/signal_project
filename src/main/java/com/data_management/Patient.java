package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    // We chose to add this method because otherwise, it would have been
    // impossible to implement many of the alerts described in the week 3
    // project document. For example Blood Pressure data alerts, we need
    // to compare 3 consecutive patient records to check how the blood
    // pressure is changing, however the first getRecords method would 
    // force us to choose a specific time interval to check, however,
    // with that arise 2 problems, there is no objective logical way
    // for us to decide which time interval to choose since we are not
    // told with what data type we are working with, seconds, minutes, years!?
    // It is simply not specified so it is impossible for us to even set
    // custom parameters to check. For that reason instead of caring for
    // this unclear time interval, we decide to simply compare all consecutive
    // records to one another and see if any of them would fulfill the conditions.
    // For that reason, we decided to add this extra and simple method.
    public List<PatientRecord> getRecords() 
    {
        return patientRecords;
    }

    public int getId()
    {
        return patientId;
    }
}
