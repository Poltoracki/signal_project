package com.alerts.decorator;

import com.alerts.AlertInterface;

public class PriorityAlertDecorator extends AlertDecorator {
    private String priorityLevel;

    public PriorityAlertDecorator(AlertInterface alert, String priorityLevel) {
        super(alert);
        this.priorityLevel = priorityLevel;
    }

    @Override
    public String getCondition() {
        return "[Priority: " + priorityLevel + "] " + super.getCondition();
    }
}