package com.vityarthi.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

// Application entry point (View) - handles user interaction (Functional Requirement: Logical Workflow)

public class MainApp {
    private final TaskManager taskManager;
    private final Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MainApp() {
        this.taskManager = new TaskManager();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Main application loop.
     */
    public void run() {
        System.out.println("==========================================");
        System.out.println("     TASKFORGE - TASK MANAGEMENT SYSTEM   ");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("\nEnter your choice (1-6): ");
            
            try {
                if (!scanner.hasNextInt()) {
                    System.err.println("❌ Invalid input. Please enter a number.");
                    scanner.next(); // Consume the invalid input
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1: addTask(); break;
                    case 2: viewTasks(); break;
                    case 3: updateTaskStatus(); break;
                    case 4: deleteTask(); break;
                    case 5: viewReports(); break;
                    case 6: 
                        taskManager.saveTasks(); // Save before exit
                        running = false; 
                        System.out.println("👋 Thank you for using TaskForge. Goodbye!"); 
                        break;
                    default: System.err.println("❌ Invalid choice. Please select a number between 1 and 6.");
                }
            } catch (Exception e) {
                System.err.println("💥 An unexpected error occurred: " + e.getMessage());
                // Ensures application stability (NFR: Reliability/Maintainability)
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Add New Task (Module 1)");
        System.out.println("2. View All Tasks (Module 1)");
        System.out.println("3. Update Task Status (Module 1)");
        System.out.println("4. Delete Task (Module 1)");
        System.out.println("5. View Reports (Module 2)");
        System.out.println("6. Exit & Save (Module 3)");
        System.out.println("-----------------");
    }

    // --- Menu Handlers ---

    private void addTask() {
        System.out.println("\n--- Add New Task ---");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description (Optional): ");
        String desc = scanner.nextLine();
        
        LocalDate dueDate = null;
        while (dueDate == null) {
            System.out.printf("Due Date (format %s): ", DATE_FORMATTER.toString());
            String dateStr = scanner.nextLine();
            try {
                dueDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.err.println("❌ Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        int priority = -1;
        while (priority < 1 || priority > 5) {
            System.out.print("Priority (1=High, 5=Low): ");
            if (scanner.hasNextInt()) {
                priority = scanner.nextInt();
            } else {
                System.err.println("❌ Invalid input. Must be a number.");
                scanner.next(); // Consume invalid input
            }
            scanner.nextLine(); // Consume newline
        }

        try {
            taskManager.addTask(title, desc, dueDate, priority);
        } catch (InvalidInputException e) {
            System.err.println("❌ ERROR: " + e.getMessage());
        }
    }

    private void viewTasks() {
        System.out.println("\n--- All Tasks (Sorted by Due Date/Priority) ---");
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("--- No tasks in the system. ---");
            return;
        }
        
        System.out.println("--------------------------------------------------------------------------------------------------");
        tasks.forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    private void updateTaskStatus() {
        System.out.println("\n--- Update Task Status ---");
        System.out.print("Enter Task ID to update: ");
        
        if (scanner.hasNextLong()) {
            long id = scanner.nextLong();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter New Status (Pending, In Progress, Complete): ");
            String status = scanner.nextLine().trim();

            try {
                taskManager.updateTaskStatus(id, status);
            } catch (TaskNotFoundException e) {
                System.err.println("❌ ERROR: " + e.getMessage());
            }
        } else {
            System.err.println("❌ Invalid input for Task ID.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    private void deleteTask() {
        System.out.println("\n--- Delete Task ---");
        System.out.print("Enter Task ID to delete: ");
        
        if (scanner.hasNextLong()) {
            long id = scanner.nextLong();
            scanner.nextLine(); // Consume newline
            try {
                taskManager.deleteTask(id);
            } catch (TaskNotFoundException e) {
                System.err.println("❌ ERROR: " + e.getMessage());
            }
        } else {
            System.err.println("❌ Invalid input for Task ID.");
            scanner.nextLine(); // Consume invalid input
        }
    }
    
    private void viewReports() {
        System.out.println("\n--- Task Reports ---");
        System.out.println("1. View Overdue Tasks");
        System.out.println("2. View Urgent Tasks (Due Today/Tomorrow)");
        System.out.print("Select report type (1 or 2): ");
        
        if (scanner.hasNextInt()) {
            int reportChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            List<Task> reportTasks;
            String reportTitle;

            if (reportChoice == 1) {
                reportTasks = taskManager.getOverdueTasks();
                reportTitle = "Overdue Tasks";
            } else if (reportChoice == 2) {
                reportTasks = taskManager.getUrgentTasks();
                reportTitle = "Urgent Tasks";
            } else {
                System.err.println("❌ Invalid report choice.");
                return;
            }

            System.out.printf("\n--- %s ---\n", reportTitle);
            if (reportTasks.isEmpty()) {
                System.out.printf("--- No %s found. ---\n", reportTitle.toLowerCase());
            } else {
                System.out.println("--------------------------------------------------------------------------------------------------");
                reportTasks.forEach(System.out::println);
                System.out.println("--------------------------------------------------------------------------------------------------");
            }
        } else {
            System.err.println("❌ Invalid input for report type.");
            scanner.nextLine(); // Consume invalid input
        }
    }

    public static void main(String[] args) {
        // Entry point for the application
        MainApp app = new MainApp();
        app.run();
    }
}
