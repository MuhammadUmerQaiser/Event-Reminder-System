package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IndexActivity extends AppCompatActivity {

    Button addEvent, logout;
    SessionUser sessionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Button addEvent = (Button)findViewById(R.id.addEvent);
        Button logout = (Button)findViewById(R.id.logout);
        sessionUser = new SessionUser(IndexActivity.this);

        if(sessionUser.isLoggedIn()){
            logout.setVisibility(View.VISIBLE);
        }


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionUser.isLoggedIn()){
                    Intent intent = new Intent(IndexActivity.this, CreateEventActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionUser.logout();
                Intent intent = new Intent(IndexActivity.this, IndexActivity.class);
                startActivity(intent);
                Intent intent2 = new Intent(IndexActivity.this, LoginActivity.class);
                startActivity(intent2);
            }
        });
    }
}