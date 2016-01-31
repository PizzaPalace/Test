package json;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import constants.Constants;

/**
 * Created by rahul on 30-01-2016.
 */
public class JSONHelper {
    /**
     * This function is no longer used, see Common.fetchDataAndBroadcast
     *
     * @param requestQueue
     * @param url
     * @return
     */
    public static ArrayList<HashMap<String,String>> requestData(RequestQueue requestQueue, String url) {

        ArrayList<HashMap<String,String>> data = null;
        JSONObject response = null;
        RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                requestFuture,
                requestFuture);

        requestQueue.add(request);
        try {
            response = requestFuture.get(1000, TimeUnit.MILLISECONDS);
            data = jsonParser(response);
            Log.v("ser",data.toString());

        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        } catch (TimeoutException e) {

        }
        return data;
    }

    /**
     * Parses the JSON feed into a usable Java datastructure that is tied with
     * the ListView's adapter.
     *
     * @param jsonObject
     * @return ArrayList<HashMap<String,String>> that corresponds to the data associated with
     * the ListView's adapter
     */
    public static ArrayList<HashMap<String,String>> jsonParser(JSONObject jsonObject) {

        try {

            ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

            JSONArray jsonArray = jsonObject.getJSONArray("rows");

            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                HashMap<String,String> map = new HashMap<String,String>();
                JSONObject jObject = jsonArray.getJSONObject(i);

                String title = jObject.getString(Constants.TITLE);
                if(title != null && !title.equals("null")) {

                    map.put(Constants.TITLE, jObject.getString(Constants.TITLE));
                    map.put(Constants.DESCRIPTION, jObject.getString(Constants.DESCRIPTION));
                    map.put(Constants.IMAGE_URL, jObject.getString(Constants.IMAGE_URL));
                    list.add(map);
                }

                map = null;
            }

            return list;
        }
        catch(JSONException exception){
            exception.printStackTrace();
            return null;
        }
        catch(Exception exception){
            return null;
        }
    }

    /**
     * Parses the JSON feed and return a String corresponding to the title of Activity.
     *
     * @param jsonObject
     * @return String that is to be set as the title of the Activity.
     */
    public static String getTitle(JSONObject jsonObject){

        String title;
        try {

            title = jsonObject.getString(Constants.PAGE_TITLE);
            return title;
        }
        catch(JSONException exception){
            exception.printStackTrace();
            return null;
        }
        catch(Exception exception){
            return null;
        }
    }
}
