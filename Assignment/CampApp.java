package Assignment;

import Assignment.view.View;

import Assignment.database.Database;

import Assignment.view.UserView;

public class CampApp {

    public static void main(String[] args) {
        //load database
        Database.start();
        // Main App View is here
        View.clearScreen();
        UserView.viewSwitch();

        // save files before closing
        Database.saveAllFiles();
    }
}
