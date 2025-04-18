package com.alerts;

import java.util.List;

import com.data_management.*;

// JavaDoc commenting style retained for this class since it provides 
// external API documentation beneficial for clarity and consistency.

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    // Marked the field 'dataStorage' as final to clearly enforce immutability
    // after the object initialization in accordance with best practices.
    private final DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * 
     * // Shortened the original comment by removing redundancy since
     * // the class-level JavaDoc already describes its usage.
     *
     * @param dataStorage The data storage system that provides access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions are met.
     * If a condition is met, an alert is triggered via the {@link #triggerAlert(Alert)} method.
     * If an alert is triggered, it needs to be added to the {@link AlertManager} repository.
     * 
     * // Improved readability by removing unnecessary line breaks and corrected
     * // JavaDoc inline link formatting to explicitly include the method parameter.
     *
     * @param patient The patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient, long startTime, long endTime) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), startTime, endTime);
        //sort the records so that they're chronically aligned
        records.sort((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()));

        // Blood pressure data alerts
        List<PatientRecord> sBloodRecords = dataStorage.getRecords("systolic blood pressure", records);
        List<PatientRecord> dBloodRecords = dataStorage.getRecords("diastolic blood pressure", records);

        // Systolic blood pressure
        if(sBloodRecords.size() < 1) {
            System.out.println("Insufficient number of records to check for systolic blood pressure!");
        }
        else {
            for(int i = 0; i < sBloodRecords.size(); i++)
            {
                if( i < sBloodRecords.size()-3)
                {
                // Checking if it's decreasing
                if((sBloodRecords.get(i).getMeasurementValue() - sBloodRecords.get(i+1).getMeasurementValue()) > 10 &&
                (sBloodRecords.get(i+1).getMeasurementValue() - sBloodRecords.get(i+2).getMeasurementValue()) > 10 &&
                (sBloodRecords.get(i+2).getMeasurementValue() - sBloodRecords.get(i+3).getMeasurementValue()) > 10)
                {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Decreasing trend in systolic blood pressure", sBloodRecords.get(i+3).getTimestamp());
                    triggerAlert(alert);
                }

                // Checking if it's increasing
                if((sBloodRecords.get(i).getMeasurementValue() - sBloodRecords.get(i+1).getMeasurementValue()) < -10 &&
                (sBloodRecords.get(i+1).getMeasurementValue() - sBloodRecords.get(i+2).getMeasurementValue()) < -10 &&
                (sBloodRecords.get(i+2).getMeasurementValue() - sBloodRecords.get(i+3).getMeasurementValue()) < -10)
                {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Increasing trend in systolic blood pressure", sBloodRecords.get(i+3).getTimestamp());
                    triggerAlert(alert);
                }
                }

                // Checking for critical upper threshold
                if(sBloodRecords.get(i).getMeasurementValue() > 180) {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Passed critial upper threshold for systolic blood pressure", sBloodRecords.get(i).getTimestamp());
                    triggerAlert(alert);
                }

                // Checking for critical lower threshold
                if(sBloodRecords.get(i).getMeasurementValue() < 90) {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Passed critial lower threshold for systolic blood pressure", sBloodRecords.get(i).getTimestamp());
                    triggerAlert(alert);
                }
            }
        }

        // Diastolic blood pressure
        if(dBloodRecords.size() < 1) {
            System.out.println("Insufficient number of records to check for diastolic blood pressure!");
        }
        else {
            for(int i = 0; i < dBloodRecords.size(); i++)
            {
                if(i < dBloodRecords.size()-3)
                {
                // Checking if it's decreasing
                if((dBloodRecords.get(i).getMeasurementValue() - dBloodRecords.get(i+1).getMeasurementValue()) > 10 &&
                (dBloodRecords.get(i+1).getMeasurementValue() - dBloodRecords.get(i+2).getMeasurementValue()) > 10 &&
                (dBloodRecords.get(i+2).getMeasurementValue() - dBloodRecords.get(i+3).getMeasurementValue()) > 10)
                {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Decreasing trend in diastolic blood pressure", dBloodRecords.get(i+3).getTimestamp());
                    triggerAlert(alert);
                }

                // Checking if it's increasing
                if((dBloodRecords.get(i).getMeasurementValue() - dBloodRecords.get(i+1).getMeasurementValue()) < -10 &&
                (dBloodRecords.get(i+1).getMeasurementValue() - dBloodRecords.get(i+2).getMeasurementValue()) < -10 &&
                (dBloodRecords.get(i+2).getMeasurementValue() - dBloodRecords.get(i+3).getMeasurementValue()) < -10)
                {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Increasing trend in diastolic blood pressure", dBloodRecords.get(i+3).getTimestamp());
                    triggerAlert(alert);
                }
                }

                // Checking for critical upper threshold
                if(dBloodRecords.get(i).getMeasurementValue() > 120) {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Passed critial upper threshold for diastolic blood pressure", dBloodRecords.get(i).getTimestamp());
                    triggerAlert(alert);
                }

                // Checking for critical lower threshold
                if(dBloodRecords.get(i).getMeasurementValue() < 60) {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Passed critial lower threshold for diastolic blood pressure", dBloodRecords.get(i).getTimestamp());
                    triggerAlert(alert);
                }
            }
        }

        // Blood saturation data alerts
        List<PatientRecord> bSaturationRecords = dataStorage.getRecords("blood oxygen saturation", records);

        // The values we get from getMeasurementValue represent percentages, so 92 would be 92%.
        if(bSaturationRecords.size() < 1) {
            System.out.println("Insufficient number of records to check for blood oxygen saturation!");
        }
        else {
            for(int i = 0; i < bSaturationRecords.size(); i++)
            {
                if(i < bSaturationRecords.size() - 1)
                {
                    // Test for rapid drop
                    // We chose 600000 to be the value used to represent 10 minutes since in the given example
                    // in DataStorageTest, the value given for timestamp is in miliseconds, and 10 minutes are equal
                    // to 600000 miliseconds, and since the records are chronologically ordered, it is guaranteed that
                    // i + 1 must have either a higher or equal timestamp.
                    if(bSaturationRecords.get(i).getMeasurementValue() - bSaturationRecords.get(i+1).getMeasurementValue() > 5 &&
                    bSaturationRecords.get(i+1).getTimestamp() - bSaturationRecords.get(i).getTimestamp() > 6000000)
                    {
                        Alert alert = new Alert(Integer.toString(patient.getId()), "Passed test for rapid drop in blood oxygen saturation", bSaturationRecords.get(i+1).getTimestamp());
                        triggerAlert(alert);
                    }
                }

                // Test for low saturation
                if(bSaturationRecords.get(i).getMeasurementValue() < 92)
                {
                    Alert alert = new Alert(Integer.toString(patient.getId()), "Passed threshold for low blood oxygen saturation!", bSaturationRecords.get(i).getTimestamp());
                    triggerAlert(alert);
                }
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions.
     *
     * // Removed overly detailed explanation regarding alert completeness
     * // to adhere to concise, focused JavaDoc recommendations.
     *
     * @param alert The alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("Patient: " + alert.getPatientId() + "; Condition: " + alert.getCondition() + "; Time: " + alert.getTimestamp() + ";");
        // AlertManager.add(alert);
    }
}
