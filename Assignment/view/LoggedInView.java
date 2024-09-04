package Assignment.view;

import Assignment.controller.StaffManager;
import Assignment.database.UserSession;

public abstract class LoggedInView extends View{
    
    public static boolean promptChangePassword() {
        clearScreen();
        System.out.println("Changing password... ");
        System.out.println("Enter your old password: ");
        String oldPass = readString();
        System.out.println("Enter your new password: ");
        String newPass = readString();
        return StaffManager.changePassword(oldPass, newPass);
    }
    public static void logOut() {
        System.out.println("Logging out...");
        UserSession.getInstance().clearSession();
        return;
    }
}
