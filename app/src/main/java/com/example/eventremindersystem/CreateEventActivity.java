package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    Spinner eventsOption;
    TextView selectedDateOfEvent, selectedTimeOfEvent;
    EditText eventName, venueOfEvent, descriptionOfEvent;
    Button selectDate, selectTime, createEvent;
    String[] options = {"Select Your Event", "Anniversary", "Birthday", "Competition" ,"Customize Your Own Event"};

    int year, month, dayOfMonth, hour, minute;
    DatePickerDialog datePickerDialog;
    Calendar calendar;

    SessionUser sessionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventsOption = (Spinner)findViewById(R.id.eventsOption);
        eventName = (EditText)findViewById(R.id.eventName);
        descriptionOfEvent = (EditText)findViewById(R.id.descriptionOfEvent);
        venueOfEvent = (EditText)findViewById(R.id.venueOfEvent);
        selectedDateOfEvent = (TextView)findViewById(R.id.selectedDateOfEvent);
        selectDate = (Button)findViewById(R.id.selectDate);
        selectedTimeOfEvent = (TextView)findViewById(R.id.selectedTimeOfEvent);
        selectTime = (Button)findViewById(R.id.selectTime);
        createEvent = (Button)findViewById(R.id.createEvent);
        sessionUser = new SessionUser(CreateEventActivity.this);

        //add options to dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventsOption.setAdapter(adapter);

        //selected option and if option is custom show edit text
        eventsOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 4){
                    eventName.setVisibility(View.VISIBLE);
                }else{
                    eventName.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //show date dialog on click
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        selectedDateOfEvent.setText(day + "/" + month + "/" + year);
                    }
                }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
        //show time picker dialog on click
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        selectedTimeOfEvent.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };

                int style = AlertDialog.THEME_HOLO_DARK;
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateEventActivity.this, onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        //create event on Button click
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkValidation = CheckAllFields();
                String user_id = sessionUser.getUserId();
                if(checkValidation.equals("")){
                    if(eventName.getVisibility() == View.VISIBLE){
                        createUserEvent(eventName.getText().toString(), selectedDateOfEvent.getText().toString(), selectedTimeOfEvent.getText().toString(), venueOfEvent.getText().toString(), descriptionOfEvent.getText().toString(), user_id);
                    }else{
                        createUserEvent(eventsOption.getSelectedItem().toString(), selectedDateOfEvent.getText().toString(), selectedTimeOfEvent.getText().toString(), venueOfEvent.getText().toString(), descriptionOfEvent.getText().toString(), user_id);
                    }
                }else{
                    Toast.makeText(CreateEventActivity.this, ""+checkValidation, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void createUserEvent(String eventName, String date, String time, String venue, String description, String user_id) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(CreateEventActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrls.createEventURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    // on below line we are displaying a success toast message.
                    Toast.makeText(CreateEventActivity.this, ""+message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateEventActivity.this, EventListingActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(CreateEventActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();;
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(CreateEventActivity.this, "Fail to create event" + error, Toast.LENGTH_LONG).show();
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
                params.put("name", eventName);
                params.put("date", date);
                params.put("time", time);
                params.put("venue", venue);
                params.put("description", description);
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

    private String CheckAllFields() {
        String message = "";

        if(eventsOption.getSelectedItem().toString().equals("Select Your Event")){
            message = "Select Your Event";
            return message;
        }

        if(eventName.getVisibility() == View.VISIBLE){
            if(eventName.length() == 0){
                message = "Event Name field is required";
                return message;
            }else{
                message = "";
                return message;
            }
        }

        if (selectedDateOfEvent.getText().length() == 0) {
            message = "Date field is required";
            return message;
        }

        if (selectedTimeOfEvent.getText().length() == 0) {
            message = "Time field is required";
            return message;
        }

        if (venueOfEvent.length() == 0) {
            message = "Venue field is required";
            return message;
        }

        if (descriptionOfEvent.length() == 0) {
            message = "Description field is required";
            return message;
        }

        // after all validation return true.
        return message;
    }
}