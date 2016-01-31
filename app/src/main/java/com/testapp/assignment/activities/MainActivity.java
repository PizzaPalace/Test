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
        // initialize UI components
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        initializeToolbar();
        initializeFAB();
        initializeData(savedInstanceState);
    }

    /*
    Required to handle orientation changes. On orientation change the Fragment's state
    is persisted. Data is retrieved in the initializeData() method called in onCreate()
    A Bundle is used to persist data at the Activity level.
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putSerializable(Constants.DATA_STORE_KEY, DataSource.getData());
        bundle.putString(Constants.TITLE, DataSource.getTitle());
        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = (ListFragment)manager.findFragmentByTag(getString(R.string.list_fragment));
        manager.putFragment(bundle, Constants.DATA_STORE_KEY, fragment);

    }

    /*
    Register a network receiver in onResume(). The receiver is unregistered in onPause()
     */
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
        mNetworkReceiver = null;
    }

    /**
     * Method that initializes data by checking if savedInstanceState is null
     * If true, a call is made to the server via an IntentService. Data is obtained
     * in the Activity using a BroadcastReceiver. Else, if the device is rotated or if
     * orientation change occurs, persisted state stored in onSaveInstance state
     * is retrieved. Every time the device is rotated, the progress bar needs to be
     * hidden for better user experience.
     *
     * @param savedInstanceState bundle passed from the onCreate() method
     */
    private void initializeData(Bundle savedInstanceState){

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

    /**
     * Initializes the Toolbar (a replacement for the ActionBar)
     * and sets the default title to a empty string. The title is changed
     * once data is fetched from the network.
     */
    private void initializeToolbar(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    /**
     * Initializes the FloatingActionButton and attaches a click listener. Does
     * not serve any purpose in this app.
     */
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


    /*
    Cancel all pending requests in Volley's RequestQueue before exiting the app.
    The tag that identifies requests is defined in the Constants class.
     */
    @Override
    protected void onStop(){
        super.onStop();

        RequestQueue requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        if(requestQueue != null){
            requestQueue.cancelAll(Constants.REQUEST_QUEUE_TAG);
        }

    }

    /**
     * Passes data obtained from network to Fragment's adapter. Also clears the
     * swipe widget from the UI once operation is completed.
     *
     * @param data ArrayList<HashMap<String,String>> that represents the JSON feed
     *             as a Java data-structure and is passed to a Fragment.
     *             The fragment contains a ListView whose adapter is bound
     *             with this ArrayList.
     */
    private void passDataToFragment(ArrayList<HashMap<String,String>> data){

        FragmentManager manager = getSupportFragmentManager();
        ListFragment listFragment = (ListFragment)manager.findFragmentByTag(getString(R.string.list_fragment));
        listFragment.setAdapter(data);
        listFragment.dismissRefresh();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Listener method that handles swipe interactions. Fetches data from the network
     * using an IntentService + BroadcastReceiver combination. Call to the service's
     * static method readFromNetwork(Context) is made here.
     */
    @Override
    public void onSwipeInteraction() {

        mProgressBar.setVisibility(View.VISIBLE);
        //Common.fetchData(this);
        NetworkService.readFromNetwork(this);
    }

    /**
     * Passes data obtained from NetworkListener to Fragment
     *
     * @param data ArrayList<HashMap<String,String>> that is to be bound to
     *             the Fragment's adapter.
     */
    @Override
    public void onDataReceived(ArrayList<HashMap<String, String>> data) {
        passDataToFragment(DataSource.getData());
    }

    /**
     * Sets the Activity's title after it is obtained from NetworkListener.
     * Also hides ProgressBar.
     *
     * @param title Title to be set for the Activity once data is obtained from listener
     */
    @Override
    public void onTitleReceived(String title) {
        getSupportActionBar().setTitle(DataSource.getTitle());
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Listener method that passes data from BroadcastReceiver.
     * Also hides progress bar.
     *
     * @param data ArrayList<HashMap<String,String>>
     * @param title String
     */
    @Override
    public void onInformationReceived(ArrayList<HashMap<String, String>> data, String title) {

        passDataToFragment(DataSource.getData());
        getSupportActionBar().setTitle(DataSource.getTitle());
        mProgressBar.setVisibility(View.GONE);
    }
}
