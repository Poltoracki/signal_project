package com.alerts;

/**
 * Represents an alert generated based on patient conditions.
 * Stores information including the patient identifier, condition description, and timestamp.
 */
public class Alert {

    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Constructs a new Alert instance.
     *
     * @param patientId  The unique identifier of the patient associated with this alert.
     * @param condition  The condition or event triggering the alert.
     * @param timestamp  The timestamp when the alert was generated, in milliseconds since epoch.
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns the patient identifier associated with the alert.
     *
     * @return The patient's unique identifier.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Returns the condition or event that triggered the alert.
     *
     * @return The description of the condition.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the timestamp at which the alert was generated.
     *
     * @return The alert's timestamp, in milliseconds since epoch.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
