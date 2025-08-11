# 🎓 Student Record Management System (Java)
A **console-based application** written in Java that allows administrators and students to manage student academic records using **text files**. This project is designed for educational purposes to demonstrate **Java file handling**, **basic OOP concepts**, and **console-based menu-driven programs**.
## 🧠 Objective
To create a simple system where:
- Admins can add, update, and delete student records.
- Students can log in and view their details.
- All data is stored in local `.txt` files instead of a database.
## 🛠️ Technologies Used
- **Java (JDK 8+)**
- **File Handling (BufferedReader, FileWriter, etc.)**
- **OOP Concepts** – Classes, Methods, Objects
- **CLI-based Interface** (Console I/O)
## 📂 Project Structure
StudentRecordSystem/
│

├── StudentRecordSystem.java # Main Java source code

├── StudentRecordSystem.class # Compiled Java class

│

├── admin.txt # Admin credentials (username & password)

├── students.txt # Student records (name, roll, branch, marks)


## ✨ Features

### 🔐 Admin Functions
- Admin Login using credentials from `admin.txt`
- Add a new student record
- Delete an existing student record
- View all student records
- Logout option

### 👨‍🎓 Student Functions
- Student Login using name or roll number (based on your implementation)
- View their individual academic record
- Logout option

### 🧾 File Handling
- Records are stored persistently in:
  - `admin.txt` for admin login credentials
  - `students.txt` for storing student data
- Files are created/updated using Java's File I/O APIs.

## ⚙️ Functional Breakdown

### Class: `StudentRecordSystem`
Main class containing:

#### 🔸 `main(String[] args)`
- Entry point of the application.
- Displays the initial login menu (admin or student).

#### 🔸 `adminLogin()`
- Validates admin username and password from `admin.txt`.

#### 🔸 `studentLogin()`
- Checks student roll or name from `students.txt`.

#### 🔸 `addStudent()`
- Prompts admin to enter student details.
- Appends the new data to `students.txt`.

#### 🔸 `viewStudents()`
- Displays all records stored in `students.txt`.

#### 🔸 `deleteStudent()`
- Reads student file.
- Deletes record based on roll number.
- Rewrites updated data into the file.

#### 🔸 `viewMyRecord(String studentId)`
- Displays the specific student record from `students.txt`.


