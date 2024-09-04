package Assignment.controller;

public class StudentManager extends UserManager {

    // Below functions will take from their respective manager

    // public static boolean changePassword(String oldPass, String newPass) {}
    // inherit from UserManager

    public static void viewListCamps(boolean onlyRegistered) {
        CampManager.viewListCamps(onlyRegistered);
        return;
    }

    public static void viewCampInfo() {
        CampManager.viewCampInfo();
        return;
    }

    public static boolean registerCamp() {
        return CampManager.registerCamp();
    }

    public static void viewRegisteredCamps() {
        CampManager.viewRegisteredCamps();
        return;
    }

    public static boolean unregisterCamp() {
        return CampManager.unregisterCamp();
    }

    public static void submitEnquiry() {
        EnquiryManager.submitEnquiry();
        return;
    }

    public static void viewOwnEnquiryReply() {
        EnquiryManager.viewOwnEnquiryReply();
        return;
    }

    public static void viewCampEnquiryReply() {
        EnquiryManager.viewCampEnquiryReply();
        return;
    }

    public static boolean editEnquiry() {
        return EnquiryManager.editEnquiry();
    }

    public static boolean deleteEnquiry() {
        return EnquiryManager.deleteEnquiry();
    }
}
