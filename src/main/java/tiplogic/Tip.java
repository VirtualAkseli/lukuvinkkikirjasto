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

    public Tip(String author, String tipName) {
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

    public String getTagsAsString() {
        String returnable = "";
        for (Tag t : tags) {
            returnable = returnable + "[" + t.getTagName() + "] ";
        }

        return returnable;
    }

    public String getCoursesAsString() {
        String returnable = "";
        for (Course c : courses) {
            returnable = returnable + "[" + c.getCourseName() + "] ";
        }
        return returnable;
    }

    @Override
    public String toString() {
        switch (tipType) {
            case "book":
                return "Tyyppi: Kirja"
                        +"\nKirjailija: " + author
                        + "\nKirjan nimi: " + tipName
                        + (identifier.equals("") ? "" : ("\nISBN: " + identifier))
                        + (tags.isEmpty() ? "" : "\n" + "Tagit: " + getTagsAsString())
                        + (courses.isEmpty() ? "" : "\n" + "Liittyvät kurssit: " + getCoursesAsString())
                        + (comments.equals("") ? "" : ("\nKommentti: " + comments))
                        + "\n";
            case "blogpost":
                return "Tyyppi: Blogi"
                        +(author.equals("") ? "" : "\nBlogin pitäjä: " + author)
                        + "\nBlogin nimi: " + tipName
                        + "\nBlogin URL: " + url
                        + (tags.isEmpty() ? "" : "\n" + "Tagit: " + getTagsAsString())
                        + (courses.isEmpty() ? "" : "\n" + "Liittyvät kurssit: " + getCoursesAsString())
                        + (comments.equals("") ? "" : ("\nKommentti: " + comments))
                        + "\n";
            case "podcast":
                return "Tyyppi: Podcast"
                        +(author.equals("") ? "" : "\nPodcastin pitäjä: " + author)
                        + "\nPodcastin nimi: " + tipName
                        + "\nPodcastin URL: " + url
                        + (identifier.equals("") ? "" : ("\nJakson nimi: " + identifier))
                        + (tags.isEmpty() ? "" : "\n" + "Tagit: " + getTagsAsString())
                        + (courses.isEmpty() ? "" : "\n" + "Liittyvät kurssit: " + getCoursesAsString())
                        + (comments.equals("") ? "" : ("\nKommentti: " + comments))
                        + "\n";
            case "video":
                return  "Tyyppi: Video"
                        +(author.equals("") ? "" : "\nVideon tekijä: " + author)
                        + "\nVideon nimi: " + tipName
                        + "\nPodcastin URL: " + url
                        + (tags.isEmpty() ? "" : "\n" + "Tagit: " + getTagsAsString())
                        + (courses.isEmpty() ? "" : "\n" + "Liittyvät kurssit: " + getCoursesAsString())
                        + (comments.equals("") ? "" : ("\nKommentti: " + comments))
                        + "\n";
            default:
               return "";
        }
    }
}
