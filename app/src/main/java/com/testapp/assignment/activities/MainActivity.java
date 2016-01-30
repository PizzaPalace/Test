package com.testapp.assignment.activities;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.testapp.assignment.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import constants.Constants;
import fragments.ListFragment;
import json.JSONHelper;
import network.VolleySingleton;

public class MainActivity extends AppCompatActivity
                          implements ListFragment.OnFragmentInteractionListener{

    String mActivityTitle;
    ArrayList<HashMap<String,String>> mData;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        initializeFAB();

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        fetchData();
    }

    private void initializeToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    private void initializeFAB(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData(){

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        mData = JSONHelper.jsonParser(response);
                        passDataToFragment(mData);
                        mProgressBar.setVisibility(View.GONE);
                        mActivityTitle = JSONHelper.getTitle(response);
                        getSupportActionBar().setTitle(mActivityTitle);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.v("ERROR", error.toString());

                    }
                });

        requestQueue.add(jsObjRequest);
    }

    private void passDataToFragment(ArrayList<HashMap<String,String>> data){

        FragmentManager manager = getSupportFragmentManager();
        ListFragment listFragment = (ListFragment)manager.findFragmentByTag(getString(R.string.list_fragment));
        listFragment.setAdapter(data);
        listFragment.dismissRefresh();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSwipeInteraction() {

        mProgressBar.setVisibility(View.VISIBLE);
        fetchData();
    }
}
