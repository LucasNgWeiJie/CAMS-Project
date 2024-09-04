package Assignment.model;

public class Enquiry {
    // connected to the Camp, access through that
    private static int enquiryCounter = 0;
    private int enquiryID;
    private Camp camp;
    private String question;
    private String answer;

    public Enquiry(String question, Camp camp) {
        this.enquiryID = ++enquiryCounter;
        this.camp = camp;
        this.question = question;
        this.answer = null;
    }

    public int getEnquiryID() {
        return enquiryID;
    }
    // no setter for enquiryID

    // check if answered
    public boolean isAnswered() {
        return this.answer != null;
    }

    public void setAnswer(String s) {
        this.answer = s;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setQuestion(String newQuestion) {
        this.question = newQuestion;
    }

    public String getQuestion() {
        return this.question;
    }

    public Camp getCamp() {
        return camp;
    }
    // no set camp

}
