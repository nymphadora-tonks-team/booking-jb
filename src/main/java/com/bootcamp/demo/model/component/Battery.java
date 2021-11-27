package com.bootcamp.demo.model.component;

import java.util.Objects;

public final class Battery {
    private Double level;
    private BatteryStatus status = BatteryStatus.UNKNOWN;

    public Battery() {
    }

    public Battery(Double level) {
        this.level = level;
        this.setStatus();
    }


    public Double getLevel() {
        return level;
    }

    public void setLevel(final Double level) {
        this.level = level;
        this.setStatus();
    }

    public BatteryStatus getStatus() {
        return status;
    }

    public void setStatus() {
        if (level >= 0 && level < 30) {
            this.status = BatteryStatus.LOW;
        } else if (level < 60) {
            this.status = BatteryStatus.MEDIUM;
        } else if (level <= 100) {
            this.status = BatteryStatus.HIGH;
        } else {
            this.status = BatteryStatus.UNKNOWN;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final var battery = (Battery) o;
        return Objects.equals(this.getLevel(), battery.getLevel())
                && this.getStatus() == battery.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLevel(), this.getStatus());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Battery{");
        sb.append("level=").append(this.getLevel());
        sb.append(", status=").append(this.getStatus()).append("}");
        return sb.toString();
    }
}
