package com.alerts;

public class AlertManager {
    private static AlertManager instance;
    private int alertCount;

    private AlertManager() {
        alertCount = 0;
    }

    public static synchronized AlertManager getInstance() {
        if (instance == null) {
            instance = new AlertManager();
        }
        return instance;
    }

    public void triggerAlert(String message) {
        alertCount++;
        System.out.println("ALERT: " + message);
    }

    public int getAlertCount() {
        return alertCount;
    }
}