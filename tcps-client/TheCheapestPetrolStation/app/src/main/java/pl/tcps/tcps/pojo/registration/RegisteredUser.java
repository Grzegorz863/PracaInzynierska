package pl.tcps.tcps.pojo.registration;

import com.google.gson.annotations.SerializedName;

public class RegisteredUser {

    @SerializedName("user_name")
    private String userName;

    @SerializedName("user_role")
    private String userRole;

    @SerializedName("is_enabled")
    private Boolean isEnabled;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email")
    private String email;


    public RegisteredUser(String userName, String userRole, Boolean isEnabled, String firstName, String lastName, String email) {
        this.userName = userName;
        this.userRole = userRole;
        this.isEnabled = isEnabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}