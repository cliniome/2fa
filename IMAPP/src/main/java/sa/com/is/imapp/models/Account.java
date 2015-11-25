package sa.com.is.imapp.models;

/**
 * Created by snouto on 25/11/15.
 */
public class Account {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String userName;

    public Account(){}
    public Account(String first,String last , String email , String user)
    {
        this.setFirstName(first);
        this.setLastName(last);
        this.setEmailAddress(email);
        this.setUserName(user);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
