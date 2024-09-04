package Assignment.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Assignment.database.UserSession;
import Assignment.model.Camp;
import Assignment.model.CommitteeMember;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.Suggestion;
import Assignment.model.User;
import Assignment.view.View;

public class SuggestionManager {

    public static boolean createSuggestions() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // only camp committee can access
        if (user instanceof Staff || (user instanceof Student && !(user instanceof CommitteeMember))) {
            // trigger for staff and student but not committee members
            System.out.println("This view suggestions is for committee members only");
            return false;
        }

        // get commCamp
        CommitteeMember comm = (CommitteeMember) user;
        // always have camp in charge - comm, no need if check
        Camp commCamp = comm.getRegisteredCampAsComm();

        // print out camp
        System.out.println("Making suggestion for Camp " + commCamp.getCampInfo().campID + ": "
                + commCamp.getCampInfo().campName);
        System.out.println("Camp Details: ");
        CampManager.printCampDetailsForStaffs(commCamp);
        System.out.println("");

        // get user suggestions
        System.out.println("Enter your suggestion: (Camp Section: Text)");
        Suggestion suggestion = new Suggestion(View.readString(), commCamp, comm);

        // add suggestion to camp comm
        comm.addSuggestions(suggestion);
        comm.addPoints(); // add 1 point for each suggestion given

        // add suggestion to camp
        commCamp.addSuggestion(suggestion);

