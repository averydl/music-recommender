package org.derek.assignment4;

public class Artist extends Person {
    private String url;
    private String pictureUrl;
    private String name;

    public Artist(int id, String name, String url, String pictureUrl) {
        super(id);
        this.name = name;
        this.url = url;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public String toString() {
        return "Artist: " + this.getName() + " Id: " + this.getId();
    }
}
