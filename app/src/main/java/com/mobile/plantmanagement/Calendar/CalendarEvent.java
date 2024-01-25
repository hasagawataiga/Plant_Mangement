package com.mobile.plantmanagement.Calendar;

public class CalendarEvent {
    private String name;
    private String amount;
    private String unit;
    public CalendarEvent(){}
    public CalendarEvent(String events, String amount) {
        this.name = events;
        splitString(amount);
    }

    private void splitString(String str){
        String[] arrOfStr = str.split("@", 0);
        if(arrOfStr.length == 2){
            this.amount = arrOfStr[0];
            this.unit = arrOfStr[1];
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
