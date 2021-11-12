package com.bootcamp.demo.models;

import java.util.Objects;

public final class User {
    private final Long userId;
    private String email;
    private String firstName;
    private String lastName;

    public User(Long userId, String email, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getUserId() {
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

    public User setUserId(Long id) {
        return new User(id, email, firstName, lastName);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return Objects.equals(userId, other.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
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
