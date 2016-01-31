package constants;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import json.JSONHelper;
import listeners.NetworkListener;
import models.DataSource;
import network.VolleySingleton;
import receivers.NetworkReceiver;

/**
 * Created by rahul on 30-01-2016.
 */
public class Common {

    public static void displayErrorMessage(Context context){
        Toast.makeText(context, "Oops, there is no data to display", Toast.LENGTH_SHORT).show();
    }

    public static void fetchData(Context _context) {

        final NetworkListener listener = (NetworkListener) _context;
        final Context context = _context;
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ArrayList<HashMap<String, String>> data = JSONHelper.jsonParser(response);

                            if (data != null) {
                                DataSource.setData(data);
                                //passDataToFragment(DataSource.getData());
                                listener.onDataReceived(DataSource.getData());
                            } else
                                Common.displayErrorMessage(context);


                            String title = JSONHelper.getTitle(response);
                            if (title != null) {
                                DataSource.setTitle(title);
                                //getSupportActionBar().setTitle(DataSource.getTitle());
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
                        // TODO Auto-generated method stub
                        //Toast.makeText(getApplicationContext(),"Oops, are you sure you are connected to the internet? ",Toast.LENGTH_SHORT);
                        fetchData(context);
                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        requestQueue.add(jsObjRequest);

    }

    public static void fetchDataAndBroadcast(Context _context) {

        final Context context = _context;

        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        final Intent broadcastIntent = new Intent();
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.setAction(NetworkReceiver.RECEIVER_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ArrayList<HashMap<String, String>> data = JSONHelper.jsonParser(response);

                            if (data != null) {
                                DataSource.setData(data);
                                broadcastIntent.putExtra(Constants.DATA,data);

                            } else
                                Common.displayErrorMessage(context);

                            String title = JSONHelper.getTitle(response);
                            if (title != null) {
                                DataSource.setTitle(title);
                                broadcastIntent.putExtra(Constants.TITLE,title);

                            } else
                                Common.displayErrorMessage(context);

                            context.sendBroadcast(broadcastIntent);

                        } catch (NullPointerException exception) {
                            Common.displayErrorMessage(context);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(getApplicationContext(),"Oops, are you sure you are connected to the internet? ",Toast.LENGTH_SHORT);

                        fetchDataAndBroadcast(context);

                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        requestQueue.add(jsObjRequest);
    }
}
