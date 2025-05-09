package com.alerts.decorator;

import com.alerts.AlertInterface;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int repeatCount;

    public RepeatedAlertDecorator(AlertInterface alert, int repeatCount) {
        super(alert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String getCondition() {
        return super.getCondition() + " (Repeated " + repeatCount + " times)";
    }
}