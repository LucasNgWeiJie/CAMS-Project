package Assignment.view;

import java.util.Calendar;

import Assignment.controller.StaffManager;

import Assignment.model.User.Faculty;

public class StaffView extends LoggedInView {

    public static void menuView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Staff Main View:");
        System.out.println("(1) Change Password");
        System.out.println("(2) View/Create/Edit/Delete Camps");
        System.out.println("(3) View Suggestions");
        System.out.println("(4) View Enquiries");
        System.out.println("(5) Generate Camps Report");
        System.out.println("(6) Log out");
        System.out.println("=============================================");
    }

    public static void viewSwitch() {
        int choice = -1;
        do {
            menuView();
            choice = readInteger(1, 6);
            switch (choice) {
                case 1:
                    // change password
                    if (!promptChangePassword()) {
                        System.out.println("Wrong old password. Password change failed");
                    } else {
                        System.out.println("Password change successful. Please re-login!");
                        choice = 6;
                    }
                    break;
                case 2:
                    // view/create/edit/delete camps
                    campsSwitch();
                    break;
                case 3:
                    // view suggestions
                    staffSuggestionsSwitch();
                    break;
                case 4:
                    // view enquiries
                    campEnquiriesSwitch();
                    break;
                case 5:
                    // generate camps report
                    campReportSwitch();
                    break;
                case 6:
                    // log out
                    logOut();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
            pressAnyToContinue();

        } while (choice != 6);
    }

    private static void campsView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("View/Create/Edit/Delete Camps:");
        System.out.println("(1) Create Camp");
        System.out.println("(2) View Camps");
        System.out.println("(3) View Created Camps");
        System.out.println("(4) Edit Camp");
        System.out.println("(5) Delete Camp");
        System.out.println("(6) Back");
        System.out.println("=============================================");
    }

    private static void campsSwitch() {
        campsView();
        int choice = -1;
        choice = readInteger(1, 6);
        switch (choice) {
            case 1:
                // create camp
                if (!promptCreateCamp()) {
                    System.out.println("Fail to make camp!");
                } else {
                    System.out.println("A new camp is made!");
                }
                break;
            case 2:
                // view camps
                promptViewCamps();
                break;
            case 3:
                // view camps that the staff created
                promptViewCreatedCamps();
                break;
            case 4:
                // edit camp
                if (!promptEditCamp()) {
                    System.out.println("Fail to edit camp!");
                } else {
                    System.out.println("Camp edit is made!");
                }
                break;
            case 5:
                // delete camp
                if (!promptDeleteCamp()) {
                    System.out.println("Fail to delete camp!");
                } else {
                    System.out.println("Camp is deleted!");
                }
                break;
            default:
                break;
        }
        if (choice != 6) {
            pressAnyToContinue();
        }
    }

    private static boolean promptCreateCamp() {
        clearScreen();
        System.out.println("Creating camp... ");
        // the error checking is done inside the controller function - not here
        System.out.println("Enter Camp Name: ");
        String campName = readString();
        System.out.print("Enter Start Date: ");
        Calendar startDate = readCalendar();
        System.out.print("Enter End Date: ");
        Calendar endDate = readCalendar();
        System.out.print("Enter Registration Closing Date: ");
        Calendar regClosingDate = readCalendar();
        System.out.println("Enter User Group: ");
        Faculty userGroup = readEnumFaculty();
        System.out.println("Enter Location: ");
        String location = readString();
        System.out.println("Enter Total Slots: ");
        int totalSlots = readInteger(0, Integer.MAX_VALUE);
        System.out.println("Enter Camp Committee Slots: (max 10)");
        int campCommitteeSlots = readInteger(0, 10);
        System.out.println("Enter Camp Description: ");
        String description = readString();
        System.out.println("Enter Camp Visibility: (true or false)");
        boolean visibility = readBoolean();
        return StaffManager.createCamp(campName, startDate, endDate, regClosingDate, userGroup,
                location, totalSlots, campCommitteeSlots, description, visibility);
    }

    private static void promptViewCamps() {
        clearScreen();
        System.out.println("Viewing camps...");
        StaffManager.viewCamp();
    }

    private static void promptViewCreatedCamps() {
        clearScreen();
        System.out.println("Viewing camps created by you...");
        StaffManager.viewCreatedCamps();
    }

    private static boolean promptEditCamp() {
        clearScreen();
        System.out.println("Editing camp...");
        return StaffManager.editCamp();
    }

    private static boolean promptDeleteCamp() {
        clearScreen();
        System.out.println("Deleting camp...");
        return StaffManager.deleteCamp();
    }

    private static void committeeSuggestionsView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Staff Camp In-Charge Suggestion View:");
        System.out.println("(1) View Camp In-Charge Suggestion");
        System.out.println("(2) Accept/Reject Camp In-Charge Suggestion");
        System.out.println("(3) Back");
        System.out.println("=============================================");
    }

    private static void staffSuggestionsSwitch() {
        committeeSuggestionsView();
        int choice = -1;
        choice = readInteger(1, 3);
        switch (choice) {
            case 1:
                // View Camp In-Charge Suggestion
                promptViewCampInChargeSuggestion();
                break;
            case 2:
                // Accept/Reject Camp In-Charge Suggestion
                int result = promptAcceptCampInChargeSuggestion();
                if (result == 0) {
                    System.out.println("Fail to accept/reject camp in-charge suggesiton");
                } else if (result == 1) {
                    System.out.println("Succesfully accepted camp in-charge suggestion");
                } else if (result == 2) {
                    System.out.println("Succesfully rejected camp in-charge suggestion");
                }
                break;
            case 3:
                // Back
                break;
            default:
                break;
        }
        if (choice != 3) {
            pressAnyToContinue();
        }
    }

    private static void promptViewCampInChargeSuggestion() {
        clearScreen();
        System.out.println("Viewing camp in-charge suggestion...");
        StaffManager.viewSuggestions();
    }

    private static int promptAcceptCampInChargeSuggestion() {
        clearScreen();
        System.out.println("Accepting/Rejecting camp in-charge suggestion...");
        return StaffManager.acceptCampInChargeSuggestion();
    }

    private static void campEnquiriesView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Staff Camp In-Charge Enquiry View:");
        System.out.println("(1) View Camp In-Charge Enquiry");
        System.out.println("(2) Reply Camp In-Charge Enquiry");
        System.out.println("(3) Back");
        System.out.println("=============================================");
    }

    private static void campEnquiriesSwitch() {
        campEnquiriesView();
        int choice = -1;
        choice = readInteger(1, 3);
        switch (choice) {
            case 1:
                // View Camp In-Charge Enquiry
                promptViewCampInChargeEnquiry();
                break;
            case 2:
                // Reply Camp In-Charge Enquiry
                if (!promptReplyCampInChargeEnquiry()) {
                    System.out.println("Fail to reply to camp in-charge enquiry");
                } else {
                    System.out.println("Succesfully replied to camp in-charge enquiry");
                }
                break;
            case 3:
                // Back
                break;
            default:
                break;
        }
        if (choice != 3) {
            pressAnyToContinue();
        }
    }

    private static void promptViewCampInChargeEnquiry() {
        clearScreen();
        System.out.println("Viewing camp in-charge enquiry...");
        StaffManager.viewCampInChargeEnquiry();
    }

    private static boolean promptReplyCampInChargeEnquiry() {
        clearScreen();
        System.out.println("Replying to camp in-charge enquiry...");
        return StaffManager.replyCampInChargeEnquiry();
    }

    private static void campReportView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Staff Report Generation View:");
        System.out.println("(1) Camp Report");
        System.out.println("(2) Students' Enquiry Report");
        System.out.println("(3) Camp Committee Performance Report");
        System.out.println("(4) Back");
        System.out.println("=============================================");
    }

    private static void campReportSwitch() {
        campReportView();
        int choice = -1;
        choice = readInteger(1, 4);
        switch (choice) {
            case 1:
                // Produce Camp Report
                if (!promptProduceCampReport()) {
                    System.out.println("Fail to produce camp report");
                } else {
                    System.out.println("Succesfully produced camp report! Check the folder");
                }
                break;
            case 2:
                // Students' Enquiry Report
                if (!promptProduceEnquiryReport()) {
                    System.out.println("Fail to produce students' enquiry report");
                } else {
                    System.out.println("Succesfully produced students' enquiry report! Check the folder");
                }
                break;
            case 3:
                // Camp Committee Performance Report
                if (!promptProduceCommPerformanceReport()) {
                    System.out.println("Fail to produce camp committee performance report");
                } else {
                    System.out.println("Succesfully produced camp committee performance report! Check the folder");
                }
                break;
            case 4:
                // Back
                break;
            default:
                break;
        }
        if (choice != 4) {
            pressAnyToContinue();
        }
    }

    private static boolean promptProduceCampReport() {
        clearScreen();
        System.out.println("Producing camp report...");
        System.out.println("Enter report name: ");
        return StaffManager.produceCampReport(readString());
    }

    private static boolean promptProduceEnquiryReport() {
        clearScreen();
        System.out.println("Producing students' enquiry report...");
        System.out.println("Enter report name: ");
        return StaffManager.produceEnquiryReport(readString());
    }

    private static boolean promptProduceCommPerformanceReport() {
        clearScreen();
        System.out.println("Producing all camp committee performance report...");
        System.out.println("Enter report name: ");
        return StaffManager.produceCommPerformanceReport(readString());
    }
    //////

}
