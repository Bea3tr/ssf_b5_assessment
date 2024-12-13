package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class Notice {

    @NotNull(message="Title cannot be null")
    @NotEmpty(message="Title cannot be empty")
    @Size(min=3, max=128, message="Title must be between 3 and 128 characters")
    private String title;

    @NotNull(message="Poster cannot be null")
    @NotEmpty(message="Poster cannot be empty")
    @Email(message="Must be a well-formed email address")
    private String poster;

    @NotNull(message="Please indicate post date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Future(message="Post date must be in the future")
    private Date postDate;

    @NotNull(message="Please select one or more categories")
    @NotEmpty(message="Please select one or more categories")
    private List<String> categories;

    @NotNull(message="Please provide contents for the notice")
    @NotEmpty(message="Please provide contents for the notice")
    private String text;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    
    public String getPoster() {return poster;}
    public void setPoster(String poster) {this.poster = poster;}
    
    public Date getPostDate() {return postDate;}
    public void setPostDate(Date postDate) {this.postDate = postDate;}
    
    public List<String> getCategories() {return categories;}
    public void setCategories(List<String> categories) {this.categories = categories;}
    
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    
    @Override
    public String toString() {
        return "Notice [title=" + title + ", poster=" + poster + ", postDate=" + postDate + ", categories=" + categories
                + ", text=" + text + "]";
    }
    
}
