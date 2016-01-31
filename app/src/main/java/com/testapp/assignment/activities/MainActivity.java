package com.testapp.assignment.activities;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.android.volley.RequestQueue;
import com.testapp.assignment.R;
import java.util.ArrayList;
import java.util.HashMap;
import listeners.NetworkListener;
import models.DataSource;
import constants.Common;
import constants.Constants;
import fragments.ListFragment;
import network.VolleySingleton;
import receivers.NetworkReceiver;
import services.NetworkService;

public class MainActivity extends AppCompatActivity
                          implements ListFragment.OnFragmentInteractionListener,
                                     NetworkListener {

    ProgressBar mProgressBar;
    BroadcastReceiver mNetworkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        initializeToolbar();
        initializeFAB();

        onOrientationChanged(savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putSerializable(Constants.DATA_STORE_KEY, DataSource.getData());
        bundle.putString(Constants.TITLE, DataSource.getTitle());
        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = (ListFragment)manager.findFragmentByTag(getString(R.string.list_fragment));
        manager.putFragment(bundle, Constants.DATA_STORE_KEY, fragment);

    }

    @Override
    public void onResume(){
        super.onResume();

        mNetworkReceiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(NetworkReceiver.RECEIVER_KEY);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(mNetworkReceiver, filter);
    }


    @Override
    public void onPause(){
        super.onPause();

        unregisterReceiver(mNetworkReceiver);
    }

    private void onOrientationChanged(Bundle savedInstanceState){

        if(savedInstanceState == null) {
            //Common.fetchData(this);
            NetworkService.readFromNetwork(this);
        }
        else{
            FragmentManager manager = getSupportFragmentManager();
            manager.getFragment(savedInstanceState,Constants.DATA_STORE_KEY);
            mProgressBar.setVisibility(View.GONE);

            String title = (String)savedInstanceState.getString(Constants.TITLE);
            getSupportActionBar().setTitle(title);
        }
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

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        if(requestQueue != null){
            requestQueue.cancelAll(Constants.REQUEST_QUEUE_TAG);
        }

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
        //Common.fetchData(this);
        NetworkService.readFromNetwork(this);
    }

    @Override
    public void onDataReceived(ArrayList<HashMap<String, String>> data) {
        passDataToFragment(DataSource.getData());
    }

    @Override
    public void onTitleReceived(String title) {
        getSupportActionBar().setTitle(DataSource.getTitle());
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onInformationReceived(ArrayList<HashMap<String, String>> data, String title) {

        passDataToFragment(DataSource.getData());
        getSupportActionBar().setTitle(DataSource.getTitle());
        mProgressBar.setVisibility(View.GONE);
    }
}
