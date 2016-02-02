package constants;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import listeners.NetworkListener;
import models.DataSource;
import network.VolleySingleton;


/**
 * Created by rahul on 30-01-2016.
 */
public class Common {

    /**
     * Common helper method to display an error message
     *
     * @param context An Activity context to display a Toast
     */
    public static void displayErrorMessage(Context context){
        Toast.makeText(context, "Oops, there is no data to display", Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method that fetches data asynchronously using Volley's RequestQueue
     * and then passes that data to an Activity using an interface listener. Was used
     * in MainActivity before being replaced by fetchDataAndBroadcast()
     *
     * @param _context An Activity context to cast the Listener to and also to display
     *                 Toast messages for errors
     */
    public static void fetchData(Context _context) {

        // Cast the context as a Listener to pass data to Activity
        final NetworkListener listener = (NetworkListener) _context;
        final Context context = _context;
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        // Use Volley to asynchronously fetch data from network
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject _response) {

                        try {
                            final JSONObject response = _response;

                            /* Perform gson parsing on background thread to avoid
                               blocking the ui thread. Note that the listener is called
                               from the background thread and control has to be passed
                               in the ui thread in the listener implementation
                               (to pass data to fragment).This can be performed in other ways */
                            new Thread(new Runnable(){

                                @Override
                                public void run() {

                                    GsonBuilder gsonBuilder  = new GsonBuilder();
                                    gsonBuilder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
                                    Gson gson = gsonBuilder.create();
                                    DataSource dataSource = gson.fromJson(response.toString(), DataSource.class);
                                    //Log.v("output", dataSource.toString());
                                    gsonBuilder = null;
                                    gson = null;
                                    listener.onDataReceived(dataSource);

                                }
                            }).start();

                        } catch (NullPointerException exception) {
                            Common.displayErrorMessage(context);
                            exception.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // If there was error in connecting to the network
                        // repeat network request until data is obtained
                        fetchData(context);
                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        requestQueue.add(jsObjRequest);

    }

}
