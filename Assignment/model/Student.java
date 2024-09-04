package Assignment.model;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<Camp> registeredCamps; // campid
    private ArrayList<Enquiry> enquiries; // enquiryid

    public Student(String name, String userid, Faculty fac) {
        super(name, userid, fac);
        this.registeredCamps = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    public ArrayList<Camp> getRegisteredCamps() {
        return registeredCamps;
    }

    public void addRegisteredCamp(Camp camp) {
        registeredCamps.add(camp);
    }

    public void removeRegisteredCamp(Camp camp) {
        registeredCamps.remove(camp);
    }

    public ArrayList<Enquiry> getEnquiries() {
        return enquiries;
    }

    public void addEnquiries(Enquiry enquiry) {
        enquiries.add(enquiry);
    }

    public boolean removeEnquiries(Enquiry enquiry) {
        if (enquiry.isAnswered()) {
            return false;
        }
        enquiries.remove(enquiry);
        return true;
    }
    // only if it HAS NOT been answered
}