package Assignment.view;

import java.util.Calendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import Assignment.model.User;

/**
 * AbstractView
 */
public abstract class View   {
    public static final Scanner scan = new Scanner(System.in);

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

        } catch (Exception e) {
            System.out.println("Error while clearing the screen: " + e.getMessage());
        }
    }

    public static void pressAnyToContinue() {
        System.out.println("Press Enter key to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    public static int readInteger(int min, int max) {
        while (true) {
            try {
                int input = -1;
                input = scan.nextInt();
                scan.nextLine();
                if (input < min || input > max) {
                    System.out.println("Out of range! Enter again!");
                } else {
                    return input;
                }
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Error. Please input the apt integer!");
            }
        }
    }

    public static int readInteger() {
        while (true) {
            try {
                int input = -1;
                input = scan.nextInt();
                scan.nextLine();
                return input;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Error. Please input the apt integer!");
            }
        }
    }

    public static String readString() {
        String input = scan.nextLine();
        return input;
    }

    public static boolean readBoolean() {
        while (true) {
            try {
                boolean input = scan.nextBoolean();
                return input;
            } catch (Exception e) {
                scan.nextLine();
                System.out.println("Error. Please input the apt boolean!");
            }
        }
    }

    public static User.Faculty readEnumFaculty() {
        while (true) {
            try {
                System.out.println("Enter one of the following values:");
                for (User.Faculty value : User.Faculty.values()) {
                    System.out.print(value + " ");
                }
                System.out.println();

                String input = readString().toUpperCase();
                return User.Faculty.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input! Please try again.");
            }
        }
    }

    public static Calendar readCalendar() {
        Calendar calendar = Calendar.getInstance();
        while (true) {
            System.out.println("(date in this format: 'yyyy-MM-dd')");
            String date = scan.nextLine();
            try {
                String[] parts = date.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                return calendar;
            } catch (Exception e) {
                System.out.println("Error. Please input the apt date!");
            }
        }
    }

    public static String getStringFromCalendar(Calendar calendar) {// calendarToString
        if (calendar != null) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 as months are zero-based
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return String.format("%04d-%02d-%02d", year, month, day);
        } else {
            return "Invalid date";
        }
    }

    public static Calendar getCurrentDate() {
        return Calendar.getInstance();
    }
    
}
