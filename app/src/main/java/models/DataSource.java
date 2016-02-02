package models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rahul on 30-01-2016.
 * Class that functions as the (M) in the MVC for this project.
 * Has getter and setter methods each for the data and title.
 * This class is populated via gson after parsing the network feed.
 * Implements Serializable so that it can be saved in a Bundle during orientation changes.
 */
public class DataSource implements Serializable {

    // fields have to correspond with JSON feed's keys to be used with gson
    private static String title;
    private static List<Details> rows;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public List<Details> getDetails(){
        return rows;
    }

    public void setDetails(List<Details> details){
        rows= details;
    }

    @Override
    public String toString() {
        return title +" - " + rows.toString();
    }
}