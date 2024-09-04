package Assignment.database;

import Assignment.model.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;

    private UserSession() {

    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void clearSession() {
        loggedInUser = null;
    }
}
