import java.io.*;
import java.util.*;

class Student {
    String rollNo, name, className, course, mobileNo, admissionYear;

    public Student(String rollNo, String name, String className, String course, String mobileNo, String admissionYear) {
        this.rollNo = rollNo.trim();
        this.name = name.trim();
        this.className = className.trim();
        this.course = course.trim();
        this.mobileNo = mobileNo.trim();
        this.admissionYear = admissionYear.trim();
    }
}

public class StudentRecordSystem {
    static List<Student> students = new ArrayList<>();
    static final String STUDENT_FILE = "students.txt";
    static final String ADMIN_FILE = "admin.txt";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) 
    {
        if (!loadAdminCredentials()) return;
        loadStudentData();

        while (true) {
            clearScreen();
            showMenu();
            int choice = getIntInput(">>> Enter your choice (1-6): ");
            switch (choice) {
                case 1 -> enterStudent();
                case 2 -> showStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteRecord();
                case 6 -> {
                    System.out.print("Are you sure you want to exit? (y/n): ");
                    if (sc.nextLine().equalsIgnoreCase("y")) {
                        System.out.println("Thank you for using the Student Record Management System!");
                        return;
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
            System.out.println("Press Enter to continue...");
            sc.nextLine();
        }
    }

    // Admin Login Setup
    static boolean loadAdminCredentials() {
        File file = new File(ADMIN_FILE);
        try {
            if (!file.exists()) {
                System.out.println("No admin credentials found. Please set up your admin account.");
                System.out.print("Set admin username: ");
                String user = sc.nextLine();
                System.out.print("Set admin password: ");
                String pass = sc.nextLine();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw.write(user + "\n" + pass + "\n");
                }
                System.out.println("Admin account created! Please restart the program.");
                return false;
            }

            List<String> creds = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                creds.add(br.readLine());
                creds.add(br.readLine());
            }

            int attempts = 0;
            while (attempts < 3) {
                System.out.println("Admin Login");
                System.out.print("Username: ");
                String user = sc.nextLine();
                System.out.print("Password: ");
                String pass = sc.nextLine();
                if (user.equals(creds.get(0)) && pass.equals(creds.get(1))) {
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Invalid credentials. Try again.");
                    attempts++;
                }
            }
            System.out.println("Too many failed attempts. Exiting.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    static void loadStudentData() {
        students.clear();
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String roll = line;
                String name = br.readLine();
                String cls = br.readLine();
                String course = br.readLine();
                String mobile = br.readLine();
                String year = br.readLine();
                students.add(new Student(roll, name, cls, course, mobile, year));
            }
        } catch (IOException e) {
            System.out.println("Error loading student data.");
        }
    }

    static void saveStudentData() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : students) {
                bw.write(s.rollNo + "\n" + s.name + "\n" + s.className + "\n" +
                         s.course + "\n" + s.mobileNo + "\n" + s.admissionYear + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving student data.");
        }
    }

    static void enterStudent() {
        int count = getIntInput(">>> How many students to enter? ");
        for (int i = 0; i < count; i++) {
            System.out.println("Student " + (students.size() + 1));
            String roll;
            while (true) {
                System.out.print("Enter Roll No: ");
                roll = sc.nextLine().trim();
                if (roll.isEmpty()) System.out.println("Cannot be empty.");
                else if (isDuplicateRoll(roll)) System.out.println("Duplicate roll no.");
                else break;
            }
            String name = getNonEmptyInput("Enter Name: ");
            String cls = getNonEmptyInput("Enter Class/Section: ");
            String course = getNonEmptyInput("Enter Course: ");
            String phone;
            while (true) {
                System.out.print("Enter Mobile No (10 digits): ");
                phone = sc.nextLine();
                if (phone.matches("\\d{10}")) break;
                else System.out.println("Invalid number.");
            }
            String year;
            while (true) {
                System.out.print("Enter Admission Year (4 digits): ");
                year = sc.nextLine();
                if (year.matches("\\d{4}")) break;
                else System.out.println("Invalid year.");
            }
            students.add(new Student(roll, name, cls, course, phone, year));
        }
        saveStudentData();
    }

    static void showStudents() {
        if (students.isEmpty()) {
            System.out.println("No student data available.");
            return;
        }
        int i = 1;
        for (Student s : students) {
            System.out.println("\nStudent " + (i++));
            printStudent(s);
        }
    }

    static void searchStudent() {
        System.out.print("Enter roll no to search: ");
        String roll = sc.nextLine().trim();
        for (Student s : students) {
            if (s.rollNo.equals(roll)) {
                printStudent(s);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    static void updateStudent() {
        System.out.print("Enter roll no to update: ");
        String roll = sc.nextLine().trim();
        for (Student s : students) {
            if (s.rollNo.equals(roll)) {
                System.out.println("Current Data:");
                printStudent(s);
                System.out.print("Update this record? (y/n): ");
                if (!sc.nextLine().equalsIgnoreCase("y")) return;

                String newRoll;
                while (true) {
                    System.out.print("New Roll No: ");
                    newRoll = sc.nextLine().trim();
                    if (newRoll.isEmpty()) System.out.println("Cannot be empty.");
                    else if (!newRoll.equals(s.rollNo) && isDuplicateRoll(newRoll)) System.out.println("Duplicate.");
                    else break;
                }
                s.rollNo = newRoll;
                s.name = getNonEmptyInput("New Name: ");
                s.className = getNonEmptyInput("New Class/Section: ");
                s.course = getNonEmptyInput("New Course: ");
                while (true) {
                    System.out.print("New Mobile No (10 digits): ");
                    s.mobileNo = sc.nextLine();
                    if (s.mobileNo.matches("\\d{10}")) break;
                    else System.out.println("Invalid number.");
                }
                while (true) {
                    System.out.print("New Admission Year (4 digits): ");
                    s.admissionYear = sc.nextLine();
                    if (s.admissionYear.matches("\\d{4}")) break;
                    else System.out.println("Invalid year.");
                }
                saveStudentData();
                System.out.println("Record updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    static void deleteRecord() {
        System.out.println("1. Delete all records");
        System.out.println("2. Delete by roll no");
        int ch = getIntInput("Enter your choice: ");
        if (ch == 1) {
            System.out.print("Are you sure? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                students.clear();
                saveStudentData();
                System.out.println("All records deleted.");
            }
        } else if (ch == 2) {
            System.out.print("Enter roll no to delete: ");
            String roll = sc.nextLine();
            boolean removed = students.removeIf(s -> s.rollNo.equals(roll));
            if (removed) {
                saveStudentData();
                System.out.println("Record deleted.");
            } else {
                System.out.println("Student not found.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    // Utilities
    static void printStudent(Student s) {
        System.out.println("Roll No: " + s.rollNo);
        System.out.println("Name: " + s.name);
        System.out.println("Class/Section: " + s.className);
        System.out.println("Course: " + s.course);
        System.out.println("Mobile No: " + s.mobileNo);
        System.out.println("Admission Year: " + s.admissionYear);
    }

    static boolean isDuplicateRoll(String roll) {
        return students.stream().anyMatch(s -> s.rollNo.equals(roll));
    }

    static String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) System.out.println("Input cannot be empty.");
        } while (input.isEmpty());
        return input;
    }

    static int getIntInput(String prompt) {
        int val;
        while (true) {
            System.out.print(prompt);
            try {
                val = Integer.parseInt(sc.nextLine());
                if (val > 0) return val;
                else System.out.println("Must be positive.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }

    static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                System.out.print("\033[H\033[2J");
        } catch (Exception ignored) {}
    }

    static void showMenu() {
        System.out.println("\n======= Student Record Management =======");
        System.out.println("1. Enter data");
        System.out.println("2. Show data");
        System.out.println("3. Search data");
        System.out.println("4. Update data");
        System.out.println("5. Delete data");
        System.out.println("6. Quit");
        System.out.println("=========================================");
    }
}
