package com.alerts;

import java.util.List;

import com.alerts.strategy.AlertContext;
import com.alerts.strategy.BloodPressureStrategy;
import com.alerts.strategy.HeartRateStrategy;
import com.alerts.strategy.OxygenSaturationStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private final DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     *
     * @param dataStorage The data storage system that provides access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions are met.
     * If a condition is met, an alert is triggered via the {@link #triggerAlert(Alert)} method.
     *
     * @param patient   The patient data to evaluate for alert conditions
     * @param startTime The start time for the data evaluation period
     * @param endTime   The end time for the data evaluation period
     */
    public void evaluateData(Patient patient, long startTime, long endTime) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), startTime, endTime);

        AlertContext context = new AlertContext();

        // Blood Pressure Strategy
        context.setStrategy(new BloodPressureStrategy());
        List<PatientRecord> bpRecords = dataStorage.getRecords("blood pressure", records);
        Alert bpAlert = context.executeStrategy(patient, bpRecords);
        if (bpAlert != null) {
            triggerAlert(bpAlert);
        }

        // Heart Rate Strategy
        context.setStrategy(new HeartRateStrategy());
        List<PatientRecord> hrRecords = dataStorage.getRecords("heart rate", records);
        Alert hrAlert = context.executeStrategy(patient, hrRecords);
        if (hrAlert != null) {
            triggerAlert(hrAlert);
        }

        // Oxygen Saturation Strategy
        context.setStrategy(new OxygenSaturationStrategy());
        List<PatientRecord> osRecords = dataStorage.getRecords("oxygen saturation", records);
        Alert osAlert = context.executeStrategy(patient, osRecords);
        if (osAlert != null) {
            triggerAlert(osAlert);
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions.
     *
     * @param alert The alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("Patient: " + alert.getPatientId() + "; Condition: " + alert.getCondition() + "; Time: " + alert.getTimestamp());
    }
}
