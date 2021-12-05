package com.bootcamp.demo.service;

import com.bootcamp.demo.model.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


// #TODO : change return null to throwing a service exception


@Service
public class UserService implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String COLLECTION_USERS_PATH = "bookings/databases/users";
    private final Firestore db;

    public UserService(Firestore db) {
        this.db = db;
    }

    @Override
    public User createUser(final User user) {
        LOGGER.info("CREATE USER - service function invoked: user = {}", user.toString());

        try {
            ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_USERS_PATH)
                    .document(user.getUserId())
                    .set(user);

            String updateTime = collectionApiFuture.get().getUpdateTime().toDate().toString();

            LOGGER.info("CREATE USER - service function completed successfully. Update time: {}", updateTime);
            return user;

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("CREATE USER - service function. Error message: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void deleteUserById(String userId) {
        LOGGER.info("DELETE USER BY ID - service function invoked: user = {}", userId);

        try {
            DocumentSnapshot user = db.collection(COLLECTION_USERS_PATH)
                    .document(userId)
                    .get().get();

            if (!user.exists()) {
                LOGGER.warn("DELETE USER BY ID - User does not exit! UserId = {}", userId);
            } else {

                ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_USERS_PATH).
                        document(userId).delete();

                String updateTime = collectionApiFuture.get().getUpdateTime().toDate().toString();


                LOGGER.info("DELETE USER BY ID - service function completed successfully. Update time: {}", updateTime);
            }


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(final String userId) {
        LOGGER.info(String.format("FIND USER BY ID - service function invoked. UserId = %s", userId));

        try {
            DocumentSnapshot user = db.collection(COLLECTION_USERS_PATH)
                    .document(userId)
                    .get()
                    .get();

            if (!user.exists()) {
                LOGGER.warn("FIND USER BY ID - user does not exist! userId = {}", userId);
                return null;
            }

            return user.toObject(User.class);

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("GET USER BY ID - service function. Error message: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getUserByEmail(final String email) {
        LOGGER.info(String.format("FIND USER BY EMAIL - service function invoked. UserId = %s", email));

        try {
            List<QueryDocumentSnapshot> filtered = db.collection(COLLECTION_USERS_PATH)
                    .whereEqualTo("email", email)
                    .get().get()
                    .getDocuments();

            if (filtered.isEmpty()) {
                LOGGER.warn("FIND USER BY EMAIL - user does not exist! userId = {}", email);
                return null;
            }

            DocumentSnapshot user = filtered.get(0);

            return user.toObject(User.class);

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("GET USER BY EMAIL - service function. Error message: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Set<User> getUsers() {
        LOGGER.info("GET USERS - service function invoked");

        try {
            List<QueryDocumentSnapshot> users = db.collection(COLLECTION_USERS_PATH)
                    .get()
                    .get()
                    .getDocuments();

            return StreamSupport.stream(users.spliterator(), false)
                    .map(qds -> qds.toObject(User.class))
                    .collect(Collectors.toSet());

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("GET USERS - service function. Error message: {}", e.getMessage(), e);
            return null;
        }
    }
}
