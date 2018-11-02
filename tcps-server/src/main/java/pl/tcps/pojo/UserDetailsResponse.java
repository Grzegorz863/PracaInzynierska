package pl.tcps.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetailsResponse {

    @JsonProperty("first_name")
    private String name;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    public UserDetailsResponse() {
    }

    public UserDetailsResponse(String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
