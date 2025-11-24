package com.vityarthi.task;

// A module dedicated to custom exceptions for robust error handling (NFR: Error Handling)

/**
 * Thrown when a task is not found based on the provided ID.
 */
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String message) {
        super(message);
    }
}

/**
 * Thrown when required input fields (like title or date) are empty or null.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
