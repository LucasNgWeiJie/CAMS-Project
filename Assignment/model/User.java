package Assignment.model;

public class User {
    private String name;
    private String user_ID;
    private Faculty faculty;
    private String password;

    // faculty enum
    public enum Faculty {
        SCSE, SSS, NBS, EEE, ADM, MAE, MSE, CEE, CCEB, ALL
    }

    // construct user, assume default password is just password
    public User(String name, String userid, Faculty fac) {
        this.name = name;
        this.user_ID = userid;
        this.faculty = fac;
        this.password = "password";
    }

    // setters
    public void setName(String name) {
        this.name = name;
    }

    public void setUserID(String id) {
        this.user_ID = id;
    }

    public void setFaculty(Faculty fac) {
        this.faculty = fac;
    }

    public void setPassword(String pw) {
        this.password = pw;
    }

    // getters
    public String getName() {
        return this.name;
    }

    public String getUserID() {
        return this.user_ID;
    }

    public Faculty getFaculty() {
        return this.faculty;
    }

    public String getPassword() {
        return this.password;
    }

    // methods
    public boolean validatePassword(String passwordAttempt) {
        return this.password.equals(passwordAttempt);
    }
}
