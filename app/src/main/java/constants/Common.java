package constants;

import android.content.Context;
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

import Models.DataSource;
import json.JSONHelper;
import network.VolleySingleton;

/**
 * Created by rahul on 30-01-2016.
 */
public class Common {

    public static void displayErrorMessage(Context context){
        Toast.makeText(context, "Oops, there is no data to display", Toast.LENGTH_SHORT).show();
    }
}
