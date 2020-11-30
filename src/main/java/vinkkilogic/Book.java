package vinkkilogic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String relatedCourses;
    private String tags;
    private String comments;

    public Book() {
    }

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getRelatedCourses() {
        return relatedCourses;
    }

    public void setRelatedCourses(String relatedCourses) {
        this.relatedCourses = relatedCourses;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' + ", isbn='" + isbn + '\'' + ", relatedCourses='" + relatedCourses + '\'' + ", tags='" + tags + '\'' + ", comments='" + comments + '\'' + '}';
    }

    public Map<String, Object> getPropertyMap() {
        Map<String, Object> map = new HashMap<>();
        Arrays.stream(this.getClass().getDeclaredFields()).forEach((field -> {
            try { map.put(field.getName(), field.get(this)); }
            catch (IllegalAccessException e) { e.printStackTrace(); }
        }));
        return map;
    }

}
