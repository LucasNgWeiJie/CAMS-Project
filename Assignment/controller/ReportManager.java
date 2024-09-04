package Assignment.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import Assignment.database.Database;
import Assignment.database.UserSession;
import Assignment.model.Camp;
import Assignment.model.CommitteeMember;
import Assignment.model.Enquiry;
import Assignment.model.Staff;
import Assignment.model.Student;
import Assignment.model.User;
import Assignment.view.View;

public class ReportManager {
    public static boolean produceCampReport(String reportName) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // get allCamps
        ArrayList<Camp> allCamps = new ArrayList<>();
        if (user instanceof Staff) {
            allCamps.addAll(((Staff) user).getCreatedCamps());
        } else if (user instanceof CommitteeMember) {
            allCamps.add(((CommitteeMember) user).getRegisteredCampAsComm());
        }

        // check if allCamps empty
        if (allCamps.isEmpty()) {
            System.out.println("No camps to produce report of");
            return false;
        }

        // filter process
        ArrayList<Camp> filteredCamps = filterCampReport(allCamps);
        Collections.sort(filteredCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));

        // check all camps again if its empty
        if (filteredCamps.isEmpty()) {
            System.out.println("Too much filter! No result");
            return false;
        }

        // start writing a report - in csv
        File file = new File("Assignment\\report\\" + reportName + ".csv");
        try {
            // create FileWriter and CSVWriter
            FileWriter writer = new FileWriter(file);

            // iterate through all camps
            for (Camp camp : filteredCamps) {
                // add all initial camp details
                writer.append("Camp ID," + camp.getCampInfo().campID + "\n");
                writer.append("Camp Name," + camp.getCampInfo().campName + "\n");
                writer.append("Staff in Charge," + camp.getCampInfo().staffInCharge.getName() + "\n");
                writer.append("Camp User Group," + camp.getCampInfo().userGroup + "\n");
                writer.append("Location," + camp.getCampInfo().location + "\n");
                writer.append("Description," + camp.getCampInfo().description + "\n");
                writer.append("\n");

                // add header for student list
                ArrayList<Student> studentList = new ArrayList<>(camp.getStudents());
                ArrayList<CommitteeMember> commList = new ArrayList<>(camp.getCampCommittee());
                if (studentList.isEmpty() && commList.isEmpty()) {
                    writer.append("(No participants yet)\n");
                    writer.append("\n\n");
                    continue;
                }
                writer.append("Student Name,User ID,Faculty,Roles\n");

                // add student details
                for (Student student : studentList) {
                    writer.append(student.getName() + ",");
                    writer.append(student.getUserID() + ",");
                    writer.append(student.getFaculty() + ",");
                    writer.append("Attendee\n");
                }

                // add committee member details
                for (CommitteeMember comm : commList) {
                    writer.append(comm.getName() + ",");
                    writer.append(comm.getUserID() + ",");
                    writer.append(comm.getFaculty() + ",");
                    writer.append("Committee\n");
                }
                writer.append("\n\n");

            }
            // close out the writer
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static ArrayList<Camp> filterCampReport(ArrayList<Camp> allCamps) {
        // filtering process
        int filterChoice;
        ArrayList<Camp> filteredCamps = new ArrayList<>();

        // ask user for filter input
        View.clearScreen();
        System.out.println("Do you want to filter your report (1-2): (1) Yes , (2) No");
        filterChoice = View.readInteger(1, 2);
        if (filterChoice == 2) {
            return allCamps;
        }
        System.out.println("Filtering out for: ");
        System.out.println("(1) Camp Name");
        System.out.println("(2) Camp User Group");
        System.out.println("(3) Camp Location");
        System.out.println("(4) Camp Description");
        System.out.println("(5) Camp Attendee Name");
        System.out.println("(6) Camp Attendee Faculty");
        System.out.println("(7) Camp Committee Name");
        System.out.println("(8) Camp Committee Faculty");
        System.out.println("(9) None");
        System.out.println("Enter the attributes to filter (1-9): ");
        int filterAttributeChoice = View.readInteger(1, 9);
        if (filterAttributeChoice == 9)
            return allCamps;
        System.out.println("What word do you want to filter: (ex: SCSE/Lim/NS)");
        String filterWord = View.readString().toLowerCase();

        // initiate switch for filtering:
        // to explain the code, if any camp has its filter category matching, it will be
        // added to the printed list.
        // stream will allow aggregate operations (->)
        // , anyMatch returns true if at least one element match the condition
        // , and collect(Collectors.toList()) turns the filtered elements to a list
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
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getStudents().stream()
                                .anyMatch(student -> student.getName().toLowerCase().contains(filterWord)))
                        .collect(Collectors.toList()));
                break;
            case 6:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getStudents().stream()
                                .anyMatch(student -> student.getFaculty().name().toLowerCase().contains(filterWord)))
                        .collect(Collectors.toList()));
                break;
            case 7:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampCommittee().stream()
                                .anyMatch(comm -> comm.getName().toLowerCase().contains(filterWord)))
                        .collect(Collectors.toList()));
                break;
            case 8:
                filteredCamps.addAll(allCamps.stream()
                        .filter(camp -> camp.getCampCommittee().stream()
                                .anyMatch(comm -> comm.getFaculty().name().toLowerCase().contains(filterWord)))
                        .collect(Collectors.toList()));
                break;
            default:
                break;
        }

        return filteredCamps;
    }

    public static boolean produceEnquiryReport(String reportName) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // get allCamps
        ArrayList<Camp> allCamps = new ArrayList<>();
        if (user instanceof Staff) {
            allCamps.addAll(((Staff) user).getCreatedCamps());
        } else if (user instanceof CommitteeMember) {
            allCamps.add(((CommitteeMember) user).getRegisteredCampAsComm());
        }

        // check if allCamps empty
        if (allCamps.isEmpty()) {
            System.out.println("No camps to produce report of");
            return false;
        }

        // filter process
        ArrayList<Camp> filteredCamps = filterEnquiryReport(allCamps);
        Collections.sort(filteredCamps, Comparator.comparing(camp -> camp.getCampInfo().campID));

        // check all camps again if its empty
        if (filteredCamps.isEmpty()) {
            System.out.println("Too much filter! No result");
            return false;
        }

        // start writing a report - in csv
        File file = new File("Assignment\\report\\" + reportName + ".csv");
        try {
            // create FileWriter and CSVWriter
            FileWriter writer = new FileWriter(file);

            // iterate through all camps
            for (Camp camp : filteredCamps) {
                // add all initial camp details
                writer.append("Camp ID," + camp.getCampInfo().campID + "\n");
                writer.append("Camp Name," + camp.getCampInfo().campName + "\n");
                writer.append("Staff in Charge," + camp.getCampInfo().staffInCharge.getName() + "\n");
                writer.append("Camp User Group," + camp.getCampInfo().userGroup + "\n");
                writer.append("Location," + camp.getCampInfo().location + "\n");
                writer.append("Description," + camp.getCampInfo().description + "\n");
                writer.append("\n");

                // check if there is enquiry in the camp
                ArrayList<Enquiry> enqList = new ArrayList<>(camp.getEnquiryList());
                if (enqList.isEmpty()) {
                    writer.append("(No enquiry for this camp)\n");
                    continue;
                }

                // add student details
                Collections.sort(enqList, Comparator.comparing(enq -> enq.getEnquiryID()));
                for (Enquiry enq : enqList) {
                    writer.append("Enquiry ID," + enq.getEnquiryID() + "\n");
                    writer.append("Question," + enq.getQuestion() + "\n");
                    writer.append("Answer," + (enq.isAnswered() ? enq.getAnswer() : "NA") + "\n");
                    writer.append("\n");
                }

                writer.append("\n\n");

            }
            // close out the writer
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static ArrayList<Camp> filterEnquiryReport(ArrayList<Camp> allCamps) {
        // filtering process
        int filterChoice;
        ArrayList<Camp> filteredCamps = new ArrayList<>();

        // ask user for filter input
        View.clearScreen();
        System.out.println("Do you want to filter your report (1-2): (1) Yes , (2) No");
        filterChoice = View.readInteger(1, 2);
        if (filterChoice == 2) {
            return allCamps;
        }
        System.out.println("Filtering out for: ");
        System.out.println("(1) Enquiry Question");
        System.out.println("(2) Enquiry Answer");
        System.out.println("(3) None");
        System.out.println("Enter the attributes to filter (1-3): ");
        int filterAttributeChoice = View.readInteger(1, 3);
        if (filterAttributeChoice == 3)
            return allCamps;
        System.out.println("What word do you want to filter: (ex: date/packing)");
        String filterWord = View.readString().toLowerCase();

        // initiate switch for filtering:
        // for this part, swith case just adds enquery if it contains filterWord
        switch (filterAttributeChoice) {
            case 1:
                for (Camp camp : allCamps)
                    for (Enquiry enq : camp.getEnquiryList())
                        if (enq.getQuestion().toLowerCase().contains(filterWord)) {
                            filteredCamps.add(camp);
                            break;
                        }
                break;
            case 2:
                for (Camp camp : allCamps)
                    for (Enquiry enq : camp.getEnquiryList())
                        if (enq.getAnswer().toLowerCase().contains(filterWord)) {
                            filteredCamps.add(camp);
                            break;
                        }
                break;
            default:
                break;
        }

        return filteredCamps;
    }

    public static boolean produceCommPerformanceReport(String reportName) {
        // get user instance
        User user = UserSession.getInstance().getLoggedInUser();

        // have some check ifs
        if (Database.getCampList().isEmpty()) {
            System.out.println("No camps yet");
            return false;
        }
        if (user instanceof Student || user instanceof CommitteeMember) {
            System.out.println("Only staff is allowed to print camp committee performance report");
            return false;
        }

        // fetch all camps from Database
        ArrayList<CommitteeMember> allComm = new ArrayList<>(Database.getCommitteeMemberList());
        if (allComm.isEmpty()) {
            System.out.println("No camp committee yet");
            return false;
        }
        Collections.sort(allComm, Comparator.comparing(comm -> comm.getUserID()));

        // filtering process
        int filterChoice;
        ArrayList<CommitteeMember> filteredComm = new ArrayList<>();

        // ask user for filter input
        View.clearScreen();
        System.out.println("Do you want to filter your report (1-2): (1) Yes , (2) No");
        filterChoice = View.readInteger(1, 2);
        if (filterChoice == 1) {
            System.out.println("Filtering out for: ");
            System.out.println("(1) Camp Committee Name");
            System.out.println("(2) Camp Committee Faculty");
            System.out.println("(3) Camp Committee Minimum Points");
            System.out.println("(4) None");
            System.out.println("Enter the attributes to filter (1-4): ");
            int filterAttributeChoice = View.readInteger(1, 4);
            System.out.println("What word do you want to filter: (ex: john/SCSE/3)");
            String filterWord = View.readString().toLowerCase();

            // initiate switch for filtering:
            // in here, swith case just adds committee member if it matches with filterWord
            switch (filterAttributeChoice) {
                case 1:
                    for (CommitteeMember comm : allComm)
                        if (comm.getName().toLowerCase().contains(filterWord))
                            filteredComm.add(comm);
                    break;
                case 2:
                    for (CommitteeMember comm : allComm)
                        if (comm.getFaculty().name().toLowerCase().contains(filterWord))
                            filteredComm.add(comm);
                    break;
                case 3:
                    try {
                        for (CommitteeMember comm : allComm)
                            if (comm.getPoints() >= Integer.parseInt(filterWord))
                                filteredComm.add(comm);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for points filtering. Try again");
                    }
                case 4:
                    filteredComm.addAll(allComm);
                    break;
                default:
                    break;
            }

        } else {
            filteredComm.addAll(allComm);
        }

        // check all comm again if its empty
        if (filteredComm.isEmpty()) {
            System.out.println("Too much filter! No result");
            return false;
        }

        // start writing a report - in csv
        File file = new File("Assignment\\report\\" + reportName + ".csv");
        try {
            // create FileWriter and CSVWriter
            FileWriter writer = new FileWriter(file);

            // header for committee member
            writer.append("Student Name,User ID,Faculty,Points,Camp ID,Camp Name,Staff in Charge\n");

            // iterate through all committee member
            for (CommitteeMember comm : filteredComm) {
                // add committee member details
                Camp tempCamp = comm.getRegisteredCampAsComm();
                writer.append(comm.getName() + ",");
                writer.append(comm.getUserID() + ",");
                writer.append(comm.getFaculty() + ",");
                writer.append(comm.getPoints() + ",");
                writer.append(tempCamp.getCampInfo().campID + ",");
                writer.append(tempCamp.getCampInfo().campName + ",");
                writer.append(tempCamp.getCampInfo().staffInCharge.getName());
                writer.append("\n");
            }
            // close out the writer
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
