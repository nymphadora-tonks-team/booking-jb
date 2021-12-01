package com.bootcamp.demo.restapi.exceptionhandler;

import java.util.Objects;

public class ApiError {

    private int status;
    private String message;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApiError apiError = (ApiError) o;
        return Objects.equals(this.getMessage(), apiError.getMessage()) && Objects.equals(this.getStatus(), apiError.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("ApiError{");
        sb.append("status=").append(this.getStatus());
        sb.append(", message='").append(this.getMessage());
        sb.append("}");

        return sb.toString();
    }
}

