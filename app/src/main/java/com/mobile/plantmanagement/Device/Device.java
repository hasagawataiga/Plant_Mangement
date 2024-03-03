package com.mobile.plantmanagement.Device;

public class Device {
    private String name;
    private String status;
    private boolean isActive;

    public Device(String name, String status, boolean isActive) {
        this.name = name;
        this.status = status;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
