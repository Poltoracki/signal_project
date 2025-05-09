package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            if (i < records.size() - 3) {
                // Check for decreasing trend
                if ((records.get(i).getMeasurementValue() - records.get(i + 1).getMeasurementValue()) > 10 &&
                    (records.get(i + 1).getMeasurementValue() - records.get(i + 2).getMeasurementValue()) > 10 &&
                    (records.get(i + 2).getMeasurementValue() - records.get(i + 3).getMeasurementValue()) > 10) {
                    return new Alert(Integer.toString(patient.getId()), "Decreasing trend in blood pressure", records.get(i + 3).getTimestamp());
                }

                // Check for increasing trend
                if ((records.get(i).getMeasurementValue() - records.get(i + 1).getMeasurementValue()) < -10 &&
                    (records.get(i + 1).getMeasurementValue() - records.get(i + 2).getMeasurementValue()) < -10 &&
                    (records.get(i + 2).getMeasurementValue() - records.get(i + 3).getMeasurementValue()) < -10) {
                    return new Alert(Integer.toString(patient.getId()), "Increasing trend in blood pressure", records.get(i + 3).getTimestamp());
                }
            }

            // Check for critical thresholds
            if (records.get(i).getMeasurementValue() > 180) {
                return new Alert(Integer.toString(patient.getId()), "Critical high blood pressure", records.get(i).getTimestamp());
            }
            if (records.get(i).getMeasurementValue() < 90) {
                return new Alert(Integer.toString(patient.getId()), "Critical low blood pressure", records.get(i).getTimestamp());
            }
        }
        return null; // No alert triggered
    }
}