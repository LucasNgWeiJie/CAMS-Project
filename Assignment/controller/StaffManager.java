package Assignment.controller;

import java.util.Calendar;

import Assignment.model.User.Faculty;

public class StaffManager extends UserManager {
    // public static boolean changePassword(String oldPass, String newPass) {}
    // inherit from StudentManager

    public static void viewCamp() {
        CampManager.viewCampInfo();
        return;
    }

    public static boolean createCamp(String campName, Calendar startDate,
            Calendar endDate, Calendar regClosingDate, Faculty userGroup, String location, int totalSlots,
            int campCommitteeSlots, String description, boolean visibility) {
        return CampManager.createCamp(campName, startDate, endDate, regClosingDate, userGroup, location, totalSlots,
                campCommitteeSlots, description, visibility);
    }

    public static void viewCreatedCamps() {
        // this function uses registered camps since it functions the same way, adding
        // from registered/created camps list
        CampManager.viewRegisteredCamps();
        return;
    }

    public static boolean editCamp() {
        return CampManager.editCamp();
    }

    public static boolean deleteCamp() {
        return CampManager.deleteCamp();
    }

    public static void viewSuggestions() {
        SuggestionManager.viewSuggestions();
        return;
    }

    public static int acceptCampInChargeSuggestion() {
        return SuggestionManager.acceptCampInChargeSuggestion();
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

    public static boolean produceCommPerformanceReport(String reportName) {
        return ReportManager.produceCommPerformanceReport(reportName);
    }

}