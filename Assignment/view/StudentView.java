package Assignment.view;

import Assignment.controller.StudentManager;


public class StudentView extends LoggedInView {
    public static void menuView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Student Main View:");
        System.out.println("(1) Change Password");
        System.out.println("(2) View Camps");
        System.out.println("(3) View Registered Camps");
        System.out.println("(4) View Enquiries");
        System.out.println("(5) Log out");
        System.out.println("=============================================");
    }

    public static void viewSwitch() {
        int choice = -1;
        do {
            menuView();
            choice = readInteger(1, 5);
            switch (choice) {
                case 1:
                    // change password
                    if (!promptChangePassword()) {
                        System.out.println("Wrong old password. Password change failed");
                    } else {
                        System.out.println("Password change successful. Please re-login!");
                        choice = 5;
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
                    // view enquiries
                    myEnquiriesSwitch();
                    break;
                case 5:
                    // log out
                    logOut();
                    break;
                default:
                    System.out.println("Invalid Choice");
            }
            pressAnyToContinue();

        } while (choice != 5);
    }

    public static void studentCampsView() {
        clearScreen();
        System.out.println("Viewing visible camps... ");
        System.out.println("(according to visiblity and faculty)");
        StudentManager.viewListCamps(false);
        System.out.println("=============================================");
        System.out.println("Student Camp View:");
        System.out.println("(1) View Camp Info");
        System.out.println("(2) Register Camp");
        System.out.println("(3) Submit Enquiry Camp Info");
        System.out.println("(4) Back");
        System.out.println("=============================================");
    }

    public static void viewCampsSwitch() {
        studentCampsView();
        int choice = -1;
        choice = readInteger(1, 4);
        switch (choice) {
            case 1:
                // view camp info
                promptViewCampInfo();
                break;
            case 2:
                // register camp
                if (!promptRegisterCamp()) {
                    System.out.println("Unable to register");
                } else {
                    System.out.println("Registration succesful");
                }
                break;
            case 3:
                // enquire camp info
                promptSubmitEnquiry();
                break;
            case 4:
                // back
                break;
            default:
                break;
        }
        if (choice != 4) {
            pressAnyToContinue();
        }
    }

    public static void promptViewCampInfo() {
        clearScreen();
        System.out.println("Viewing camp info...");
        StudentManager.viewCampInfo();
    }

    public static boolean promptRegisterCamp() {
        clearScreen();
        System.out.println("Registering for camp...");
        return StudentManager.registerCamp();
    }

    public static void promptSubmitEnquiry() {
        clearScreen();
        System.out.println("Submitting an enquiry...");
        StudentManager.submitEnquiry();
    }

    public static void registeredCampsView() {
        clearScreen();
        System.out.println("Viewing registered camps... ");
        StudentManager.viewListCamps(true);
        System.out.println("=============================================");
        System.out.println("Student Registered Camp View:");
        System.out.println("(1) Request Unregistration Camp");
        System.out.println("(2) View Camp Info");
        System.out.println("(3) Back");
        System.out.println("=============================================");
    }

    public static void registeredCampsSwitch() {
        registeredCampsView();
        int choice = -1;
        choice = readInteger(1, 3);
        switch (choice) {
            case 1:
                // request unregistration camp
                if (!promptUnregisterCamp()) {
                    System.out.println("Fail to unregister camp");
                } else {
                    System.out.println("Succesfully unregistered");
                }
                break;
            case 2:
                // view camp info
                promptViewRegisteredCamps();
                break;
            case 3:
                // back
                break;
            default:
                break;
        }
        if (choice != 3) {
            pressAnyToContinue();
        }
    }

    public static void promptViewRegisteredCamps() {
        clearScreen();
        System.out.println("Viewing registered camps info...");
        StudentManager.viewRegisteredCamps();
    }

    public static boolean promptUnregisterCamp() {
        clearScreen();
        System.out.println("Requesting for unregistration of camp...");
        return StudentManager.unregisterCamp();
    }

    public static void myEnquiriesView() {
        clearScreen();
        System.out.println("=============================================");
        System.out.println("Student Enquiry View:");
        System.out.println("(1) View Own Enquiry Reply");
        System.out.println("(2) View Camp Enquiry Reply");
        System.out.println("(3) Edit Enquiry");
        System.out.println("(4) Delete Enquiry");
        System.out.println("(5) Back");
        System.out.println("=============================================");
    }

    public static void myEnquiriesSwitch() {
        myEnquiriesView();
        int choice = -1;
        choice = readInteger(1, 5);
        switch (choice) {
            case 1:
                // View Own Enquiry Reply
                promptViewOwnEnquiryReply();
                break;
            case 2:
                // View Camp Enquiry reply
                promptViewCampEnquiryReply();
                break;
            case 3:
                // Edit Enquiry
                if (!promptEditEnquiry()) {
                    System.out.println("Fail to edit enquiry");
                } else {
                    System.out.println("Succesfully edited the enquiry");
                }
                break;
            case 4:
                // Delete Enquiry
                if (!promptDeleteEnquiry()) {
                    System.out.println("Fail to delete enquiry");
                } else {
                    System.out.println("Succesfully deleted the enquiry");
                }
                break;
            case 5:
                // back
                break;
            default:
                break;
        }
        if (choice != 5) {
            pressAnyToContinue();
        }
    }

    public static void promptViewOwnEnquiryReply() {
        clearScreen();
        System.out.println("Viewing own enquiries (both replied/not replied)...");
        StudentManager.viewOwnEnquiryReply();
    }

    public static void promptViewCampEnquiryReply() {
        clearScreen();
        System.out.println("Viewing camp enquiries (both replied/not replied)...");
        StudentManager.viewCampEnquiryReply();
    }

    public static boolean promptEditEnquiry() {
        clearScreen();
        System.out.println("Editing enquiries (not replied)...");
        return StudentManager.editEnquiry();
    }

    public static boolean promptDeleteEnquiry() {
        clearScreen();
        System.out.println("Deleting enquiries (not replied)...");
        return StudentManager.deleteEnquiry();
    }

}