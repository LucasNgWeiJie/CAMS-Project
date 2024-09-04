package Assignment.view;

import Assignment.controller.CommitteeMemberManager;

public class CommitteeMemberView extends StudentView {

    public static void menuView() {
        clearScreen();
        System.out.println("=============================================");
        CommitteeMemberManager.printCommitteeMemberDetails();
        System.out.println("=============================================");
        System.out.println("Committee Member Main View:");
        System.out.println("(1) Change Password");
        System.out.println("(2) View Camps");
        System.out.println("(3) View Registered Camps");
        System.out.println("(4) View My Enquiries");
        System.out.println("(5) View/Create/Edit Suggestions");
        System.out.println("(6) View Camp Enquiries");
        System.out.println("(7) Generate Camps Report");
        System.out.println("(8) Log out");
        System.out.println("=============================================");
    }

    public static void viewSwitch() {
        int choice = -1;
        do {
            menuView();
            choice = readInteger(1, 8);
            switch (choice) {
                case 1:
                    // change password
                    if (!promptChangePassword()) {
                        System.out.println("Wrong old password. Password change failed");
                    } else {
                        System.out.println("Password change successful. Please re-login!");
                        choice = 8;
                    }
                    break;
                case 2:
                    // view camps
                    viewCampsSwitch();
                    break;
                case 3:
                    // view registered camps
                    registeredCampsSwitch();
                    break;
                case 4:
                    // view my enquiries
                    myEnquiriesSwitch();
                    break;
                case 5:
                    // view/create/edit suggestions
                    committeeSuggestionsSwitch();
                    break;
                case 6:
                    // view camp enquiries
                    campEnquiriesSwitch();
                    break;
                case 7:
                    // generate camps report
                    campReportSwitch();
                    break;
                case 8:
                    // log out
                    logOut();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
            pressAnyToContinue();

        } while (choice != 8);
    }

    private static void committeeSuggestionsView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Committee Member Suggestions View:");
        System.out.println("(1) View Suggestions");
        System.out.println("(2) Create Suggestions");
        System.out.println("(3) Edit Suggestions");
        System.out.println("(4) Delete Suggestions");
        System.out.println("(5) Back");
        System.out.println("=============================================");
    }

    private static void committeeSuggestionsSwitch() {
        committeeSuggestionsView();
        int choice = -1;
        choice = readInteger(1, 5);
        switch (choice) {
            case 1:
                // View Suggestions
                promptViewSuggestions();
                break;
            case 2:
                // Create Suggestions
                if (!promptCreateSuggestions()) {
                    System.out.println("Fail to create suggestion");
                } else {
                    System.out.println("Succesfully created suggestion");
                }
                break;
            case 3:
                // Edit Suggestions
                if (!promptEditSuggestions()) {
                    System.out.println("Fail to edit suggestion");
                } else {
                    System.out.println("Succesfully edited suggestion");
                }
                break;
            case 4:
                // Delete SUggestions
                if (!promptDeleteSuggestions()) {
                    System.out.println("Fail to delete suggestion");
                } else {
                    System.out.println("Succesfully deleted suggestion");
                }
                break;
            case 5:
                // Back
                break;
            default:
                break;
        }
        if (choice != 5) {
            pressAnyToContinue();
        }
    }

    private static void promptViewSuggestions() {
        clearScreen();
        System.out.println("Viewing suggestions made...");
        CommitteeMemberManager.viewSuggestions();
    }

    private static boolean promptCreateSuggestions() {
        clearScreen();
        System.out.println("Creating new suggestions...");
        return CommitteeMemberManager.createSuggestions();
    }

    private static boolean promptEditSuggestions() {
        clearScreen();
        System.out.println("Editing suggestions made...");
        return CommitteeMemberManager.editSuggestions();
    }

    private static boolean promptDeleteSuggestions() {
        clearScreen();
        System.out.println("Deleting suggestions made...");
        return CommitteeMemberManager.deleteSuggestions();
    }

    private static void campEnquiriesView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Committee Member Camp In-Charge Enquiry View:");
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
        CommitteeMemberManager.viewCampInChargeEnquiry();
    }

    private static boolean promptReplyCampInChargeEnquiry() {
        clearScreen();
        System.out.println("Replying to camp in-charge enquiry...");
        return CommitteeMemberManager.replyCampInChargeEnquiry();
    }

    private static void campReportView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Camp Committee Report Generation View:");
        System.out.println("(1) Camp Report");
        System.out.println("(2) Students' Enquiry Report");
        System.out.println("(3) Back");
        System.out.println("=============================================");
    }

    private static void campReportSwitch() {
        campReportView();
        int choice = -1;
        choice = readInteger(1, 3);
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
                // Back
                break;
            default:
                break;
        }
        if (choice != 3) {
            pressAnyToContinue();
        }
    }

    private static boolean promptProduceCampReport() {
        clearScreen();
        System.out.println("Producing camp report...");
        System.out.println("Enter report name: ");
        return CommitteeMemberManager.produceCampReport(readString());
    }

    private static boolean promptProduceEnquiryReport() {
        clearScreen();
        System.out.println("Producing students' enquiry report...");
        System.out.println("Enter report name: ");
        return CommitteeMemberManager.produceEnquiryReport(readString());
    }

}
