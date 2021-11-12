package com.bootcamp.demo;

import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void init() throws IOException {
        final var serviceAccount = new ByteArrayInputStream(getProperty("firebaseKey").getBytes(UTF_8));

        final var options = FirebaseOptions.builder()
                .setCredentials(fromStream(serviceAccount))
                .build();

        initializeApp(options);
    }
}
