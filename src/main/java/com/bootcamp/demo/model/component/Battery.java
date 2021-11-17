package com.bootcamp.demo.model.component;

import java.util.Objects;

public final class Battery {
    private Double level;
    private BatteryStatus status;

    public Double getLevel() {
        return level;
    }

    public void setLevel(final Double level) {
        if (level >= 0 && level < 30) {
            this.setStatus(BatteryStatus.LOW);
        } else if(level < 60) {
            this.setStatus(BatteryStatus.MEDIUM);
        } else {
            this.setStatus(BatteryStatus.HIGH);
        }
        this.level = level;
    }

    public BatteryStatus getStatus() {
        return status;
    }

    private void setStatus(final BatteryStatus status) {
        this.status = status;
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
