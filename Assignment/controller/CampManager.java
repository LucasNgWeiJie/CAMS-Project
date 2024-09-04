package Assignment.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import Assignment.database.Database;
import Assignment.database.UserSession;
import Assignment.model.Camp;
import Assignment.model.CampInformation;
import Assignment.model.CommitteeMember;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;
import Assignment.model.User.Faculty;
import Assignment.view.View;

public class CampManager {

    // viewListCamps show ALL available to see camps - even those registered/beyond
    // pass registry. Its just that you cant register for those
    public static void viewListCamps(boolean onlyRegistered) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return;
        }
        if (user instanceof Staff) {
            System.out.println("This view is for students only");
            return;
        }

        // get the camps that can be seen by student/committee
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userViewCamps = new ArrayList<>(getUserViewCamps((CommitteeMember) user));
        } else if (user instanceof Student) {
            userViewCamps = new ArrayList<>(getUserViewCamps((Student) user));
        }

        // check if userViewCamps empty
        if (userViewCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return;
        }

        // print camp info
        if (!onlyRegistered) {
            // view for all camps - registered or not
            Collections.sort(userViewCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
            for (Camp camp : userViewCamps) {
                System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                        + camp.getCampInfo().campName);
            }
            return;
        } else {
            // view for all camps - only registered
            // get registeredCamps
            ArrayList<Camp> registeredCamps = new ArrayList<>();
            if (user instanceof CommitteeMember) {
                registeredCamps.addAll(((CommitteeMember) user).getRegisteredCamps());
            } else if (user instanceof Student) {
                registeredCamps.addAll(((Student) user).getRegisteredCamps());
            }

            // check if registeredCamps empty
            if (registeredCamps.isEmpty()) {
                System.out.println("No registered camps available yet");
                return;
            }

            // print registeredCamps
            Collections.sort(registeredCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
            for (Camp camp : registeredCamps) {
                System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                        + camp.getCampInfo().campName);
            }
            return;
        }
    }

    public static void viewCampInfo() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // have some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return;
        }

        // get the camps that can be seen by student/committee
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userViewCamps = new ArrayList<>(getUserViewCamps((CommitteeMember) user));
        } else if (user instanceof Student) {
            userViewCamps = new ArrayList<>(getUserViewCamps((Student) user));
        } else if (user instanceof Staff) {
            userViewCamps = new ArrayList<>(Database.getCampList());
        }

        // check if userViewCamps empty
        if (userViewCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return;
        }

        // filter process
        userViewCamps = filterCampView(userViewCamps);
        if (userViewCamps.isEmpty()) {
            System.out.println("Too much filter! No result");
            return;
        }

        // printing camp details to user to choose
        System.out.println("Camp List: ");
        Collections.sort(userViewCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : userViewCamps) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }

        // take user input
        System.out.println("Enter the camp ID to view details: ");
        int campID = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campID, userViewCamps);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return;
        }

        // print out selected camp details
        View.clearScreen();
        System.out.println("Camp Details: ");
        System.out.print("Camp Role: ");
        if (user instanceof CommitteeMember) {
            System.out.println("Camp Committee");
            printCampDetailsForStaffs(selectedCamp);
        } else if (user instanceof Student) {
            System.out.println("Attendees");
            printCampDetailsForStudents(selectedCamp);
        } else if (user instanceof Staff) {
            System.out.println("Staff");
            printCampDetailsForStaffs(selectedCamp);
        }
        return;
    }

    public static boolean registerCamp() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return false;
        }
        if (user instanceof Staff) {
            System.out.println("This view is for students only");
            return false;
        }

        // get the camps that can be seen by student/committee
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            userViewCamps = new ArrayList<>(getUserViewCamps((CommitteeMember) user));
        } else if (user instanceof Student) {
            userViewCamps = new ArrayList<>(getUserViewCamps((Student) user));
        }

        // check if userViewCamps empty
        if (userViewCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return false;
        }

        // get registeredCamps
        ArrayList<Camp> registeredCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            registeredCamps.addAll(((CommitteeMember) user).getRegisteredCamps());
        } else if (user instanceof Student) {
            registeredCamps.addAll(((Student) user).getRegisteredCamps());
        }

        // remove all camps that are registered already
        // camp committee camp will also be removed here so no need to worry
        ArrayList<Camp> campsToRegisterList = new ArrayList<>(userViewCamps);
        campsToRegisterList.removeAll(registeredCamps);

        // print out options for user
        System.out.println("Camp List: ");
        Collections.sort(campsToRegisterList, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : campsToRegisterList) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }

        // get user input for camp regiser
        System.out.println("Enter the camp ID to register: ");
        int campID = View.readInteger();
        System.out.println("Enter to register as (1) camp attendee or (2) camp committee:");
        int registerChoice = View.readInteger(1, 2);
        Camp selectedCamp = CampManager.findCampByID(campID, campsToRegisterList);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return false;
        }

        // checking for clash in time
        Calendar newStartDate = selectedCamp.getCampInfo().startDate;
        Calendar newEndDate = selectedCamp.getCampInfo().endDate;
        for (Camp camp : registeredCamps) {
            Calendar startDate = camp.getCampInfo().startDate;
            Calendar endDate = camp.getCampInfo().endDate;
            if (newStartDate.before(endDate) && newEndDate.after(startDate)) {
                // There's a clash in dates
                System.out.println("There is a clash in your registered camp!");
                return false;
            }
        }

        // checking if camp is full
        int committeeSlots = selectedCamp.getCampInfo().campCommitteeSlots;
        int allSlots = selectedCamp.getCampInfo().totalSlots;
        int allSlotsRemaining = allSlots - selectedCamp.getCampCommittee().size() - selectedCamp.getStudents().size();
        int commSlotsremaining = committeeSlots - selectedCamp.getCampCommittee().size();
        if (registerChoice == 2 && commSlotsremaining <= 0) {
            System.out.println("Camp committee slot is full!");
            return false;
        }
        if (allSlotsRemaining <= 0) {
            System.out.println("Camp slots are full");
            return false;
        }

        // checking if registration is closed or not
        Calendar regClosingDate = selectedCamp.getCampInfo().regClosingDate;
        Calendar currentDate = View.getCurrentDate();
        if (regClosingDate.compareTo(currentDate) < 0) {
            System.out.println("Camp registration is already closed!");
            return false;
        }

        // checking if banned
        for (Student tempStudent : selectedCamp.getBannedStudents()) {
            if (tempStudent.getUserID().equals(user.getUserID())) {
                System.out.println("Student previously quit from the camp. ");
                System.out.println(" Not allowed to register again!");
                return false;
            }
        }

        // register time
        if (registerChoice == 1) {
            // camp attendee
            selectedCamp.addStudent((Student) user);
            if (user instanceof CommitteeMember) {
                ((CommitteeMember) user).addRegisteredCamp(selectedCamp);
            } else if (user instanceof Student) {
                ((Student) user).addRegisteredCamp(selectedCamp);
            }
        } else if (registerChoice == 2) {
            // camp committee - only student can be here
            // remove the old student class
            Database.getStudentList().remove((Student) user);
            // add the new comm member class
            CommitteeMember newCommitteeMember = new CommitteeMember(((Student) user), selectedCamp);
            newCommitteeMember.addRegisteredCamp(selectedCamp);
            Database.getCommitteeMemberList().add(newCommitteeMember);
            // update camp
            selectedCamp.addCampCommittee(newCommitteeMember);
        }
        return true;
    }

    public static void viewRegisteredCamps() {
        // this function will be used for Staff viewCreatedCamps as well
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // have some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return;
        }

        // get registeredCamps
        ArrayList<Camp> registeredCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            registeredCamps.addAll(((CommitteeMember) user).getRegisteredCamps());
        } else if (user instanceof Student) {
            registeredCamps.addAll(((Student) user).getRegisteredCamps());
        } else if (user instanceof Staff) {
            registeredCamps.addAll(((Staff) user).getCreatedCamps());
        }

        // check if registeredCamps empty
        if (registeredCamps.isEmpty()) {
            System.out.println("No registered camps available yet");
            return;
        }

        // filtering process
        ArrayList<Camp> filteredCamps = new ArrayList<>(CampManager.filterCampView(registeredCamps));
        if (registeredCamps.isEmpty()) {
            System.out.println("Too much filter! No result");
            return;
        }

        // print out all camps to get user preferred camp view
        System.out.println("Camp List: ");
        Collections.sort(filteredCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : filteredCamps) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }
        // get user input for camp view
        System.out.println("Enter the camp ID to view details: ");
        int campID = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campID, filteredCamps);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return;
        }

        View.clearScreen();
        System.out.println("Camp Details: ");
        System.out.print("Camp Role: "); // print out the camp role based on user instanceof
        if (user instanceof CommitteeMember) {
            System.out.println("Camp Committee");
            printCampDetailsForStaffs(selectedCamp);
        } else if (user instanceof Student) {
            System.out.println("Attendees");
            printCampDetailsForStudents(selectedCamp);
        } else if (user instanceof Staff) {
            System.out.println("Staff");
            printCampDetailsForStaffs(selectedCamp);
        }
        return;
    }

    public static boolean unregisterCamp() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // have some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return false;
        }
        if (user instanceof Staff) {
            System.out.println("This view is for students only");
            return false;
        }

        // get registeredCamps
        ArrayList<Camp> registeredCamps = new ArrayList<>();
        if (user instanceof CommitteeMember) {
            registeredCamps.addAll(((CommitteeMember) user).getRegisteredCamps());
            // needs to remove own committee camp since cannot unregister from those
            registeredCamps.remove(((CommitteeMember) user).getRegisteredCampAsComm());
        } else if (user instanceof Student) {
            registeredCamps.addAll(((Student) user).getRegisteredCamps());
        }

        // check if registeredCamps empty
        if (registeredCamps.isEmpty()) {
            System.out.println("No camps available yet");
            return false;
        }

        // print out all camps for users to unregister from
        System.out.println("Camp List: ");
        Collections.sort(registeredCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));
        for (Camp camp : registeredCamps) {
            System.out.println("(" + (camp.getCampInfo().campID) + ") Camp "
                    + camp.getCampInfo().campName);
        }

        // get user input for camp to unregister
        System.out.println("Enter the camp ID to unregister from: ");
        int campID = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campID, registeredCamps);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return false;
        }

        if (user instanceof CommitteeMember) {
            // manage camp
            selectedCamp.removeStudent((CommitteeMember) user);
            selectedCamp.addBannedStudent((CommitteeMember) user);
            // manage student
            ((CommitteeMember) user).removeRegisteredCamp(selectedCamp);
        } else if (user instanceof Student) {
            // manage camp
            selectedCamp.removeStudent((Student) user);
            selectedCamp.addBannedStudent((Student) user);
            // manage student
            ((Student) user).removeRegisteredCamp(selectedCamp);
        }

        return true;
    }

    public static boolean createCamp(String campName, Calendar startDate,
            Calendar endDate, Calendar regClosingDate, Faculty userGroup, String location, int totalSlots,
            int campCommitteeSlots, String description, boolean visibility) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // if check
        if (user instanceof Student || user instanceof CommitteeMember) {
            System.out.println("Only staff is allowed to edit");
            return false;
        }

        // checking if input is correct
        if (startDate.compareTo(endDate) > 0) {
            System.out.println("Error! Start date must be before end date!");
            return false;
        }
        if (startDate.compareTo(regClosingDate) < 0) {
            System.out.println("Error! Start date must come after registration date!");
            return false;
        }
        if (regClosingDate.compareTo(View.getCurrentDate()) < 0) {
            System.out.println("Error! Closing date is before today, camp cannot be registered for!");
            return false;
        }

        // create camp process
        Staff staff = (Staff) user;
        CampInformation campInfo = new CampInformation(campName, startDate, endDate, regClosingDate,
                userGroup, location, totalSlots, campCommitteeSlots, description, staff);
        Camp camp = new Camp(campInfo, visibility);

        // saving camp
        staff.addCamp(camp);
        Database.getCampList().add(camp);
        // havent add save file for camp yet

        // print out camp details
        View.clearScreen();
        System.out.println("Camp created! Details: ");
        CampManager.printCampDetailsForStaffs(camp);
        return true;

    }

    public static boolean editCamp() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // some if checks
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps to edit");
            return false;
        }
        if (user instanceof Student || user instanceof CommitteeMember) {
            System.out.println("Only staff is allowed to edit");
            return false;
        }

        // get campsByStaff
        Staff staff = (Staff) user;
        ArrayList<Camp> campsByStaff = new ArrayList<>(staff.getCreatedCamps());
        Collections.sort(campsByStaff, Comparator.comparing(camp -> camp.getCampInfo().campID));

        // if check
        if (campsByStaff.isEmpty()) {
            System.out.println("No created camps to edit");
            return false;
        }

        // print out camps list to choose to edit
        System.out.println("Camp List: ");
        for (Camp camp : campsByStaff) {
            System.out.println(
                    "(" + (camp.getCampInfo().campID) + ") Camp " + camp.getCampInfo().campName);
        }

        // get user input
        System.out.println("Enter the camp ID to edit: ");
        int campIndex = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campIndex, campsByStaff);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return false;
        }

        // editing process
        System.out.println("Editing Camp Details for: " + selectedCamp.getCampInfo().campName);
        System.out.println("(1) Camp Name: " + selectedCamp.getCampInfo().campName);
        System.out
                .println("(2) Start Date: "
                        + View.getStringFromCalendar(selectedCamp.getCampInfo().startDate));
        System.out
                .println(
                        "(3) End Date: " + View.getStringFromCalendar(selectedCamp.getCampInfo().endDate));
        System.out.println("(4) Reg Closing Date: "
                + View.getStringFromCalendar(selectedCamp.getCampInfo().regClosingDate));
        System.out.println("(5) User Group: " + selectedCamp.getCampInfo().userGroup);
        System.out.println("(6) Location: " + selectedCamp.getCampInfo().location);
        System.out.println("(7) Total Slots: " + selectedCamp.getCampInfo().totalSlots);
        System.out.println("(8) Camp Committee Slots: " + selectedCamp.getCampInfo().campCommitteeSlots);
        System.out.println("(9) Description: " + selectedCamp.getCampInfo().description);
        System.out.println("(10) Visibility: " + selectedCamp.getVisibility());

        // get user choice to edit
        System.out.println("Enter the attributes to edit (1-10): ");
        int editChoice = View.readInteger(1, 10);
        switch (editChoice) {
            case 1:
                System.out.println("Enter the new camp name: ");
                selectedCamp.getCampInfo().campName = View.readString();
                break;
            case 2:
                System.out.println("Enter the new camp start date: ");
                Calendar startDate = View.readCalendar();
                if (startDate.compareTo(selectedCamp.getCampInfo().endDate) > 0) {
                    System.out.println("Error! Start date must be before end date!");
                    return false;
                }
                if (startDate.compareTo(selectedCamp.getCampInfo().regClosingDate) < 0) {
                    System.out.println("Error! Start date must come after registration date!");
                    return false;
                }
                if (startDate.compareTo(View.getCurrentDate()) < 0) {
                    System.out.println("Error! Closing date is before today, camp cannot be registered for!");
                    return false;
                }
                selectedCamp.getCampInfo().startDate = startDate;
                break;
            case 3:
                System.out.println("Enter the new camp end date: ");
                Calendar endDate = View.readCalendar();
                if (endDate.compareTo(selectedCamp.getCampInfo().startDate) < 0) {
                    System.out.println("Error! Start date must be before end date!");
                    return false;
                }
                if (endDate.compareTo(View.getCurrentDate()) < 0) {
                    System.out.println("Error! Closing date is before today, camp cannot be registered for!");
                    return false;
                }
                selectedCamp.getCampInfo().endDate = endDate;
                break;
            case 4:
                System.out.println("Enter the new registration closing date: ");
                Calendar regClosingDate = View.readCalendar();
                if (regClosingDate.compareTo(View.getCurrentDate()) < 0) {
                    System.out.println("Error! Closing date is before today, camp cannot be registered for!");
                    return false;
                }
                if (regClosingDate.compareTo(selectedCamp.getCampInfo().startDate) > 0) {
                    System.out.println("Error! Closing date must be before start date!");
                    return false;
                }
                selectedCamp.getCampInfo().regClosingDate = regClosingDate;
                break;
            case 5:
                System.out.println("Enter the new user group: ");
                if ((selectedCamp.getStudents().size() + selectedCamp.getCampCommittee().size()) > 0) {
                    System.out.println("Error! Cannot edit user group with students inside the camp already");
                    return false;
                }
                selectedCamp.getCampInfo().userGroup = View.readEnumFaculty();
                break;
            case 6:
                System.out.println("Enter the new camp location: ");
                selectedCamp.getCampInfo().location = View.readString();
                break;
            case 7:
                System.out.println("Enter the new total slots: ");
                int totalSlots = View.readInteger();
                if (totalSlots < (selectedCamp.getStudents().size() + selectedCamp.getCampCommittee().size())) {
                    System.out.println("Error! Camp slots currently is already bigger than inputted slots");
                    return false;
                }
                selectedCamp.getCampInfo().totalSlots = totalSlots;
                break;
            case 8:
                System.out.println("Enter the new camp committee slots: ");
                int commSlots = View.readInteger(0, 10);
                if (commSlots < selectedCamp.getCampCommittee().size()) {
                    System.out.println("Error! Camp committee slots currently is already bigger than inputted slots");
                    return false;
                }
                selectedCamp.getCampInfo().totalSlots = commSlots;
                break;
            case 9:
                System.out.println("Enter the new description: ");
                selectedCamp.getCampInfo().description = View.readString();
                break;
            case 10:
                System.out.println("Enter the new visibility status: ");
                boolean newVisibility = View.readBoolean();
                if (newVisibility == false) {
                    if (!selectedCamp.getCampCommittee().isEmpty() || !selectedCamp.getStudents().isEmpty()) {
                        // cannot change visibility to false if there are students registered
                        System.out
                                .println("Cannot update the visibility to false if there are students registered");
                        return false;
                    } else {
                        // no students registered, thus can change
                        selectedCamp.setVisibility(newVisibility);
                    }
                } else {
                    // can always turn on
                    selectedCamp.setVisibility(newVisibility);
                }
                break;
            default:
                break;
        }
        return true;

    }

    public static boolean deleteCamp() {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // some if check
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps to delete");
            return false;
        }
        if (user instanceof Student || user instanceof CommitteeMember) {
            System.out.println("Only staff is allowed to delete");
            return false;
        }

        // get campsByStaff
        Staff staff = (Staff) user;
        ArrayList<Camp> campsByStaff = new ArrayList<>(staff.getCreatedCamps());
        Collections.sort(campsByStaff, Comparator.comparing(camp -> camp.getCampInfo().campID));
        if (campsByStaff.isEmpty()) {
            System.out.println("No created camps to delete");
            return false;
        }

        // print out camp list for staff to choose
        System.out.println("Camp List: ");
        for (Camp camp : campsByStaff) {
            System.out
                    .println("(" + (camp.getCampInfo().campID) + ") Camp " + camp.getCampInfo().campName);
        }

        // get user input for camp deletion
        System.out.println("Enter the camp ID to delete the camp: ");
        int campIndex = View.readInteger();
        Camp selectedCamp = CampManager.findCampByID(campIndex, campsByStaff);
        if (selectedCamp == null) {
            System.out.println("Camp not found with the provided ID.");
            return false;
        }

        // check if camp is not empty
        if (!selectedCamp.getCampCommittee().isEmpty() || !selectedCamp.getStudents().isEmpty()) {
            System.out.println("Cannot delete a camp if there are students registered");
            return false;
        }

        // deletion process
        staff.removeCamp(selectedCamp);
        Database.getCampList().remove(selectedCamp);
        // havent add save file for camp yet
        return true;

    }

    /*
     * below here are private/protected methods
     */

    // make protected modifier so the other controller can utilize it
    protected static ArrayList<Camp> getUserViewCamps(Student student) {
        // use polymorphism since this works for Student and CommitteeMember
        ArrayList<Camp> userViewCamps = new ArrayList<>();
        for (Camp camp : Database.getCampList()) {
            if (camp.getVisibility()) {
                if (camp.getCampInfo().userGroup.name().equals("ALL")
                        || camp.getCampInfo().userGroup.equals(student.getFaculty())) {
                    userViewCamps.add(camp);
                }
            }
        }
        return userViewCamps;
    }

    // make protected modifier so the other controller can utilize it
    protected static void printCampDetailsForStudents(Camp camp) {
        System.out.println("Camp ID: " + camp.getCampInfo().campID);
        System.out.println("Camp Name: " + camp.getCampInfo().campName);
        System.out.println("Start Date: " + View.getStringFromCalendar(camp.getCampInfo().startDate));
        System.out.println("End Date: " + View.getStringFromCalendar(camp.getCampInfo().endDate));
        System.out
                .println("Reg Closing Date: " + View.getStringFromCalendar(camp.getCampInfo().regClosingDate));
        System.out.println("User Group: " + camp.getCampInfo().userGroup);
        System.out.println("Location: " + camp.getCampInfo().location);
        int slotsRemaining = camp.getCampInfo().totalSlots - camp.getCampCommittee().size()
                - camp.getStudents().size();
        System.out.println("Slots remaining: " + slotsRemaining + "/" + camp.getCampInfo().totalSlots);
        int commSlotsremaining = camp.getCampInfo().campCommitteeSlots - camp.getCampCommittee().size();
        System.out.println("Camp Committee Slots Remaining: " + commSlotsremaining + "/"
                + camp.getCampInfo().campCommitteeSlots);
        System.out.println("Description: " + camp.getCampInfo().description);
        System.out.println("Staff In Charge: " + camp.getCampInfo().staffInCharge.getName());
        System.out.println("");
    }

    // make protected modifier so the other controller can utilize it
    protected static void printCampDetailsForStaffs(Camp camp) {
        CampInformation campInfo = camp.getCampInfo();
        System.out.println("Camp ID: " + campInfo.campID);
        System.out.println("Camp Name: " + campInfo.campName);
        System.out.println("Start Date: " + View.getStringFromCalendar(campInfo.startDate));
        System.out.println("End Date: " + View.getStringFromCalendar(campInfo.endDate));
        System.out
                .println("Reg Closing Date: " + View.getStringFromCalendar(campInfo.regClosingDate));
        System.out.println("User Group: " + camp.getCampInfo().userGroup);
        System.out.println("Location: " + camp.getCampInfo().location);
        System.out.println("Total Slots: " + camp.getCampInfo().totalSlots);
        System.out.println("Camp Committee Slots: " + camp.getCampInfo().campCommitteeSlots);
        System.out.println("Description: " + camp.getCampInfo().description);
        System.out.println("Staff In Charge: " + camp.getCampInfo().staffInCharge.getName());
        System.out.println("");
        System.out.println("Registered Students: ");
        if (camp.getStudents() == null) {
            System.out.println("No registered students yet");
        } else {
            for (Student student : camp.getStudents()) {
                System.out.println(student.getName() + " from " + student.getFaculty());
            }
        }
        System.out.println("");
        System.out.println("Registered Camp Committee: ");
        if (camp.getStudents() == null) {
            System.out.println("No registered camp committee yet");
        } else {
            for (CommitteeMember comm : camp.getCampCommittee()) {
                System.out.println(comm.getName() + " from " + comm.getFaculty());
            }
        }
        System.out.println("");
        System.out.println("Camp Visibility: " + (camp.getVisibility() == true ? "Open" : "Closed"));
    }

    private static ArrayList<Camp> filterCampView(ArrayList<Camp> allCamps) {
        // filtering process
        int filterChoice;
        ArrayList<Camp> filteredCamps = new ArrayList<>();

        // ask user for filter input
        View.clearScreen();
        System.out.println("Do you want to filter (1-2): (1) Yes , (2) No");
        filterChoice = View.readInteger(1, 2);
        if (filterChoice == 2) {
            return allCamps;
        }
        System.out.println("Filtering out for: ");
        System.out.println("(1) Camp Name");
        System.out.println("(2) Camp User Group");
        System.out.println("(3) Camp Location");
        System.out.println("(4) Camp Description");
        System.out.println("(5) Camp Date Range");
        System.out.println("(6) Staff In Charge Name");
        System.out.println("(7) Staff In Charge Faculty");
        System.out.println("(8) None");
        System.out.println("Enter the attributes to filter (1-8): ");
        int filterAttributeChoice = View.readInteger(1, 8);
        if (filterAttributeChoice == 8)
            return allCamps;

        String filterWord;
        if (filterAttributeChoice == 5) {
            filterWord = "";
        } else {
            System.out.println("What word do you want to filter: (ex: SCSE/Lim/NS)");
            filterWord = View.readString().toLowerCase();
        }

        // initiate switch for filtering:
        // to explain the code, if any camp has its filter category matching, it will be
        // added to the printed list.
        // stream will allow aggregate operations (->)
        // and collect(Collectors.toList()) turns the filtered elements to a list
        switch (filterAttributeChoice) {
            case 1:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().campName.toLowerCase().contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            case 2:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().userGroup.name().toLowerCase().contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            case 3:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().location.toLowerCase().contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            case 4:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().description.toLowerCase().contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            case 5:
                System.out.println("Enter the start of date range for the camp search: ");
                Calendar startDate = View.readCalendar();
                System.out.println("Enter the end of date range for the camp search: ");
                Calendar endDate = View.readCalendar();
                // only take camps that are within the date range
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> {
                            Calendar campStartDate = camp.getCampInfo().startDate;
                            Calendar campEndDate = camp.getCampInfo().endDate;
                            return campStartDate.compareTo(startDate) >= 0 &&
                                    campEndDate.compareTo(endDate) <= 0;
                        })
                        .collect(Collectors.toList()));
                break;
            case 6:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().staffInCharge.getName().toLowerCase()
                                .contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            case 7:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampInfo().staffInCharge.getFaculty().name().toLowerCase()
                                .contains(filterWord))
                        .collect(Collectors.toList()));
                break;
            default:
                break;
        }
        View.clearScreen();
        return filteredCamps;
    }

    protected static Camp findCampByID(int campID, ArrayList<Camp> campList) {
        for (Camp camp : campList) {
            if (camp.getCampInfo().campID == campID) {
                return camp;
            }
        }
        return null;
    }

}
