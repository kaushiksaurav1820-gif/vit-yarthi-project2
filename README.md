VITyarthi Project: TaskForge - A Modular Task Management System

🌟 Overview

TaskForge is a console-based application designed to help users efficiently manage their daily tasks, projects, and deadlines. It implements core Object-Oriented Programming (OOP) principles and follows a clean, modular architecture (Model-View-Controller/Service pattern) to ensure maintainability and scalability. This project directly applies concepts like data structures (Lists, Maps), custom exception handling, and encapsulation, relevant to core Java coursework.

✨ Features

This system is organized into three major functional modules:

Task Management Module: Create, view, update, and delete tasks (CRUD operations).

Reporting Module: Generate aggregated reports (e.g., tasks completed today, tasks overdue).

Persistence Module: Save and load task data to and from a local file (simulating real-world persistence).

🛠️ Technologies & Tools Used

Language: Java 17+

Tools: Standard Java Development Kit (JDK)

Concepts: OOP, Encapsulation, Custom Exception Handling, File I/O (Serialization/Deserialization).

Version Control: Git

⚙️ Steps to Install & Run the Project

Prerequisites

Java Development Kit (JDK) 17 or higher installed.

Execution Steps

Clone the Repository:

git clone [Your-Repo-Link-Here]
cd taskforge


Compile the Java Files:
Assuming the files are in src/com/vityarthi/task/:

javac src/com/vityarthi/task/*.java -d bin


Run the Main Application:

java -cp bin com.vityarthi.task.MainApp


🧪 Instructions for Testing

Basic validation tests are integrated into the TaskManager module, specifically for handling null or empty inputs.

To manually test the functionality:

Run the application using the steps above.

Test the main commands provided in the menu (1-6).

Specifically, test the persistence by:
a. Adding a few tasks (Option 1).
b. Exiting the application (Option 6).
c. Running the application again. The previously added tasks should be re-loaded.

Test error handling by trying to enter invalid dates or non-existent Task IDs.
