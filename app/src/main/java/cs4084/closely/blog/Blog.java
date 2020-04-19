package cs4084.closely.blog;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blog implements Parcelable {
    private String title;
    private String subtitle;
    private String body;
    private String author;
    private String userID;
    private String blogImage;
    private String documentId;
    private Timestamp datePosted;
    private Map<String, String> comments = new HashMap<>();

    public Blog() {
    }

    public Blog(String title, String subtitle, String body, String author, String userID, Map<String, String> comments) {
        this.title = title;
        this.subtitle = subtitle;
        this.body = body;
        this.author = author;
        this.userID = userID;
        this.comments = comments;
    }

    public Blog(Parcel parcel) {
        title = parcel.readString();
        subtitle = parcel.readString();
        body = parcel.readString();
        author = parcel.readString();
        userID = parcel.readString();
    }
    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public String getUserID() {
        return userID;
    }

    public Timestamp getDatePosted() {
        return datePosted;
    }

    public String toString() {
        return title + ", " + author + "\n";
    }

    public static final Parcelable.Creator<Blog> CREATOR = new Parcelable.Creator<Blog>() {
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(body);
        dest.writeString(author);
        dest.writeString(userID);
    }
    public String getBlogImage() {
        return blogImage;
    }

    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<Comment> getCommentList() {
        List<Comment> commentList = new ArrayList<>();

        for (Map.Entry<String, String> entry : comments.entrySet()) {
            commentList.add(new Comment(entry.getKey(),entry.getValue()));
        }
        return commentList;
    }
}
