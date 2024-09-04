package Assignment.model;

public class Suggestion {
    private static int suggestionCounter = 0;
    private int suggestionID;
    private Camp camp;
    private CommitteeMember committeeMember;
    private String description;
    private int state; // 0 for unprocessed, 1 for accepted, 2 for denied

    public Suggestion(String description, Camp camp, CommitteeMember comm) {
        this.suggestionID = ++suggestionCounter;
        this.camp = camp;
        this.committeeMember = comm;
        this.description = description;
        this.state = 0;
    }

    public int getSuggestionID() {
        return suggestionID;
    }

    public Camp getCamp() {
        return this.camp;
    }
    // no setCamp since Suggestion is linked to Camp

    public CommitteeMember getCommitteeMember() {
        return committeeMember;
    }
    // no setCommitteeMembe since Suggestion is linked to Committee Member

    public void setDescription(String s) {
        this.description = s;
    }

    public String getDescription() {
        return this.description;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

}
