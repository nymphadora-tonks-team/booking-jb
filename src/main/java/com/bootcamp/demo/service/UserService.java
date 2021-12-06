package com.bootcamp.demo.service;

import com.bootcamp.demo.model.User;
import com.bootcamp.demo.service.exception.ItemNotFoundException;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
            try {
                this.getUserByEmail(user.getEmail());
                throw new ServiceException("An account linked with this email already exists!");
            } catch (ItemNotFoundException e) {
                ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_USERS_PATH)
                        .document(user.getUserId())
                        .set(user);

                String updateTime = collectionApiFuture.get().getUpdateTime().toDate().toString();

                LOGGER.info("CREATE USER - service function completed successfully. Update time: {}", updateTime);
                return user;
            }


        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("CREATE USER - service function. Error message: {}", e.getMessage(), e);
            throw new ServiceException(e);
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
            throw new ServiceException(e);
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
                throw new ItemNotFoundException("User does not exist! userID = " + userId);
            }

            return user.toObject(User.class);

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("GET USER BY ID - service function. Error message: {}", e.getMessage(), e);
            throw new ServiceException(e);
        }
    }

    @Override
    public User getUserByEmail(final String email) {
        LOGGER.info(String.format("GET USER BY EMAIL - service function invoked. UserId = %s", email));

        try {
            List<QueryDocumentSnapshot> filtered = db.collection(COLLECTION_USERS_PATH)
                    .whereEqualTo("email", email)
                    .get().get()
                    .getDocuments();

            if (filtered.isEmpty()) {
                LOGGER.warn("GET USER BY EMAIL - user does not exist! Email = {}", email);
                throw new ItemNotFoundException("User does not exist! User email = " + email);
            }

            DocumentSnapshot user = filtered.get(0);

            return user.toObject(User.class);

        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("GET USER BY EMAIL - service function. Error message: {}", e.getMessage(), e);
            throw new ServiceException(e);
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
            throw new ServiceException(e);
        }
    }
}
