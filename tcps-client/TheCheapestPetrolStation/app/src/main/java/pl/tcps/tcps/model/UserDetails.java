package pl.tcps.tcps.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDetails implements Parcelable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

    public static final Parcelable.Creator<UserDetails> CREATOR = new Parcelable.Creator<UserDetails>(){

        @Override
        public UserDetails createFromParcel(Parcel source) {
            return new UserDetails(source);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[0];
        }
    };

    public UserDetails() {
        super();
    }

    public UserDetails(String id) {
        this.id = id;
        this.firstName = "Jan";
        this.lastName = "Kowalski";
        this.email = "jakis@email.com";
    }

    public UserDetails(Parcel parcel){
        this.id = parcel.readString();
        this.firstName = parcel.readString();
        this.lastName = parcel.readString();
        this.email = parcel.readString();
    }

    public UserDetails(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
