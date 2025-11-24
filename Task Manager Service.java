package com.vityarthi.task;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Functional Module 2: Core Business Logic (Controller/Service)

public class TaskManager {
    private List<Task> taskList;
    private static final String DATA_FILE = "task_data.ser"; // Persistence file (Functional Requirement)

    public TaskManager() {
        // Attempt to load data on initialization (Functional Module 3: Persistence)
        this.taskList = loadTasks();
    }

    // --- CRUD Operations (Functional Module 1) ---

    /**
     * Adds a new task to the system.
     */
    public void addTask(String title, String description, LocalDate dueDate, int priority) 
            throws InvalidInputException {
        if (title == null || title.trim().isEmpty() || dueDate == null) {
            throw new InvalidInputException("Task title and due date cannot be empty.");
        }
        Task newTask = new Task(title, description, dueDate, priority);
        taskList.add(newTask);
        System.out.println("✅ Task added successfully! ID: " + newTask.getId());
    }

    /**
     * Finds a task by its unique ID.
     */
    public Optional<Task> getTask(long id) {
        return taskList.stream()
            .filter(t -> t.getId() == id)
            .findFirst();
    }

    /**
     * Updates an existing task's status.
     */
    public void updateTaskStatus(long id, String newStatus) throws TaskNotFoundException {
        Optional<Task> taskOpt = getTask(id);
        if (taskOpt.isPresent()) {
            taskOpt.get().setStatus(newStatus);
            System.out.println("✅ Task " + id + " status updated to: " + newStatus);
        } else {
            throw new TaskNotFoundException("Task with ID " + id + " not found.");
        }
    }

    /**
     * Deletes a task by ID.
     */
    public void deleteTask(long id) throws TaskNotFoundException {
        boolean removed = taskList.removeIf(t -> t.getId() == id);
        if (!removed) {
            throw new TaskNotFoundException("Task with ID " + id + " not found for deletion.");
        }
        System.out.println("✅ Task " + id + " deleted successfully.");
    }

    // --- Reporting & Analytics (Functional Module 2) ---
    
    /**
     * Generates a report of all tasks.
     */
    public List<Task> getAllTasks() {
        // Sorting by Due Date and then Priority for better usability (NFR: Usability)
        return taskList.stream()
            .sorted((t1, t2) -> {
                int dateCompare = t1.getDueDate().compareTo(t2.getDueDate());
                if (dateCompare != 0) return dateCompare;
                return Integer.compare(t1.getPriority(), t2.getPriority());
            })
            .collect(Collectors.toList());
    }

    /**
     * Generates a report of overdue tasks.
     */
    public List<Task> getOverdueTasks() {
        // Functional Requirement: Reporting/Analytics
        return taskList.stream()
            .filter(Task::isOverdue)
            .collect(Collectors.toList());
    }
    
    /**
     * Generates a report of tasks due today or tomorrow.
     */
    public List<Task> getUrgentTasks() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return taskList.stream()
            .filter(t -> t.getStatus().equals(Task.STATUS_PENDING) && 
                         (t.getDueDate().isEqual(today) || t.getDueDate().isEqual(tomorrow)))
            .collect(Collectors.toList());
    }


    // --- Persistence (Functional Module 3) ---

    /**
     * Saves the current list of tasks to a file using Java Serialization.
     */
    public void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(taskList);
            System.out.println("\n[LOG] Data saved successfully to " + DATA_FILE);
            // Logging (NFR: Logging/Monitoring)
        } catch (IOException e) {
            System.err.println("\n[ERROR] Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads the list of tasks from the file.
     */
    @SuppressWarnings("unchecked") // Suppress warning for type casting readObject
    private List<Task> loadTasks() {
        File file = new File(DATA_FILE);
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                List<Task> loadedList = (List<Task>) ois.readObject();
                System.out.println("[LOG] Task data loaded successfully.");
                return loadedList;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("[ERROR] Failed to load tasks. Starting with an empty list. Details: " + e.getMessage());
                // Error handling (NFR: Reliability)
            }
        } else if (file.exists()) {
             System.out.println("[LOG] Data file exists but is empty. Starting with an empty list.");
        } else {
             System.out.println("[LOG] No previous data found. Starting fresh.");
        }
        return new ArrayList<>();
    }
}
