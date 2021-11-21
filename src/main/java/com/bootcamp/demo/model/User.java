package com.bootcamp.demo.model;

import java.util.Objects;
import java.util.UUID;

public final class User {
    private final UUID userId;
    private String email;
    private String firstName;
    private String lastName;

    public User(String email, String firstName, String lastName) {
        this.userId = UUID.randomUUID();
        this.email = Objects.requireNonNull(email);
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
    }

    public User(UUID userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = Objects.requireNonNull(email);
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public User setUserId(final UUID id) {
        return new User(id, email, firstName, lastName);
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(userId, other.userId) || Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("User[");
        builder.append("userId=").append(userId).append(',');
        builder.append("email=").append(email).append(',');
        builder.append("firstName=").append(firstName).append(',');
        builder.append("lastName=").append(lastName).append("]");
        return builder.toString();
    }
}
