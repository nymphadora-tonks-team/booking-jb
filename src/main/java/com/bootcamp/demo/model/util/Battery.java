package com.bootcamp.demo.model.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Battery {
    private Double level;
    private BatteryStatus status;
}
