package tiplogic;

import java.util.ArrayList;
import java.util.List;

public class Tip implements Mappable {
     Long id;
     String tipType;
     String author;
     String tipName;
     String identifier;
     String url;
     String comments;
     List<Tag> tags = new ArrayList<>();
     List<Course> courses = new ArrayList<>();

    public Tip() {
    }

    public Tip(String tipName, String author) {
        this.tipName = tipName;
        this.author = author;
    }

    public Tip(String tipType) {
        this.tipType = tipType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipType() {
        return tipType;
    }

    public void setTipType(String tipType) {
        this.tipType = tipType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Tip{" + "id=" + id + ", tipType='" + tipType + '\'' + ", author='" + author + '\'' + ", tipName='" + tipName + '\'' + ", identifier='" + identifier + '\'' + ", url='" + url + '\'' + ", comments='" + comments + '\'' + ", tags=" + tags + ", courses=" + courses + '}';
    }

}
