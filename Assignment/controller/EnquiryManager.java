package Assignment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Assignment.database.Database;
import Assignment.database.UserSession;
import Assignment.view.View;
import Assignment.model.Camp;
import Assignment.model.CommitteeMember;
import Assignment.model.Enquiry;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;

public class EnquiryManager {

    public static void submitEnquiry() {
        // initiate user
        User user = UserSession.getInstance().getLoggedInUser();

        // initial check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return;
        }
        if (user instanceof Staff) {
            System.out.println("This enquiry submission is for students only");
            return;
        }

        // get its respective userViewCamps
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userViewCamps.addAll(CampManager.getUserViewCamps((CommitteeMember) user));
        } else if (user instanceof Student) {
            userViewCamps.addAll(CampManager.getUserViewCamps((Student) user));
        }

        // check if userViewCamps empty
        if (userViewCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return;
        }

        // print out camp list for students to choose from
        System.out.println("Camp List: ");
        Collections.sort(userViewCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : userViewCamps) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }
        System.out.println("Enter the camp ID to enquire about: ");
        int campIndex = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campIndex, userViewCamps);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return;
        }

        // check if camp committee is enquiring from his/her own camp in charge
        if (user instanceof CommitteeMember) {
            Camp commCamp = ((CommitteeMember) user).getRegisteredCampAsComm();
            if (selectedCamp.getCampInfo().campID == commCamp.getCampInfo().campID) {
                System.out.println("Camp committee cannot enquire his/her camp in charge");
                return;
            }
        }

        View.clearScreen();
        System.out.println("Camp details: ");
        CampManager.printCampDetailsForStudents(selectedCamp);
        System.out.println("Enter your enquiry: ");
        Enquiry enquiry = new Enquiry(View.readString(), selectedCamp);
        selectedCamp.addEnquiry(enquiry);
        if (user instanceof CommitteeMember) {
            ((CommitteeMember) user).addEnquiries(enquiry);
        } else if (user instanceof Student) {
            ((Student) user).addEnquiries(enquiry);
        }
        return;
    }

    public static void viewOwnEnquiryReply() {
        // initiate user
        User user = UserSession.getInstance().getLoggedInUser();

        // initial check if
        if (user instanceof Staff) {
            System.out.println("This view enquiry is for students only");
            return;
        }

        // get its respective userEnquiries
        ArrayList<Enquiry> userEnquiries = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userEnquiries.addAll(((CommitteeMember) user).getEnquiries());
        } else if (user instanceof Student) {
            userEnquiries.addAll(((Student) user).getEnquiries());
        }

        // check if userEnquiries empty
        if (userEnquiries.isEmpty()) {
            System.out.println("No enquiries yet");
            return;
        }

        // sort by answered and unanswerd
        ArrayList<Enquiry> answeredUserEnquiries = new ArrayList<>();
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : userEnquiries) {
            if (enq.isAnswered()) {
                answeredUserEnquiries.add(enq);
            } else {
                unansweredUseEnquiries.add(enq);
            }
        }

        // print out unaswered enquries
        System.out.println("Unanswered Enqueries: ");
        if (!unansweredUseEnquiries.isEmpty()) {
            for (Enquiry enq : unansweredUseEnquiries) {
                System.out.println("Camp " + enq.getCamp().getCampInfo().campID + ": "
                        + enq.getCamp().getCampInfo().campID);
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: -");
            }
        } else
            System.out.println("NA");

        // print out answered enquries
        System.out.println("Answered Enqueries: ");
        if (!answeredUserEnquiries.isEmpty()) {
            for (Enquiry enq : answeredUserEnquiries) {
                System.out.println("Camp " + enq.getCamp().getCampInfo().campID + ": "
                        + enq.getCamp().getCampInfo().campName);
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: " + enq.getAnswer());
            }
        } else
            System.out.println("NA");

        return;
    }

    public static void viewCampEnquiryReply() {
        // initiate user
        User user = UserSession.getInstance().getLoggedInUser();

        // initial check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return;
        }
        if (user instanceof Staff) {
            System.out.println("This enquiry submission is for students only");
            return;
        }

        // get its respective userViewCamps
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userViewCamps.addAll(CampManager.getUserViewCamps((CommitteeMember) user));
        } else if (user instanceof Student) {
            userViewCamps.addAll(CampManager.getUserViewCamps((Student) user));
        }

        // check if userViewCamps empty
        if (userViewCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return;
        }

        // get campsWithEnquiries
        ArrayList<Camp> campsWithEnquiries = new ArrayList<>();
        for (Camp camp : userViewCamps) {
            if (camp.getEnquiryList().size() != 0) {
                campsWithEnquiries.add(camp);
            }
        }

        // check if campsWithEnquiries empty
        if (campsWithEnquiries.isEmpty()) {
            System.out.println("No visible camps with enquiries yet");
            return;
        }

        // select which camp to view its enquries
        System.out.println("Camp List: ");
        Collections.sort(campsWithEnquiries, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : campsWithEnquiries) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }
        System.out.println("Enter the camp ID to view enquiries: ");
        int campID = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campID, campsWithEnquiries);

        // sort by answered and unanswerd
        ArrayList<Enquiry> answeredUserEnquiries = new ArrayList<>();
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : selectedCamp.getEnquiryList()) {
            if (enq.isAnswered()) {
                answeredUserEnquiries.add(enq);
            } else {
                unansweredUseEnquiries.add(enq);
            }
        }

        // print unanswered queries
        System.out.println("Unanswered Queries: ");
        if (!unansweredUseEnquiries.isEmpty()) {
            for (Enquiry enq : unansweredUseEnquiries) {
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: -");
            }
        } else
            System.out.println("NA");

        // print answered queries
        System.out.println("Answered Queries: ");
        if (!answeredUserEnquiries.isEmpty()) {
            for (Enquiry enq : answeredUserEnquiries) {
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: " + enq.getAnswer());
            }
        } else {
            System.out.println("NA");
        }
        return;
    }

    public static boolean editEnquiry() {
        // initiate user
        User user = UserSession.getInstance().getLoggedInUser();

        // initial check if
        if (user instanceof Staff) {
            System.out.println("This view enquiry is for students only");
            return false;
        }

        // get its respective userEnquiries
        ArrayList<Enquiry> userEnquiries = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userEnquiries.addAll(((CommitteeMember) user).getEnquiries());
        } else if (user instanceof Student) {
            userEnquiries.addAll(((Student) user).getEnquiries());
        }

        // check if userEnquiries empty
        if (userEnquiries.isEmpty()) {
            System.out.println("No enquiries yet");
            return false;
        }

        // only take the unanswered ones
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : userEnquiries) {
            if (!enq.isAnswered()) {
                unansweredUseEnquiries.add(enq);
            }
        }

        // check if unasweredUseEnquiries empty
        if (unansweredUseEnquiries.isEmpty()) {
            System.out.println("No unanswered enquiries to be edited");
            return false;
        }

        // print out all queries to choose to edit
        System.out.println("Enquiry List: ");
        Collections.sort(unansweredUseEnquiries, Comparator.comparing(enq -> enq.getEnquiryID()));
        for (Enquiry enq : unansweredUseEnquiries) {
            System.out.println("(" + (enq.getEnquiryID()) + ") Enquiry: " + enq.getQuestion());
        }

        // get user input for editing enquiries
        System.out.println("Enter the enquiry ID o edit: ");
        int index = View.readInteger();
        Enquiry selectedEnquiry = EnquiryManager.findEnquiryByID(index, unansweredUseEnquiries);
        if (selectedEnquiry == null) {
            System.out.println("Enquiry not found with the provided ID.");
            return false;
        }

        // editing process
        View.clearScreen();
        System.out.println("Camp Details: ");
        CampManager.printCampDetailsForStudents(selectedEnquiry.getCamp());
        System.out.println("Old Enquiry: " + selectedEnquiry.getQuestion());
        System.out.println("Please enter the new enquiry: ");
        String newEnqString = View.readString();
        selectedEnquiry.setQuestion(newEnqString);
        return true;
    }

    public static boolean deleteEnquiry() {
        // initiate user
        User user = UserSession.getInstance().getLoggedInUser();

        // initial check if
        if (user instanceof Staff) {
            System.out.println("This view enquiry is for students only");
            return false;
        }

        // get its respective userEnquiries
        ArrayList<Enquiry> userEnquiries = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userEnquiries.addAll(((CommitteeMember) user).getEnquiries());
        } else if (user instanceof Student) {
            userEnquiries.addAll(((Student) user).getEnquiries());
        }

        // check if userEnquiries empty
        if (userEnquiries.isEmpty()) {
            System.out.println("No enquiries yet");
            return false;
        }

        // only take the unanswered ones
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : userEnquiries) {
            if (!enq.isAnswered()) {
                unansweredUseEnquiries.add(enq);
            }
        }

        // check if unasweredUseEnquiries empty
        if (unansweredUseEnquiries.isEmpty()) {
            System.out.println("No unanswered enquiries to be deleted");
            return false;
        }

        // print out all queries to choose to delete
        System.out.println("Enquiry List: ");
        Collections.sort(unansweredUseEnquiries, Comparator.comparing(enq -> enq.getEnquiryID()));
        for (Enquiry enq : unansweredUseEnquiries) {
            System.out.println("(" + (enq.getEnquiryID()) + ") Enquiry: " + enq.getQuestion());
        }

        // get user input for deleting enquiries
        System.out.println("Enter the enquiry ID to delete: ");
        int index = View.readInteger();
        Enquiry selectedEnquiry = EnquiryManager.findEnquiryByID(index, unansweredUseEnquiries);
        if (selectedEnquiry == null) {
            System.out.println("Enquiry not found with the provided ID.");
            return false;
        }
        Camp enquiryCamp = selectedEnquiry.getCamp();

        // start unreferencing
        enquiryCamp.removeEnquiry(selectedEnquiry);
        if (user instanceof CommitteeMember) {
            ((CommitteeMember) user).removeEnquiries(selectedEnquiry);
        } else if (user instanceof Student) {
            ((Student) user).removeEnquiries(selectedEnquiry);
        }
        selectedEnquiry = null; // auto garabage-collected

        return true;
    }

    public static void viewCampInChargeEnquiry() {
        // get its respective user and campList
        User user = UserSession.getInstance().getLoggedInUser();
        ArrayList<Camp> campList = new ArrayList<>();
        if (user instanceof Staff) {
            campList.addAll(((Staff) user).getCreatedCamps());
        } else if (user instanceof CommitteeMember) {
            campList.add(((CommitteeMember) user).getRegisteredCampAsComm());
        }

        // check if campList is empty
        if (campList.isEmpty()) {
            System.out.println("No camps list to view enquiries from");
            return;
        }

        // print camp details of enquiry or not
        System.out.println("Camp List: ");
        Collections.sort(campList, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : campList) {
            System.out
                    .println("(" + (camp.getCampInfo().campID) + ") Camp "
                            + camp.getCampInfo().campName + " - Total Enquiries: "
                            + ((camp.getEnquiryList().isEmpty()) ? "NA" : camp.getEnquiryList().size()));
        }
        System.out.println("Enter the camp ID to view its enquiries: ");
        int campIndex = View.readInteger();

        // get the queries from selected camp
        Camp selectedCamp = CampManager.findCampByID(campIndex, campList);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return;
        }
        if (selectedCamp.getEnquiryList().isEmpty()) {
            System.out.println("Error! You selected camps with NA enqueries");
            return;
        }

        // sort by unanswered and answered queries
        ArrayList<Enquiry> answeredUserEnquiries = new ArrayList<>();
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : selectedCamp.getEnquiryList()) {
            if (enq.isAnswered()) {
                answeredUserEnquiries.add(enq);
            } else {
                unansweredUseEnquiries.add(enq);
            }
        }

        // print out all enquiry for that camp
        View.clearScreen();
        System.out.println("Camp In Charge Details: ");
        CampManager.printCampDetailsForStaffs(selectedCamp);
        System.out.println("");
        System.out.println("Unanswered Queries: ");
        if (!unansweredUseEnquiries.isEmpty()) {
            for (Enquiry enq : unansweredUseEnquiries) {
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: -");
            }
        } else
            System.out.println("NA");

        System.out.println("Answered Queries: ");
        if (!answeredUserEnquiries.isEmpty()) {
            for (Enquiry enq : answeredUserEnquiries) {
                System.out.println("Question: " + enq.getQuestion());
                System.out.println("Answer: " + enq.getAnswer());
            }
        } else
            System.out.println("NA");

        return;
    }

    public static boolean replyCampInChargeEnquiry() {
        // get its respective user and campList
        User user = UserSession.getInstance().getLoggedInUser();
        ArrayList<Camp> campList = new ArrayList<>();
        if (user instanceof Staff) {
            campList.addAll(((Staff) user).getCreatedCamps());
        } else if (user instanceof CommitteeMember) {
            campList.add(((CommitteeMember) user).getRegisteredCampAsComm());
        }

        // check if campList is empty
        if (campList.isEmpty()) {
            System.out.println("No camps list to reply enquiries from");
            return false;
        }

        // print camp details of enquiry or not
        System.out.println("Camp List: ");
        Collections.sort(campList, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : campList) {
            // counting the number of unaswered queries
            int unansweredCount = 0;
            for (Enquiry enq : camp.getEnquiryList()) {
                if (!enq.isAnswered()) {
                    unansweredCount++;
                }
            }

            // print camp info with total unanswered enquiries
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName + " - No. of Unanswered Enquiries: "
                    + ((unansweredCount == 0) ? "NA" : unansweredCount));
        }
        System.out.println("Enter the camp ID to view its enquries: ");
        int campIndex = View.readInteger();

        // get the queries from selected camp
        Camp selectedCamp = CampManager.findCampByID(campIndex, campList);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return false;
        }

        // only take the unaswered enquries from the enquery list
        ArrayList<Enquiry> unansweredUseEnquiries = new ArrayList<>();
        for (Enquiry enq : selectedCamp.getEnquiryList()) {
            if (!enq.isAnswered()) {
                unansweredUseEnquiries.add(enq);
            }
        }
        if (unansweredUseEnquiries.isEmpty()) {
            System.out.println("No unaswered enquiries to be replied to");
            return false;
        }

        // print out all queries to choose to reply
        System.out.println("Enquiry List: ");
        Collections.sort(unansweredUseEnquiries, Comparator.comparing(enq -> enq.getEnquiryID()));
        for (Enquiry enq : unansweredUseEnquiries) {
            System.out.println("(" + (enq.getEnquiryID()) + ") Enquiry: " + enq.getQuestion());
        }
        System.out.println("Enter the enquiry ID to reply to: ");
        int enquiryID = View.readInteger();
        Enquiry selectedEnquiry = EnquiryManager.findEnquiryByID(enquiryID, unansweredUseEnquiries);
        if (selectedEnquiry == null) {
            System.out.println("Enquiry not found with the provided ID.");
            return false;
        }

        // replying process
        View.clearScreen();
        System.out.println("Camp Details: ");
        CampManager.printCampDetailsForStaffs(selectedCamp);
        System.out.println("Enquiry Question: " + selectedEnquiry.getQuestion());
        System.out.println("Please enter the reply: ");
        String replyEnqString = View.readString();

        // updating enquiry
        selectedEnquiry.setAnswer(replyEnqString);
        if (user instanceof CommitteeMember) {
            ((CommitteeMember) user).addPoints();
        }

        return true;
    }

    protected static Enquiry findEnquiryByID(int enquiryID, ArrayList<Enquiry> enquiryList) {
        for (Enquiry enq : enquiryList) {
            if (enq.getEnquiryID() == enquiryID) {
                return enq;
            }
        }
        return null;
    }

}
