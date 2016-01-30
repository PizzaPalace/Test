package Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rahul on 30-01-2016.
 */
public class DataSource  {

    public static ArrayList<HashMap<String,String>> mData;
    public static String mTitle;


    public static ArrayList<HashMap<String,String>> getData(){
        return mData;
    }

    public static void setData(ArrayList<HashMap<String,String>> data){
        mData = data;
    }

    public static String getTitle(){
        return mTitle;
    }

    public static void setTitle(String title){
        mTitle = title;
    }
}
