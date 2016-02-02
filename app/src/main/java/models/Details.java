package models;

/**
 * Created by rahul on 02-02-2016.
 */
public class Details {

    String title;
    String description;
    String imageHref;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public void setImageURL(String url){
        imageHref = url;
    }

    public String getImageURL(){
        return imageHref;
    }

    @Override
    public String toString() {
        return title + " - " + description +" - " + imageHref;
    }

}
