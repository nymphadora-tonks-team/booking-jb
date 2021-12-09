package com.bootcamp.demo.service;

import com.bootcamp.demo.model.component.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentService {
    public PaymentStatus getPaymentStatus(){
        Random  rand = new Random();
        int randInt = rand.nextInt(9) + 1;
        if(randInt < 5){
            return PaymentStatus.FAILURE;
        }
        else {
            return PaymentStatus.SUCCESS;
        }
    }
}
