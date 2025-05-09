package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class AlertContext {
    private AlertStrategy strategy;

    public void setStrategy(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    public Alert executeStrategy(Patient patient, List<PatientRecord> records) {
        if (strategy == null) {
            throw new IllegalStateException("Alert strategy isnt set, dumbass");
        }
        return strategy.checkAlert(patient, records);
    }
}