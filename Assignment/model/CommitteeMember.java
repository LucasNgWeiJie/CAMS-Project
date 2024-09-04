package Assignment.model;

import java.util.ArrayList;

public class CommitteeMember extends Student {
    private int points;
    private Camp registeredCampAsComm;
    private ArrayList<Suggestion> ownSuggestionList;

    public CommitteeMember(Student stu, Camp camp) {
        // Initate a new committee member object using student information
        super(stu.getName(), stu.getUserID(), stu.getFaculty());
        super.setPassword(stu.getPassword());
        this.points = 0;
        this.registeredCampAsComm = camp;
        this.ownSuggestionList = new ArrayList<>();
        // remove student
        stu = null;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints() {
        this.points += 1;
    }

    public Camp getRegisteredCampAsComm() {
        return registeredCampAsComm;
    }
    // no setRegisteredCampAsComm since camp comm cant quit

    public ArrayList<Suggestion> getSuggestions() {
        return ownSuggestionList;
    }

    public void addSuggestions(Suggestion suggestion) {
        ownSuggestionList.add(suggestion);
    }

    public void removeSuggestions(Suggestion suggestion) {
        ownSuggestionList.remove(suggestion);
    }
}