package com.example.eventremindersystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivity extends AppCompatActivity {

    EditText nameRegister, emailRegister, contactRegister, passwordRegister, confirm_passwordRegister;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameRegister = (EditText)findViewById(R.id.nameRegister);
        emailRegister = (EditText)findViewById(R.id.emailRegister);
        contactRegister = (EditText)findViewById(R.id.contactRegister);
        passwordRegister = (EditText)findViewById(R.id.passwordRegister);
        confirm_passwordRegister = (EditText)findViewById(R.id.confirm_passwordRegister);
        register = (Button)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkValidation = CheckAllFields();
//                registerUser(nameRegister.getText().toString(), emailRegister.getText().toString(), contactRegister.getText().toString(), passwordRegister.getText().toString());
                if(checkValidation.equals("")){
                    registerUser(nameRegister.getText().toString(), emailRegister.getText().toString(), contactRegister.getText().toString(), passwordRegister.getText().toString());
//                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RegisterActivity.this, ""+checkValidation, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(String name, String email, String contact, String password) {

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, ApiUrls.regitserURL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    // on below line we are displaying a success toast message.
                    Toast.makeText(RegisterActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();;
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(RegisterActivity.this, "Fail to add user" + error, Toast.LENGTH_LONG).show();
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
                params.put("name", name);
                params.put("email", email);
                params.put("contact", contact);
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
        if (nameRegister.length() == 0) {
            message = "Name field is required";
            return message;
        }

        if (emailRegister.length() == 0) {
            message = "Email field is required";
            return message;
        }

        if (contactRegister.length() == 0) {
            message = "Contact field is required";
            return message;
        }

        if (passwordRegister.length() == 0) {
            message = "Password field is required";
            return message;
        } else if (passwordRegister.length() < 8) {
            message = "Password must be minimum 8 characters";
            return message;
        }

        if(confirm_passwordRegister.length() == 0){
            message = "Confirm Password field is required";
            return message;
        }

        if(!passwordRegister.getText().toString().equals(confirm_passwordRegister.getText().toString())){
            message = "Password does not match";
            return message;
        }

        // after all validation return true.
        return message;
    }
}