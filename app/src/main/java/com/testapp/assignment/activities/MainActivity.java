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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.testapp.assignment.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Models.DataSource;
import constants.Common;
import constants.Constants;
import fragments.ListFragment;
import json.JSONHelper;
import network.VolleySingleton;

public class MainActivity extends AppCompatActivity
                          implements ListFragment.OnFragmentInteractionListener{

    ProgressBar mProgressBar;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        initializeFAB();

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        if(savedInstanceState == null) {
            fetchData();
        }
        else{
            FragmentManager manager = getSupportFragmentManager();
            manager.getFragment(savedInstanceState,Constants.DATA_STORE_KEY);
            mProgressBar.setVisibility(View.GONE);

            String title = (String)savedInstanceState.getString(Constants.TITLE);
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putSerializable(Constants.DATA_STORE_KEY, DataSource.getData());
        bundle.putString(Constants.TITLE,DataSource.getTitle());
        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = (ListFragment)manager.findFragmentByTag(getString(R.string.list_fragment));
        manager.putFragment(bundle, Constants.DATA_STORE_KEY, fragment);

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

    @Override
    protected void onStop(){
        super.onStop();

        if(mRequestQueue != null){
            mRequestQueue.cancelAll(Constants.REQUEST_QUEUE_TAG);
        }

    }

    private void fetchData(){

        this.mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, Constants.URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            ArrayList<HashMap<String,String>> data = JSONHelper.jsonParser(response);
                            if(data != null) {
                                DataSource.setData(data);
                                passDataToFragment(DataSource.getData());
                            }
                            else
                                Common.displayErrorMessage(getApplicationContext());


                            String title = JSONHelper.getTitle(response);
                            if(title != null) {
                                DataSource.setTitle(title);
                                getSupportActionBar().setTitle(DataSource.getTitle());
                            }
                            else
                                Common.displayErrorMessage(getApplicationContext());


                            mProgressBar.setVisibility(View.GONE);
                        }
                        catch(NullPointerException exception) {
                            Common.displayErrorMessage(getApplicationContext());
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(getApplicationContext(),"Oops, are you sure you are connected to the internet? ",Toast.LENGTH_SHORT);
                        fetchData();

                    }
                });

        jsObjRequest.setTag(Constants.REQUEST_QUEUE_TAG);
        this.mRequestQueue.add(jsObjRequest);
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
