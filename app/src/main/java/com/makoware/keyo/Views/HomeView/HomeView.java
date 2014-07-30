package com.makoware.keyo.Views.HomeView;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.makoware.keyo.Other.CustomAdapter;
import com.makoware.keyo.Other.Intenter;
import com.makoware.keyo.Views.Hub.HubView;
import com.makoware.keyo.Views.Login.LoginView;
import com.makoware.keyo.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public class HomeView extends ActionBarActivity implements CustomAdapter.AdapterViewDelegate<ParseObject>, AdapterView.OnItemClickListener, LocationListener, ActionBar.OnNavigationListener {

    private ArrayAdapter<ParseObject> hubsAdapter;
    private CustomAdapter<ParseObject> adapter;
    private ParseQuery<ParseObject> hubsQuery;
    private Location location;
    private ListView listView;
    private ProgressDialog loadingDialog;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        this.listView = (ListView)findViewById(R.id.homeView_listView);
        this.listView.setOnItemClickListener(this);

        setTitle(R.string.homeView_title);


        /*
        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_section1),
                                getString(R.string.title_section2),
                                getString(R.string.title_section3),
                        }),
                this);

        */

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
//        LocationListener locationListener = new LocationListener() {
//            public void onLocationChanged(Location location) {
//                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            public void onProviderEnabled(String provider) {}
//
//            public void onProviderDisabled(String provider) {}
//        };

// Register the listener with the Location Manager to receive location updates
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        if(ParseUser.getCurrentUser() != null){
            // login
            Log.i("login", "logged in");
//            this.hubsQuery = new ParseQuery<ParseObject>("Hub");

            onRefreshHubs();

        } else {
            // show the login screen
            Log.i("login", "go to login");
            startActivity(new Intent(this, LoginView.class));
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        Log.i("homeView", "onRestoreInstanceState");
//        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
//            getSupportActionBar().setSelectedNavigationItem(
//                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_view_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_refresh:
                onRefreshHubs();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
        return true;
    }

    private void onRefreshHubs(){
//        if(this.location!=null){
//            ParseGeoPoint geoPoint = new ParseGeoPoint(this.location.getLatitude(), this.location.getLongitude());
//            ParseQuery<ParseObject> q = new ParseQuery<ParseObject>("Hub");
//
//            q.findInBackground(new FindCallback<ParseObject>() {
//                @Override
//                public void done(List<ParseObject> parseObjects, ParseException e) {
////                    HomeView.this.hubsAdapter = new ArrayAdapter<ParseObject>(HomeView.this, android.R.layout.simple_list_item_1, parseObjects);
//
//                }
//            });
//        }

        onLoading();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Hub");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> o, ParseException e) {
                if(e==null){
                    if(HomeView.this.adapter==null){
                        HomeView.this.adapter = new CustomAdapter<ParseObject>(HomeView.this,o,HomeView.this);
                        HomeView.this.listView.setAdapter(HomeView.this.adapter);
                    } else {
                        HomeView.this.adapter.clear();
                        HomeView.this.adapter.addAll(o);
                    }
                } else {
                    Log.e("HomeView", "an error getting hubs: "+e);
                }
                onLoaded();
            }
        });

    }

    public ParseQuery<ParseObject> create() {
        // Here we can configure a ParseQuery to our heart's desire.
        ParseQuery query = new ParseQuery("Hub");
//                query.whereContainedIn("genre", Arrays.asList({"Punk", "Metal"}));
//                query.whereGreaterThanOrEqualTo("memberCount", 4);
//                query.orderByDescending("albumsSoldCount");
        return query;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ParseObject obj = (ParseObject)parent.getItemAtPosition(position);
        Log.i("homeView", "selected position: "+position+" title: "+obj.get("title"));
        Intent i = new Intent(this, HubView.class);
        Intenter.putObjectForKey("HubView.Hub",obj);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            startActivity(i, ActivityOptions.makeCustomAnimation(this,R.anim.dst_forward,R.anim.src_forward).toBundle());
        else
            startActivity(i);

    }

    public void onLoading() {
        loadingDialog = ProgressDialog.show(HomeView.this,"Finding Hubs", "Loading...", true, false);
    }

    public void onLoaded() {
        loadingDialog.dismiss();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public View getView(ParseObject object, int position, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(this, R.layout.hub_cell, null);
        }

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.hub_cell_text_view);
        descriptionView.setText(object.getString("title"));
        return v;
    }
}
