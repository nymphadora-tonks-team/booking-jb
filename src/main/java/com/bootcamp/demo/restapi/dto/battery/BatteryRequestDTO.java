package com.bootcamp.demo.restapi.dto.battery;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class BatteryRequestDTO {

    @NotNull(message = "Please provide the battery level")
    @Min(value = 0, message = "The battery level minimum value should be 0 ")
    @Max(value = 100, message = "The battery level maximum value should be 100")
    private Double level;

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var battery = (BatteryRequestDTO) o;
        return Objects.equals(this.getLevel(), battery.getLevel());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(level);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BatteryRequestDTO{");
        sb.append("level=").append(this.getLevel());
        sb.append("}");
        return sb.toString();
    }
}
