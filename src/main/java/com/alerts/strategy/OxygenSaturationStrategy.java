package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            if (i < records.size() - 1) {
                // Check for rapid drop
                if (records.get(i).getMeasurementValue() - records.get(i + 1).getMeasurementValue() > 5 &&
                    records.get(i + 1).getTimestamp() - records.get(i).getTimestamp() > 600000) {
                    return new Alert(Integer.toString(patient.getId()), "Rapid drop in oxygen saturation", records.get(i + 1).getTimestamp());
                }
            }

            // Check for low saturation
            if (records.get(i).getMeasurementValue() < 92) {
                return new Alert(Integer.toString(patient.getId()), "Low oxygen saturation", records.get(i).getTimestamp());
            }
        }
        return null; // No alert triggered
    }
}