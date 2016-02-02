package models;

import java.io.Serializable;

/**
 * Created by rahul on 02-02-2016.
 * Class that corresponds to gson parsed objects belonging to a JSONArray.
 * Has getter and setter methods.
 * A List<Details> is declared in the DataSource class.
 * Implements Serializable so that it can be saved in a Bundle during orientation changes.
 */
public class Details implements Serializable {

    // fields have to correspond with JSON feed's keys to be used with gson.
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
