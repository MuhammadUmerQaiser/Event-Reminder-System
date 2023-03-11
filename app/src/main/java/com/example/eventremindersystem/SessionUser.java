package com.example.eventremindersystem;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUser {
    private static final String Session_Name = "eventreminder";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SessionUser(Context context){
        this.context = context;
    }

    public void saveUser(String id, String name, String email, String contact){
        sharedPreferences = context.getSharedPreferences(Session_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("contact", contact);
        editor.putBoolean("logged", true);
        editor.commit();
    }


    public boolean isLoggedIn(){
        sharedPreferences = context.getSharedPreferences(Session_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }

    public String getUserId(){
        sharedPreferences = context.getSharedPreferences(Session_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "1");
    }

    public void logout(){
        sharedPreferences = context.getSharedPreferences(Session_Name, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
