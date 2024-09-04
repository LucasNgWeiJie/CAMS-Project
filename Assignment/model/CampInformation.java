package Assignment.model;

import java.util.Calendar;

import Assignment.model.User.Faculty;

public class CampInformation {
    public static int campInformationCounter = 0;
    public int campID;
    public String campName;
    public Calendar startDate;
    public Calendar endDate;
    public Calendar regClosingDate;
    public Faculty userGroup;
    public String location;
    public int totalSlots;
    public int campCommitteeSlots;
    public String description;
    public Staff staffInCharge;

    public CampInformation(String campName, Calendar startDate, Calendar endDate, Calendar regClosingDate,
            Faculty userGroup,
            String location, int totalSlots, int campCommitteeSlots, String description, Staff staffInCharge) {
        this.campID = ++campInformationCounter;
        this.campName = campName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.regClosingDate = regClosingDate;
        this.userGroup = userGroup;
        this.location = location;
        this.totalSlots = totalSlots;
        this.campCommitteeSlots = campCommitteeSlots;
        this.description = description;
        this.staffInCharge = staffInCharge;
    }
}