package com.alerts;

import java.util.LinkedList;

public class AlertManager 
{
    private int alertCount;
    private LinkedList<Alert> alerts;

    public AlertManager()
    {
        alerts = new LinkedList<Alert>();
        alertCount = 0;
    }

    public void addAlert(Alert alert)
    {
        alerts.add(alert);
        alertCount++;
    }

    public int getAlertCount() {
        return alertCount;
    }
}