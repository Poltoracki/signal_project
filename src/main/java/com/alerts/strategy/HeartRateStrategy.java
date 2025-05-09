package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, List<PatientRecord> records) {
        double sum = 0;
        for (PatientRecord record : records) {
            sum += record.getMeasurementValue();
        }
        double average = sum / records.size();

        for (PatientRecord record : records) {
            if (record.getMeasurementValue() > 1.5 * average) {
                return new Alert(Integer.toString(patient.getId()), "Abnormally high heart rate", record.getTimestamp());
            }
        }
        return null; // No alert triggered
    }
}