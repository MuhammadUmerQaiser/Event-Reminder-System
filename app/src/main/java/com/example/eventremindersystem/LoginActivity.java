package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailLogin, passwordLogin;
    TextView dontHaveAccount;
    Button login;
    SessionUser sessionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (EditText)findViewById(R.id.emailLogin);
        passwordLogin = (EditText)findViewById(R.id.passwordLogin);
        dontHaveAccount = (TextView)findViewById(R.id.dontHaveAccount);
        login = (Button)findViewById(R.id.login);
        sessionUser = new SessionUser(LoginActivity.this);

        if(sessionUser.isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
            startActivity(intent);
        }

        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkValidation = CheckAllFields();
                if(checkValidation.equals("")){
                    loginUser(emailLogin.getText().toString(), passwordLogin.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, ""+checkValidation, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loginUser(String email, String password) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrls.loginURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String contact = jsonObject.getString("contact");
                    sessionUser.saveUser(id, name, email, contact);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();;
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(LoginActivity.this, "Fail to logged in" + error, Toast.LENGTH_LONG).show();
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
                params.put("email", email);
                params.put("password", password);

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

        if (emailLogin.length() == 0) {
            message = "Email field is required";
            return message;
        }

        if (passwordLogin.length() == 0) {
            message = "Password field is required";
            return message;
        }

        // after all validation return true.
        return message;
    }
}