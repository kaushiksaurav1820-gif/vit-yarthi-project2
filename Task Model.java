package com.vityarthi.task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

// Functional Module 1: Task Data Structure (Model)
// Implements Serializable for persistence (File I/O)

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicLong counter = new AtomicLong(1);
    
    private final long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status; // Pending, In Progress, Complete
    private int priority; // 1 (High) to 5 (Low)

    // Enum for Status might be cleaner, but using String for simpler CLI implementation
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_COMPLETE = "Complete";

    /**
     * Constructor for creating a new Task.
     * @param title The task title.
     * @param description The task description.
     * @param dueDate The task due date.
     * @param priority The task priority (1-5).
     */
    public Task(String title, String description, LocalDate dueDate, int priority) {
        this.id = counter.getAndIncrement();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = STATUS_PENDING; // Default status
    }

    // --- Getters ---
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    public int getPriority() { return priority; }

    // --- Setters (for updates) ---
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(int priority) { this.priority = priority; }

    /**
     * Checks if the task is overdue.
     * @return true if the due date is before today and the task is not complete.
     */
    public boolean isOverdue() {
        return !status.equals(STATUS_COMPLETE) && dueDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format(
            "| ID: %-3d | Title: %-25s | Due Date: %-10s | Priority: %-2d | Status: %-12s |",
            id, title, dueDate, priority, status
        );
    }
}
