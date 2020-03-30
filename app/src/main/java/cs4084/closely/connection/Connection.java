package cs4084.closely.connection;

import android.os.Parcel;
import android.os.Parcelable;

public class Connection implements Parcelable {
    private String username;
    private String bio;
    private String userID;

    public Connection() {
    }

    public Connection(String username, String bio, String userID) {
        this.username = username;
        this.bio = bio;
        this.userID = userID;
    }

    public Connection(Parcel parcel) {
        username = parcel.readString();
        bio = parcel.readString();
        userID = parcel.readString();
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getUserID() {
        return userID;
    }

    public static final Parcelable.Creator<Connection> CREATOR = new Parcelable.Creator<Connection>() {
        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(bio);
        dest.writeString(userID);
    }
}
