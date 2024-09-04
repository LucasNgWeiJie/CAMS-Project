package Assignment.controller;

import Assignment.database.Database;
import Assignment.database.UserSession;

import Assignment.model.CommitteeMember;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;
import Assignment.model.User.Faculty;

public class UserManager {

    public static boolean login(String id, String password) {
        for (Student student : Database.getStudentList()) {
            String tempStudentID = student.getUserID();
            if (tempStudentID.equals(id)) {
                if (student.validatePassword(password)) {
                    UserSession.getInstance().setLoggedInUser(student);
                    return true;
                }
            }
        }
        for (Staff staff : Database.getStaffList()) {
            String tempStaffID = staff.getUserID();
            if (tempStaffID.equals(id)) {
                if (staff.validatePassword(password)) {
                    UserSession.getInstance().setLoggedInUser(staff);
                    return true;
                }
            }
        }
        for (CommitteeMember committee : Database.getCommitteeMemberList()) {
            String tempCommID = committee.getUserID();
            if (tempCommID.equals(id)) {
                if (committee.validatePassword(password)) {
                    UserSession.getInstance().setLoggedInUser(committee);
                    return true;
                }
            }
        }
        return false;
    }

    public static void register(String name, String user_ID, Faculty faculty, String userType) {
        if (userType == "STAFF") {
            Staff staff = new Staff(name, user_ID, faculty);
            Database.getStaffList().add(staff);
            Database.saveOneFile("staff_list");
            System.out.println("Staff created! Details: ");
            printUserDetails(staff);
        } else if (userType == "STUDENT") {
            Student student = new Student(name, user_ID, faculty);
            Database.getStudentList().add(student);
            Database.saveOneFile("student_list");
            System.out.println("Student created! Details: ");
            printUserDetails(student);
        }
    }

    public static void printUserDetails(User user) {
        System.out.println("Name: " + user.getName());
        System.out.println("Email: " + user.getUserID());
        System.out.println("Faculty: " + user.getFaculty());
    }

    public static boolean changePassword(String oldPass, String newPass) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // check its respective instance
        if (user instanceof Staff) {
            // if user is a staff
            if (((Staff) user).validatePassword(oldPass)) {
                ((Staff) user).setPassword(newPass);
                return true;
            } else {
                return false;
            }
        } else if (user instanceof CommitteeMember) {
            // if user is committee member
            if (((CommitteeMember) user).validatePassword(oldPass)) {
                ((CommitteeMember) user).setPassword(newPass);
                return true;
            } else {
                return false;
            }
        } else if (user instanceof Student) {
            // if user is student but not committee member
            if (((Student) user).validatePassword(oldPass)) {
                ((Student) user).setPassword(newPass);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}