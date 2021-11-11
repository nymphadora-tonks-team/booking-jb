package com.bootcamp.demo.service.assembler;

import com.bootcamp.demo.model.Scooter;
import com.google.cloud.firestore.QueryDocumentSnapshot;

public class ScooterAssembler {

    public static Scooter documentToModel(QueryDocumentSnapshot qdc) {
        return qdc.toObject(Scooter.class);
    }
}
