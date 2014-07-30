package com.makoware.keyo.Views.Hub;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.makoware.keyo.Other.CustomAdapter;
import com.makoware.keyo.Other.Intenter;
import com.makoware.keyo.R;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.HashMap;
import java.util.List;

public class HubView extends Activity implements AdapterView.OnItemClickListener, CustomAdapter.AdapterViewDelegate<ParseObject> {

    private ListView listView;
//    private ParseQueryAdapter<ParseObject> adapter;
    private ProgressDialog loadingDialog;
    private CustomAdapter<ParseObject> adapter;
    private ParseObject hub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub_view);

        this.hub = (ParseObject)Intenter.getObjectForKey("HubView.Hub");

        this.listView = (ListView)findViewById(R.id.listView);
        this.listView.setOnItemClickListener(this);


        refreshPlaylist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hub_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.dst_back, R.anim.src_back);
                return true;
            case R.id.action_add_song:
                Intent i = new Intent(this,SongChooserView.class);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    startActivity(i, ActivityOptions.makeCustomAnimation(this, R.anim.dst_forward, R.anim.src_forward).toBundle());
                else
                    startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    public void refreshPlaylist(){
        onLoading();
        HashMap<String, Object> params = new HashMap<String, Object>();
        Log.i("HubView", "hub: "+this.hub);
        params.put("hubId", this.hub.getObjectId());
        ParseCloud.callFunctionInBackground("getPlaylist", params, new FunctionCallback<List<ParseObject>>() {
            @Override
            public void done(List<ParseObject> o, ParseException e) {
                if(e==null){
                    if(HubView.this.adapter==null){
                        HubView.this.adapter = new CustomAdapter<ParseObject>(HubView.this,o,HubView.this);
                        HubView.this.listView.setAdapter(HubView.this.adapter);
                    } else {
                        HubView.this.adapter.clear();
                        HubView.this.adapter.addAll(o);
                    }
//                    HubView.this.adapter.

                } else {
                    Log.e("HubView", "an error getting queue: "+e);
                }
                onLoaded();
            }
        });
    }

    @Override
    public View getView(ParseObject object, int position, View v, ViewGroup parent) {
        ParseObject song = ((ParseObject)object).getParseObject("song");

        if (v == null) {
            v = View.inflate(this, R.layout.hub_cell, null);
        }

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.hub_cell_text_view);
        descriptionView.setText(song.getString("title"));
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

//    @Override
//    public ParseQuery<ParseObject> create() {
//        ParseQ
//        return null;
//    }


    public void onLoading() {
        this.loadingDialog = ProgressDialog.show(this,"Retrieving Playlist", "Loading...",true, false);
    }


    public void onLoaded() {
        this.loadingDialog.dismiss();
    }
}
