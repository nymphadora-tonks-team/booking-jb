package com.bootcamp.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    private long id;
    private long serialNumber;
    private long accountId;
    private Date startDate;
    private Date endDate;
    private PaymentStatus payment;

}
