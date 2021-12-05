package com.bootcamp.demo.service;

import com.bootcamp.demo.model.User;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface IUserService {
    User createUser(final User user);
    User getUserById(final String userId);
    User getUserByEmail(final String email);
    void deleteUserById(final String userId);
    Set<User> getUsers();

}
