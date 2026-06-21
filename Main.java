import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;

// ================= COURSE CLASS =================
class Course {

    private String courseCode;
    private String courseTitle;
    private int courseCapacity;
    private int registeredStudentCount;

    public Course(String courseCode, String courseTitle, int courseCapacity) {
        if(courseCapacity <= 0){
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.courseCapacity = courseCapacity;
        this.registeredStudentCount = 0;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseTitle() { return courseTitle; }
    public int getCourseCapacity() { return courseCapacity; }
    public int getRegisteredStudentCount() { return registeredStudentCount; }

    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }

    public void setCourseCapacity(int courseCapacity) {
        if(courseCapacity < registeredStudentCount){
            System.out.println("Cannot set capacity below registered students!");
            return;
        }
        this.courseCapacity = courseCapacity;
    }

    public void setRegisteredStudentCount(int count){
        this.registeredStudentCount = count;
    }

    public void registerStudent() {
        if (registeredStudentCount < courseCapacity) {
            registeredStudentCount++;
            System.out.println("Student Registered Successfully!");
        } else {
            System.out.println("Course Capacity Full!");
        }
    }

    public void displayCourse() {
        System.out.println("Course Code: " + courseCode);
        System.out.println("Course Title: " + courseTitle);
        System.out.println("Capacity: " + courseCapacity);
        System.out.println("Registered: " + registeredStudentCount);
        System.out.println("--------------------------------");
    }
}

// ================= STUDENT CLASS =================
class Student {

    private String studentName;
    private String studentId;

    public Student(String studentName, String studentId) {
        this.studentName = studentName;
        this.studentId = studentId;
    }

    public void displayStudent() {
        System.out.println("Name: " + studentName);
        System.out.println("ID: " + studentId);
    }
}

// ================= USER BASE CLASS =================
class User {

    protected String name;
    protected String userId;

    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public void displayUserInfo() {
        System.out.println("Name: " + name);
        System.out.println("ID: " + userId);
    }
}

// ================= STUDENT USER =================
class StudentUser extends User {

    private ArrayList<String> registeredCourses = new ArrayList<>();

    public StudentUser(String name, String userId) {
        super(name, userId);
    }

    public void registerCourse(String courseCode) {
        if(registeredCourses.contains(courseCode)){
            System.out.println("Already Registered!");
            return;
        }
        registeredCourses.add(courseCode);
        System.out.println("Course Registered!");
    }

    public void showCourses() {
        if(registeredCourses.isEmpty()){
            System.out.println("No courses registered.");
            return;
        }
        for(String c : registeredCourses){
            System.out.println(c);
        }
    }
}

// ================= ADMIN USER =================
class AdminUser extends User {

    public AdminUser(String name, String userId) {
        super(name, userId);
    }

    public void addCourse(RegistrationSystem system, Course course) {
        system.addCourse(course);
    }

    public void deleteCourse(RegistrationSystem system, String code) {
        system.deleteCourse(code);
    }

    public void updateCourse(RegistrationSystem system, String code, String title, int cap) {
        system.updateCourse(code, title, cap);
    }
}

// ================= LOGIN SYSTEM =================
class LoginSystem {

    // ADMIN LOGIN (fixed version)
    public static boolean adminLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    // STUDENT LOGIN (fixed validation)
    public static boolean studentLogin(String studentId, String password) {

        if (studentId == null || password == null) return false;

        // password rule
        if (password.length() < 6) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) hasLetter = true;
            if (Character.isDigit(ch)) hasDigit = true;
        }

        if (!(hasLetter && hasDigit)) return false;

        // ID rule: UAP + year check
        if (!studentId.startsWith("UAP")) return false;

        try {
            String yearStr = studentId.substring(3, 5);
            int studentYear = Integer.parseInt(yearStr);
            int currentYear = LocalDate.now().getYear() % 100;

            if (studentYear > currentYear) return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}

// ================= REGISTRATION SYSTEM =================
class RegistrationSystem {

    ArrayList<Course> courses = new ArrayList<>();

    // ADD COURSE (no duplicate allowed)
    public void addCourse(Course course) {

        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(course.getCourseCode())) {
                System.out.println("Course Already Exists!");
                return;
            }
        }

        courses.add(course);
        System.out.println("Course Added Successfully!");
    }

    // SHOW ALL
    public void displayAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No Courses Available!");
            return;
        }

        for (Course c : courses) {
            c.displayCourse();
        }
    }

    // SEARCH
    public void searchCourse(String code) {
        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                c.displayCourse();
                return;
            }
        }
        System.out.println("Course Not Found!");
    }

    // UPDATE
    public void updateCourse(String code, String title, int cap) {

        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {

                if (cap < c.getRegisteredStudentCount()) {
                    System.out.println("Cannot reduce capacity below registered students!");
                    return;
                }

                c.setCourseTitle(title);
                c.setCourseCapacity(cap);
                System.out.println("Course Updated Successfully!");
                return;
            }
        }

        System.out.println("Course Not Found!");
    }

    // DELETE
    public void deleteCourse(String code) {

        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {
                courses.remove(c);
                System.out.println("Course Deleted Successfully!");
                return;
            }
        }

        System.out.println("Course Not Found!");
    }

    // REGISTER STUDENT TO COURSE
    public void registerStudent(String code, StudentUser student) {

        for (Course c : courses) {
            if (c.getCourseCode().equalsIgnoreCase(code)) {

                c.registerStudent();
                student.registerCourse(code);
                return;
            }
        }

        System.out.println("Course Not Found!");
    }
}