        return true;
    }

    public static boolean editSuggestions() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // only camp committee can access
        if (user instanceof Staff || (user instanceof Student && !(user instanceof CommitteeMember))) {
            // trigger for staff and student but not committee members
            System.out.println("This view suggestions is for committee members only");
            return false;
        }

        // get commCamp
        CommitteeMember comm = (CommitteeMember) user;
        // always have camp in charge - comm, no need if check
        Camp commCamp = comm.getRegisteredCampAsComm();

        // get suggestions list
        ArrayList<Suggestion> suggestionList = new ArrayList<>(comm.getSuggestions());
        if (suggestionList.isEmpty()) {
            System.out.println("No own suggestions made yet");
            return false;
        }

        // get unprocessed suggestion list - can only edit unprocessed suggestion
        ArrayList<Suggestion> unprocessedSuggestionList = new ArrayList<>();
        for (Suggestion sugg : suggestionList) {
            if (sugg.getState() == 0) {
                unprocessedSuggestionList.add(sugg);
            }
        }
        if (unprocessedSuggestionList.isEmpty()) {
            System.out.println("No unprocessed suggestions to be edited");
            return false;
        }

        // print out all suggestions to choose to edit
        System.out.println("Suggestion List: ");
        Collections.sort(unprocessedSuggestionList, Comparator.comparing(sugg -> sugg.getSuggestionID()));
        for (Suggestion sugg : unprocessedSuggestionList) {
            System.out.println("(" + (sugg.getSuggestionID()) + ") Suggestion: " + sugg.getDescription());
        }

        // get user input for editing suggestion
        System.out.println("Enter the suggestion ID to edit: ");
        int index = View.readInteger();
        Suggestion selectedSuggestion = SuggestionManager.findSuggestionByID(index, unprocessedSuggestionList);
        if (selectedSuggestion == null) {
            System.out.println("Suggestion not found with the provided ID");
            return false;
        }

        // print camp to edit
        View.clearScreen();
        System.out.println("Editing suggestion for Camp " + commCamp.getCampInfo().campID + ": "
                + commCamp.getCampInfo().campName);
        System.out.println("Camp Details: ");
        CampManager.printCampDetailsForStaffs(commCamp);
        System.out.println("");

        // getting suggestion input
        System.out.println("Old Suggestion: " + selectedSuggestion.getDescription());
        System.out.println("Enter the new suggesiton: (Camp Section: Text)");
        String newSuggString = View.readString();

        // set the suggestion
        selectedSuggestion.setDescription(newSuggString);

        return true;
    }

    public static boolean deleteSuggestions() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // only camp committee can access
        if (user instanceof Staff || (user instanceof Student && !(user instanceof CommitteeMember))) {
            // trigger for staff and student but not committee members
            System.out.println("This view suggestions is for committee members only");
            return false;
        }

        // get commCamp
        CommitteeMember comm = (CommitteeMember) user;
        // always have camp in charge - comm, no need if check
        Camp commCamp = comm.getRegisteredCampAsComm();

        // get suggestions list
        ArrayList<Suggestion> suggestionList = new ArrayList<>(comm.getSuggestions());
        if (suggestionList.isEmpty()) {
            System.out.println("No own suggestions made yet");
            return false;
        }

        // only take the unprocessed ones - can only delete the unprocessed
        ArrayList<Suggestion> unprocessedSuggestionList = new ArrayList<>();
        for (Suggestion sugg : suggestionList) {
            if (sugg.getState() == 0) {
                unprocessedSuggestionList.add(sugg);
            }
        }
        if (unprocessedSuggestionList.isEmpty()) {
            System.out.println("No unprocessed suggestions to be deleted");
            return false;
        }

        // print out all suggestions to choose to delete
        System.out.println("Suggestion List: ");
        Collections.sort(unprocessedSuggestionList, Comparator.comparing(sugg -> sugg.getSuggestionID()));
        for (Suggestion sugg : unprocessedSuggestionList) {
            System.out.println("(" + (sugg.getSuggestionID()) + ") Suggestion: " + sugg.getDescription());
        }

        // get user input for deleting suggestion
        System.out.println("Enter the suggestion ID to delete: ");
        int index = View.readInteger();
        Suggestion selectedSuggestion = SuggestionManager.findSuggestionByID(index, unprocessedSuggestionList);
        if (selectedSuggestion == null) {
            System.out.println("Suggestion not found with the provided ID");
            return false;
        }

        // deletion process
        commCamp.removeSuggestion(selectedSuggestion);
        comm.removeSuggestions(selectedSuggestion);
        selectedSuggestion = null; // auto garabage-collected

        return true;
    }

    public static void viewSuggestions() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // get campList
        ArrayList<Camp> campList = new ArrayList<>();
        if (user instanceof Staff) {
            campList.addAll(((Staff) user).getCreatedCamps());
        } else if (user instanceof CommitteeMember) {
            campList.add(((CommitteeMember) user).getRegisteredCampAsComm());
        }

        // check if campList empty
        if (campList.isEmpty()) {
            System.out.println("No camps available to view suggestion from");
            return;
        }

        // print camp details of suggestion or not
        System.out.println("Camp List: ");
        for (Camp camp : campList) {
            System.out.println(
                    "(" + (camp.getCampInfo().campID) + ") Camp " + camp.getCampInfo().campName
                            + " - Total Suggestions: "
                            + ((camp.getSuggestionList().isEmpty()) ? "NA" : camp.getSuggestionList().size()));
        }

        // get the suggestion from selected camp
        System.out.println("Enter the camp ID to get suggestions: ");
        int campIndex = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campIndex, campList);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return;
        }

        // check if suggestion is empty
        if (selectedCamp.getSuggestionList().isEmpty()) {
            System.out.println("No suggestions made to the camp selected");
            return;
        }

        // sort by processed and unprocessed suggestions
        ArrayList<Suggestion> processedCommSuggestions = new ArrayList<>();
        ArrayList<Suggestion> unprocessedCommSuggestions = new ArrayList<>();
        for (Suggestion sugg : selectedCamp.getSuggestionList()) {
            if (sugg.getState() == 0) {
                unprocessedCommSuggestions.add(sugg);
            } else {
                processedCommSuggestions.add(sugg);
            }
        }

        // print out all suggestions for that camp
        View.clearScreen();
        System.out.println("Camp In Charge Details: ");
        CampManager.printCampDetailsForStaffs(selectedCamp);
        System.out.println("");

        // print unprocessed suggestions
        System.out.println("Unprocessed Suggestions: ");
        if (!unprocessedCommSuggestions.isEmpty()) {
            for (Suggestion sugg : unprocessedCommSuggestions) {
                System.out.println("Description: " + sugg.getDescription());
                System.out.println("State: Not processed yet");
            }
        } else
            System.out.println("NA");

        // print processed suggestions
        System.out.println("Processed Suggestions: ");
        if (!processedCommSuggestions.isEmpty()) {
            for (Suggestion sugg : processedCommSuggestions) {
                System.out.println("Description: " + sugg.getDescription());
                System.out.println("State: " + ((sugg.getState() == 1) ? "Accepted" : "Declined"));
            }
        } else
            System.out.println("NA");

        return;
    }

    public static int acceptCampInChargeSuggestion() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();
        if (user instanceof Student || user instanceof CommitteeMember) {
            System.out.println("Only staff is allowed to accept/decline camp in charge suggestion");
            return 0;
        }
        Staff staff = (Staff) user;

        // get camp in charge
        ArrayList<Camp> campInCharge = new ArrayList<>(staff.getCreatedCamps());
        if (campInCharge.isEmpty()) {
            System.out.println("No camps created to accept/decline suggestion from");
            return 0;
        }

        // print camp details of suggestion or not
        System.out.println("Camp List: ");
        Collections.sort(campInCharge, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : campInCharge) {
            // counting the number of unprocessed suggestions
            int unprocessedCount = 0;
            for (Suggestion sugg : camp.getSuggestionList()) {
                if (sugg.getState() == 0) {
                    unprocessedCount++;
                }
            }

            System.out
                    .println("(" + (camp.getCampInfo().campID) + ") Camp " + camp.getCampInfo().campName
                            + " - No. of Unprocessed Suggestions: "
                            + ((unprocessedCount == 0) ? "NA" : unprocessedCount));
        }
        System.out.println("Enter the camp ID to view its suggestions: ");
        int campIndex = View.readInteger();

        // get the suggestion from selected camp
        Camp selectedCamp = CampManager.findCampByID(campIndex, campInCharge);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return 0;
        }

        // check if suggestion list empty
        if (selectedCamp.getSuggestionList().isEmpty()) {
            System.out.println("No unprocessed suggestions for the camp selected");
            return 0;
        }

        // only take the unprocessed suggestions from the enquery list
        ArrayList<Suggestion> unprocessedCommSuggestions = new ArrayList<>();
        for (Suggestion sugg : selectedCamp.getSuggestionList()) {
            if (sugg.getState() == 0) {
                unprocessedCommSuggestions.add(sugg);
            }
        } // this only returns > 0 unprocessed suggestion

        // print out all suggestions to choose to approve/decline
        Collections.sort(unprocessedCommSuggestions, Comparator.comparing(sugg -> sugg.getSuggestionID()));
        for (Suggestion sugg : unprocessedCommSuggestions) {
            System.out.println("(" + (sugg.getSuggestionID()) + ") Suggestion: " + sugg.getDescription());
        }

        // get user to choose suggestion
        System.out.println(
                "Enter the suggestion ID to approve/deny: ");
        int suggestionID = View.readInteger();
        Suggestion selectedSuggestion = SuggestionManager.findSuggestionByID(suggestionID, unprocessedCommSuggestions);
        if (selectedSuggestion == null) {
            System.out.println("Suggestion not found with the provided ID");
            return 0;
        }

        // accepting/denying process
        View.clearScreen();
        System.out.println("Camp In Charge Details: ");
        CampManager.printCampDetailsForStaffs(selectedSuggestion.getCamp());
        System.out.println("Suggestion Description: " + selectedSuggestion.getDescription());
        System.out.println("Do you accept(1) or deny(2) this suggestion? (Enter 0 to back)");
        int choiceSuggString = View.readInteger(0, 2);

        // processing responses
        if (choiceSuggString == 0) {
            System.out.println("You have chosen to go back");
            return 0;
        } else if (choiceSuggString == 1) {
            selectedSuggestion.setState(1);
            System.out.println("The suggestion has been accepted");
            selectedSuggestion.getCommitteeMember().addPoints(); // add 1 point for each accepeted suggestion
            return 1;
        } else {
            selectedSuggestion.setState(2);
            System.out.println("The suggestion has been rejected");
            return 2;
        }

    }

    protected static Suggestion findSuggestionByID(int suggestionID, ArrayList<Suggestion> suggestionList) {
        for (Suggestion sugg : suggestionList) {
            if (sugg.getSuggestionID() == suggestionID) {
                return sugg;
            }
        }
        return null;
    }

}
