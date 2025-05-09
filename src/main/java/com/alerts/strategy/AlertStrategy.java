package com.alerts.strategy;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public interface AlertStrategy {
    Alert checkAlert(Patient patient, List<PatientRecord> records);
}