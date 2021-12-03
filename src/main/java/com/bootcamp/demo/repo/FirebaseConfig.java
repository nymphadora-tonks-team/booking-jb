package com.bootcamp.demo.repo;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        final var serviceAccount = new ByteArrayInputStream(getProperty("firebaseKey").getBytes(UTF_8));

        final var options = FirebaseOptions.builder()
                .setCredentials(fromStream(serviceAccount))
                .build();

        initializeApp(options);
    }

    @Bean
    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
