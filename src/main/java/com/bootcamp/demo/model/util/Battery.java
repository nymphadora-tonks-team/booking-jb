package com.bootcamp.demo.model.util;


import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Battery {
    private Double level;
    private BatteryStatus status;

    public Battery(Double level, BatteryStatus status) {
        this.level = requireNonNull(level);
        this.status = requireNonNull(status);
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public BatteryStatus getStatus() {
        return status;
    }

    public void setStatus(BatteryStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Battery)) return false;
        Battery battery = (Battery) o;
        return level.equals(battery.level) && status == battery.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, status);
    }

    @Override
    public String toString() {
        return "Battery{" +
                "level=" + level +
                ", status=" + status +
                '}';
    }
}