// ================= FILE HANDLING =================
class DataManager {

    // SAVE
    public static void saveCourses(ArrayList<Course> courses) {

        try {
            FileWriter writer = new FileWriter("courses.txt");

            for (Course c : courses) {
                writer.write(
                        c.getCourseCode() + "," +
                                c.getCourseTitle() + "," +
                                c.getCourseCapacity() + "," +
                                c.getRegisteredStudentCount() + "\n"
                );
            }

            writer.close();
            System.out.println("Data Saved Successfully!");

        } catch (IOException e) {
            System.out.println("File Write Error!");
        }
    }

    // LOAD
    public static void loadCourses(ArrayList<Course> courses) {

        try {
            File file = new File("courses.txt");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                String[] data = sc.nextLine().split(",");

                String code = data[0];
                String title = data[1];
                int cap = Integer.parseInt(data[2]);
                int registered = Integer.parseInt(data[3]);

                Course c = new Course(code, title, cap);
                c.setRegisteredStudentCount(registered);

                courses.add(c);
            }

            sc.close();
            System.out.println("Data Loaded Successfully!");

        } catch (Exception e) {
            System.out.println("File Read Error!");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        RegistrationSystem system = new RegistrationSystem();

        // load previous data
        DataManager.loadCourses(system.courses);

        System.out.println("==================================");
        System.out.println("   COURSE REGISTRATION SYSTEM");
        System.out.println("==================================");

        System.out.println("1. Admin Login");
        System.out.println("2. Student Login");
        System.out.print("Choose: ");

        int role = input.nextInt();
        input.nextLine();

        // ================= ADMIN LOGIN =================
        if (role == 1) {

            System.out.print("Admin Username: ");
            String username = input.nextLine();

            System.out.print("Admin Password: ");
            String password = input.nextLine();

            if (!LoginSystem.adminLogin(username, password)) {
                System.out.println("Access Denied!");
                return;
            }

            AdminUser admin = new AdminUser("Admin", "ADM01");

            int choice;

            do {
                System.out.println("\n===== ADMIN MENU =====");
                System.out.println("1. Add Course");
                System.out.println("2. Show Courses");
                System.out.println("3. Search Course");
                System.out.println("4. Update Course");
                System.out.println("5. Delete Course");
                System.out.println("6. Save & Exit");

                System.out.print("Enter choice: ");
                choice = input.nextInt();
                input.nextLine();

                switch (choice) {

                    case 1:
                        System.out.print("Code: ");
                        String code = input.nextLine();

                        System.out.print("Title: ");
                        String title = input.nextLine();

                        System.out.print("Capacity: ");
                        int cap = input.nextInt();
                        input.nextLine();

                        admin.addCourse(system, new Course(code, title, cap));
                        break;

                    case 2:
                        system.displayAllCourses();
                        break;

                    case 3:
                        System.out.print("Code: ");
                        system.searchCourse(input.nextLine());
                        break;

                    case 4:
                        System.out.print("Code: ");
                        String ucode = input.nextLine();

                        System.out.print("New Title: ");
                        String ntitle = input.nextLine();

                        System.out.print("New Capacity: ");
                        int ncap = input.nextInt();
                        input.nextLine();

                        admin.updateCourse(system, ucode, ntitle, ncap);
                        break;

                    case 5:
                        System.out.print("Code: ");
                        admin.deleteCourse(system, input.nextLine());
                        break;

                    case 6:
                        DataManager.saveCourses(system.courses);
                        System.out.println("Admin Exit Successful!");
                        break;

                    default:
                        System.out.println("Invalid Choice!");
                }

            } while (choice != 6);
        }

        // ================= STUDENT LOGIN =================
        else if (role == 2) {

            System.out.print("Student ID: ");
            String id = input.nextLine();

            System.out.print("Password: ");
            String password = input.nextLine();

            if (!LoginSystem.studentLogin(id, password)) {
                System.out.println("Access Denied!");
                return;
            }

            System.out.print("Student Name: ");
            String name = input.nextLine();

            StudentUser student = new StudentUser(name, id);

            int choice;

            do {
                System.out.println("\n===== STUDENT MENU =====");
                System.out.println("1. Show Courses");
                System.out.println("2. Search Course");
                System.out.println("3. Register Course");
                System.out.println("4. My Courses");
                System.out.println("5. Save & Exit");

                System.out.print("Enter choice: ");
                choice = input.nextInt();
                input.nextLine();

                switch (choice) {

                    case 1:
                        system.displayAllCourses();
                        break;

                    case 2:
                        System.out.print("Code: ");
                        system.searchCourse(input.nextLine());
                        break;

                    case 3:
                        System.out.print("Course Code: ");
                        String ccode = input.nextLine();

                        system.registerStudent(ccode, student);
                        break;

                    case 4:
                        student.showCourses();
                        break;

                    case 5:
                        DataManager.saveCourses(system.courses);
                        System.out.println("Student Exit Successful!");
                        break;

                    default:
                        System.out.println("Invalid Choice!");
                }

            } while (choice != 5);

        }

        else {
            System.out.println("Invalid Role!");
        }

        input.close();
    }
}