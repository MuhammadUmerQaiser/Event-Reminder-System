package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventListingActivity extends AppCompatActivity {

    SessionUser sessionUser;
    ListView myEventListView;
    MyAdapter adapter;
    public static ArrayList<EventListing> eventArrayList = new ArrayList<>();
    EventListing eventListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_listing);

        myEventListView = (ListView)findViewById(R.id.myEventListView);
        adapter = new MyAdapter(this, eventArrayList);
        myEventListView.setAdapter(adapter);

        sessionUser = new SessionUser(EventListingActivity.this);
        String user_id = sessionUser.getUserId();
        getUserEvent(user_id);
    }

    private void getUserEvent(String user_id) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(EventListingActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrls.eventListingURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                eventArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if(message.equals("1")){
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            String id = object.getString("id");
                            String name = object.getString("name");
                            String date = object.getString("date");
                            String time = object.getString("time");
                            String description = object.getString("description");
                            String venue = object.getString("venue");

                            eventListing = new EventListing(id, name, date, time, venue, description);
                            eventArrayList.add(eventListing);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(EventListingActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();;
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(EventListingActivity.this, "Fail to create event" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/json; charset=utf-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<>();

                // on below line we are passing our key and value pair to our parameters.
                params.put("user_id", user_id);

                // at last we are returning our params.
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}