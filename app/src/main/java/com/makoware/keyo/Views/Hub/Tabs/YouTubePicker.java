package com.makoware.keyo.Views.Hub.Tabs;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.makoware.keyo.Other.App;
import com.makoware.keyo.Other.CustomAdapter;
import com.makoware.keyo.R;
import com.makoware.keyo.Views.Hub.HubView;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class YouTubePicker extends Fragment implements CustomAdapter.AdapterViewDelegate<JSONObject> {


    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private CustomAdapter<JSONObject> adapter;
    private SearchView searchView;
    private MenuItem searchViewItem;

    public static YouTubePicker newInstance(){
        YouTubePicker fragment = new YouTubePicker();

        return fragment;
    }

    public YouTubePicker() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setHasOptionsMenu(true);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.youtube_picker_menu,menu);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) ((Activity)mListener).getSystemService(Context.SEARCH_SERVICE);
        this.searchViewItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (searchView != null)
            searchView.setOnQueryTextListener(this.queryTextListener);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.youtube_picker_fragment, container, false);

        this.listView = (ListView)view.findViewById(R.id.listView);

        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View getView(JSONObject object, int position, View v, ViewGroup parent) {
        JSONObject snippet = null;
        String title = null;
        try {
            snippet = object.getJSONObject("snippet");
            if(snippet!=null){
                title = snippet.getString("title");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (v == null) {
            v = View.inflate(this.getActivity(), R.layout.hub_cell, null);
        }

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.hub_cell_text_view);
        if(title!=null){
            descriptionView.setText(title);
        } else {
            descriptionView.setText("**NO TITLE**");
        }

        return v;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            Log.i("YouTubePicker", "on query text submit: "+s);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("part", "snippet");
            params.put("type", "video");
            params.put("maxResults", "20");
            params.put("key", "AIzaSyDBrE7H_f_7VGa13LLCefYA2uoocG2j0Qg");
            params.put("q", s);

            String URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=20&key=AIzaSyDBrE7H_f_7VGa13LLCefYA2uoocG2j0Qg&q="+s;
//            JsonObjectRequest blah = new JsonObjectRequest()
            JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
//                                    try {
//                                        VolleyLog.v("Response:%n %s", response.toString(4));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
                            Log.i("YouTubePicker", "on json response");
                            ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                            JSONArray array = null;
                            try {
                                array = response.getJSONArray("items");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(array!=null){
                                for(int i=0; i<array.length(); i++){
                                    JSONObject obj = null;
                                    try {
                                        obj = (JSONObject) array.get(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(obj != null){
                                        list.add(i,obj);
                                    }
                                }
                            }
                            if (YouTubePicker.this.adapter == null) {
                                YouTubePicker.this.adapter = new CustomAdapter<JSONObject>(YouTubePicker.this.getActivity(), list, YouTubePicker.this);
                                YouTubePicker.this.listView.setAdapter(YouTubePicker.this.adapter);
                            } else {
                                YouTubePicker.this.adapter.clear();
                                YouTubePicker.this.adapter.addAll(list);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                }
            }
            );

            YouTubePicker.this.searchViewItem.collapseActionView();

            App.getInstance().addToRequestQueue(req);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
//                    Log.i("YouTubePicker", "on query text change: "+s);
            return false;
        }
    };

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
