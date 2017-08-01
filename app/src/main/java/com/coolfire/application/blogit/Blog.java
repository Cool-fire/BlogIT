package com.coolfire.application.blogit;

/**
 * Created by upendra on 7/14/2017.
 */

public class Blog {
    private String Title,Description,Image;

    public Blog(String title, String description, String image) {
        this.Title = title;
        this.Description = description;
        this.Image = image;
    }

    public Blog() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
