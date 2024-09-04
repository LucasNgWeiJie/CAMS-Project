package Assignment.database;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import Assignment.model.Camp;
import Assignment.model.CommitteeMember;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;

public class Database {
    public static final String SEPARATOR = "|";

    private static ArrayList<Student> student = new ArrayList<Student>();

    private static ArrayList<Staff> staff = new ArrayList<Staff>();

    private static ArrayList<CommitteeMember> committeeMember = new ArrayList<CommitteeMember>();
    // no save files for committee member yet since needs to store its camp

    private static ArrayList<Camp> camp = new ArrayList<Camp>();
    // no save files for camp yet

    public static void start() {
        try {
            readUsers("student_list");
            readUsers("staff_list");
            readUsers("committeemember_list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // setters and getters
    public static ArrayList<Student> getStudentList() {
        return student;
    }

    public static ArrayList<Staff> getStaffList() {
        return staff;
    }

    public static ArrayList<CommitteeMember> getCommitteeMemberList() {
        return committeeMember;
    }

    public static ArrayList<Camp> getCampList() {
        return camp;
    }

    // setters? not sure if need
    public static void setCampList(Camp c) {
        camp.add(c);
    }

    public static void saveAllFiles() {
        try {
            saveUsers("student_list");
            saveUsers("staff_list");
            saveUsers("committeemember_list");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveOneFile(String fileType) {
        try {
            saveUsers(fileType);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readUsers(String fileName) throws IOException {
        String filePath = "Assignment\\database\\" + fileName + ".txt";
        ArrayList stringArray = (ArrayList) readFile(filePath);

        for (int i = 0; i < stringArray.size(); i++) {
            String st = (String) stringArray.get(i);
            // get individual 'fields' of the string separated by SEPARATOR
            StringTokenizer star = new StringTokenizer(st, SEPARATOR); // pass in the string to the string tokenizer
            String name = star.nextToken().trim(); // first token
            String email = star.nextToken().trim(); // second token
            String faculty = star.nextToken().trim(); // third token
            String password = star.nextToken().trim();

            if (fileName.equals("student_list")) {
                Student temp = new Student(name, email, User.Faculty.valueOf(faculty));
                temp.setPassword(password);
                student.add(temp);
            } else if (fileName.equals("staff_list")) {
                Staff temp = new Staff(name, email, User.Faculty.valueOf(faculty));
                temp.setPassword(password);
                staff.add(temp);
            }
        }

    }

    public static void saveUsers(String filename) throws IOException {
        String filePath = "Assignment\\database\\" + filename + ".txt";
        List alw = new ArrayList();// to store Professors data
        List al = null;

        if (filename == "staff_list") {
            al = staff;
        } else if (filename == "student_list") {
            al = student;
        } else if (filename == "committeemember_list") {
            al = committeeMember;
        }

        for (int i = 0; i < al.size(); i++) {
            User user = (User) al.get(i);
            StringBuilder st = new StringBuilder();
            st.append(user.getName().trim());
            st.append(SEPARATOR);
            st.append(user.getUserID().trim());
            st.append(SEPARATOR);
            st.append(user.getFaculty());
            st.append(SEPARATOR);
            st.append(user.getPassword());
            alw.add(st.toString());
        }
        writeFile(filePath, alw);
    }

    public static List readFile(String filename) throws IOException {
        List data = new ArrayList<>();
        Scanner scanner = new Scanner(new FileInputStream(filename));
        try {
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        return data;
    }

    public static void writeFile(String fileName, List data) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));
        try {
            for (int i = 0; i < data.size(); i++) {
                out.println((String) data.get(i));
            }
        } finally {
            out.close();
        }
    }

}
