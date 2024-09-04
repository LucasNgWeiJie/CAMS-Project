package Assignment.controller;

import Assignment.database.UserSession;

import Assignment.model.CommitteeMember;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;

public class CommitteeMemberManager extends StudentManager {

    // print out details of committee members
    public static void printCommitteeMemberDetails() {
        User user = UserSession.getInstance().getLoggedInUser();
        if (user instanceof Staff || (user instanceof Student && !(user instanceof CommitteeMember))) {
            // trigger for staff and student but not committee members
            System.out.println("This view is for committee members only");
            return;
        }
        CommitteeMember comm = (CommitteeMember) user;
        System.out.println("Committee Member: ");
        System.out.println(comm.getName() + " (from " + comm.getFaculty() + ")");
        System.out.println("Current Points: " + comm.getPoints());
    }

    // Below functions will take from their respective manager

    // public static boolean changePassword(String oldPass, String newPass) {
    // return UserManager.changePassword(oldPass, newPass);
    // }

    // public static void viewListCamps(boolean onlyRegistered) {} inherit from
    // StudentManager

    // public static void viewCampInfo() {} inherit from StudentManager

    // public static boolean registerCamp() {} inherit from StudentManager

    // public static void viewRegisteredCamps() {} inherit from StudentManager

    // public static boolean unregisterCamp() {} inherit from StudentManager

    // public static void submitEnquiry() {} inherit from StudentManager

    // public static void viewOwnEnquiryReply() {} inherit from StudentManager

    // public static void viewCampEnquiryReply() {} inherit from StudentManager

    // public static boolean editEnquiry() {} inherit from StudentManager

    // public static boolean deleteEnquiry() {} inherit from StudentManager

    public static void viewSuggestions() {
        SuggestionManager.viewSuggestions();
        return;
    }

    public static boolean createSuggestions() {
        return SuggestionManager.createSuggestions();
    }

    public static boolean editSuggestions() {
        return SuggestionManager.editSuggestions();
    }

    public static boolean deleteSuggestions() {
        return SuggestionManager.deleteSuggestions();
    }

    public static void viewCampInChargeEnquiry() {
        EnquiryManager.viewCampInChargeEnquiry();
        return;
    }

    public static boolean replyCampInChargeEnquiry() {
        return EnquiryManager.replyCampInChargeEnquiry();
    }

    public static boolean produceCampReport(String reportName) {
        return ReportManager.produceCampReport(reportName);
    }

    public static boolean produceEnquiryReport(String reportName) {
        return ReportManager.produceEnquiryReport(reportName);
    }

}
