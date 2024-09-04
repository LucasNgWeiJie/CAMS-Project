package Assignment.model;

import java.util.ArrayList;

public class Camp {
    private CampInformation campInfo;
    private ArrayList<Student> students;
    private ArrayList<CommitteeMember> campCommittee;
    private ArrayList<Enquiry> enquiryList;
    private ArrayList<Suggestion> suggestionList;
    private ArrayList<Student> bannedStudents;
    private boolean visibility;

    public Camp(CampInformation campInfo, boolean visibility) {
        this.campInfo = campInfo;
        this.students = new ArrayList<>();
        this.campCommittee = new ArrayList<>();
        this.enquiryList = new ArrayList<>();
        this.suggestionList = new ArrayList<>();
        this.bannedStudents = new ArrayList<>();
        this.visibility = visibility;
    }

    // methods
    public void access(User user) {
        if (user instanceof Staff || user instanceof CommitteeMember) {
            // access for user
        } else {
            System.out.println("Access is denied!");
        }
    }

    public CampInformation getCampInfo() {
        return campInfo;
    }

    public void setCampInfo(CampInformation campInfo) {
        this.campInfo = campInfo;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void removeStudent(Student student) {
        students.remove(student);
    }

    public ArrayList<CommitteeMember> getCampCommittee() {
        return campCommittee;
    }

    public void addCampCommittee(CommitteeMember committeeMember) {
        campCommittee.add(committeeMember);
    }

    public void removeCampCommittee(CommitteeMember committeeMember) {
        campCommittee.remove(committeeMember);
    }

    public ArrayList<Enquiry> getEnquiryList() {
        return enquiryList;
    }

    public void addEnquiry(Enquiry enquiry) {
        enquiryList.add(enquiry);
    }

    public boolean removeEnquiry(Enquiry enquiry) {
        if (enquiry.isAnswered()) {
            return false;
        }
        enquiryList.remove(enquiry);
        return true;
    }
    // only if it HAS NOT been answered

    public ArrayList<Suggestion> getSuggestionList() {
        return suggestionList;
    }

    public void addSuggestion(Suggestion suggestion) {
        suggestionList.add(suggestion);
    }

    public void removeSuggestion(Suggestion suggestion) {
        suggestionList.remove(suggestion);
    }
    // only if it HAS NOT been processed

    public ArrayList<Student> getBannedStudents() {
        return bannedStudents;
    }

    public void addBannedStudent(Student student) {
        bannedStudents.add(student);
    }
    // no removing for banned students

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}