package Assignment.model;

import java.util.ArrayList;

public class Staff extends User {
    private ArrayList<Camp> createdCamps;

    public Staff(String name, String userid, Faculty fac) {
        super(name, userid, fac);
        this.createdCamps = new ArrayList<>();
    }

    public ArrayList<Camp> getCreatedCamps() {
        return createdCamps;
    }

    public void addCamp(Camp camp) {
        createdCamps.add(camp);
    }

    public void removeCamp(Camp camp) {
        createdCamps.remove(camp);
    }
}