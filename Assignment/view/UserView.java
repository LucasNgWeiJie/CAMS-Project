package Assignment.view;

import Assignment.controller.UserManager;
import Assignment.database.UserSession;
import Assignment.model.Student;
import Assignment.model.User.Faculty;
import Assignment.model.Staff;
import Assignment.model.CommitteeMember;

public class UserView extends View {


    public static void menuView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Camp Application and Management System (CAMs)");
        System.out.println("Welcome to CAMs");
        System.out.println("(1) Register");
        System.out.println("(2) Login");
        System.out.println("(3) Exit");
        System.out.println("=============================================");

    }

    public static void viewSwitch() {
        int choice = -1;
        do {
            menuView();
            choice = readInteger(1, 5);
            switch (choice) {
                case 1:
                    // register
                    if (!promptCreateUser()) {
                        System.out.println("Failed to create user");
                    } else {
                        System.out.println("Please login to confirm");
                    }
                    // prompt to login using registered
                    break;
                case 2:
                    // Login
                    if (!promptLogin()) {
                        System.out.println("Failed to login");
                        System.out.println("Invalid user or valid ID but wrong password");
                    }
                    // dont swap the if else statements, committee member must be on top of student
                    else if (UserSession.getInstance().getLoggedInUser() instanceof Staff) {
                        StaffView.viewSwitch();
                    } else if (UserSession.getInstance().getLoggedInUser() instanceof CommitteeMember) {
                        CommitteeMemberView.viewSwitch(); // note that if student just registered as committee, then he
                                                          // has to relogin
                    } else if (UserSession.getInstance().getLoggedInUser() instanceof Student) {
                        StudentView.viewSwitch();
                    }
                    break;
                case 3:
                    // exit
                    System.out.println("Exiting CAMS...");
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
            if (choice != 3) {
                pressAnyToContinue();
            }
        } while (choice != 3);
    }

    private static boolean promptCreateUser() {
        clearScreen();
        System.out.println("What account do you want to make: ");
        System.out.println("(1) Student");
        System.out.println("(2) Staff");
        int accChoice = readInteger(1, 2);
        System.out.println("Enter name: ");
        String name = readString();
        System.out.println("Enter email: ");
        String email = readString();
        System.out.println("Enter faculty: ");
        Faculty faculty = readEnumFaculty();
        UserManager.register(name, email, faculty, (accChoice == 1 ? "STUDENT" : "STAFF"));
        return true;
    }

    private static boolean promptLogin() {
        clearScreen();
        System.out.print("Enter email: ");
        String id = readString();
        System.out.print("Enter password: ");
        String password = readString();
        return UserManager.login(id, password);
    }

}
