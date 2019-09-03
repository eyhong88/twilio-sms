package com.example.twiliosms;

public enum ActionTypeEnum {
    CLOCK_IN("CLOCK IN"),
    CLOCK_OUT("CLOCK OUT"),
    MEAL_IN("MEAL IN"),
    MEAL_OUT("MEAL OUT");

    private String actionType;

    ActionTypeEnum(String actionType){
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
}
