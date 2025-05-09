package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public Alert checkAlert(Patient patient, List<PatientRecord> records) {
        //TODO IMPLEMENTATION
        return null;
    }
}