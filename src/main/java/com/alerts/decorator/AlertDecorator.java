package com.alerts.decorator;

import com.alerts.AlertInterface;

public abstract class AlertDecorator implements AlertInterface {
    protected AlertInterface alert;

    public AlertDecorator(AlertInterface alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }
}