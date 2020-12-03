package tiplogic;

public class Tag implements Mappable {
    Long id;
    String tagName;

    public Tag() {
    }

    public Tag(Long id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
