package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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

import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    Spinner eventsOption;
    TextView selectedDateOfEvent, selectedTimeOfEvent;
    EditText eventName;
    Button selectDate, selectTime;
    String[] options = {"Select Your Event", "Anniversary", "Birthday", "Competition" ,"Customize Your Own Event"};

    int year, month, dayOfMonth, hour, minute;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventsOption = (Spinner)findViewById(R.id.eventsOption);
        eventName = (EditText)findViewById(R.id.eventName);
        selectedDateOfEvent = (TextView)findViewById(R.id.selectedDateOfEvent);
        selectDate = (Button)findViewById(R.id.selectDate);
        selectedTimeOfEvent = (TextView)findViewById(R.id.selectedTimeOfEvent);
        selectTime = (Button)findViewById(R.id.selectTime);

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
    }
}