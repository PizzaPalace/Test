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
    /*public static void fetchData(Context _context) {

        // Cast the context as a Listener to pass data to Activity
        final NetworkListener listener = (NetworkListener) _context;
        final Context context = _context;
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        // Use Volley to asynchronously fetch data from network
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            // On successful network query, get data from JSONHelper
                            // class that parses JSON and returns an ArrayList<HashMap<String,String>>
                            ArrayList<Object[]> data = JSONHelper.jsonParser(response);

                            // Check if data obtained is not null else display a Toast
                            if (data != null) {
                                DataSource.setData(data);
                                //passDataToFragment(DataSource.getData());

                                // listener transfers data to Activity
                                listener.onDataReceived(DataSource.getData());
                            } else
                                Common.displayErrorMessage(context);

                            // Check if the title for the page is not null, else display a Toast
                            String title = JSONHelper.getTitle(response);
                            if (title != null) {
                                DataSource.setTitle(title);
                                //getSupportActionBar().setTitle(DataSource.getTitle());

                                // listener transfers data to Activity
                                listener.onTitleReceived(DataSource.getTitle());
                            } else
                                Common.displayErrorMessage(context);

                            //mProgressBar.setVisibility(View.GONE);
                        } catch (NullPointerException exception) {
                            Common.displayErrorMessage(context);
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

    }*/

    /**
     * Helper method that uses Volley's RequestQueue to fetch data over the network.
     * In addition this operation is performed using an IntentService(NetworkService)
     * and BroadcastReceiver (NetworkReceiver).
     *
     * @param _context An Activity context to cast the Listener to and also to display
     *                 Toast messages for errors
     */

    /*public static void fetchDataAndBroadcast(Context _context) {

        final Context context = _context;
        // Get reference to Volley's RequestQueue
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        // Create implicit intent and set action
        final Intent broadcastIntent = new Intent();
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.setAction(NetworkReceiver.RECEIVER_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ArrayList<HashMap<String, String>> data = JSONHelper.jsonParser(response);

                            // Check if data obtained is not null else display a Toast
                            if (data != null) {
                                DataSource.setData(data);
                                // save data in intent
                                broadcastIntent.putExtra(Constants.DATA,data);

                            } else
                                Common.displayErrorMessage(context);

                            // Check if title obtained is not null else display a Toast
                            String title = JSONHelper.getTitle(response);
                            if (title != null) {
                                DataSource.setTitle(title);
                                // save title in intent
                                broadcastIntent.putExtra(Constants.TITLE,title);

                            } else
                                Common.displayErrorMessage(context);

                            // broadcast data and title obtained from network
                            context.sendBroadcast(broadcastIntent);

                        } catch (NullPointerException exception) {
                            Common.displayErrorMessage(context);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // If there was error in connecting to the network
                        // repeat network request until data is obtained
                        fetchDataAndBroadcast(context);

                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        requestQueue.add(jsObjRequest);
    }*/

    public static void fetchData3(Context _context) {

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
                        fetchData3(context);
                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        requestQueue.add(jsObjRequest);

    }



}
