package com.example.vehicle_history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    EditText e1,e2,e4,e5,e6,e7,e8,e9,e10,e11,e12,e13;
    Button b1;
    SharedPreferences sh;
    String name, address, email, phone, username, password;

    String age,hname,dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1 = findViewById(R.id.editTextTextPersonName4);
        e4 = findViewById(R.id.editTextTextPersonName8);
        e7 = findViewById(R.id.editTextTextPersonName10);
        e8 = findViewById(R.id.editTextTextPersonName11);
        e9 = findViewById(R.id.editTextTextPersonName12);
        e10 = findViewById(R.id.editTextTextPersonName13);

        b1 = findViewById(R.id.button3);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = e1.getText().toString();
                address = e4.getText().toString();
                email = e7.getText().toString();
                phone = e8.getText().toString();
                username = e9.getText().toString();
                password = e10.getText().toString();


                if (name.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter your first name");
                    e1.requestFocus();
                }
                else if (!name.matches("^[a-z A-Z]*$"))
                {
                    e1.setError("Only characters are allowed");
                    e1.requestFocus();
                }

                else if (address.equalsIgnoreCase(""))
                {
                    e4.setError("Please enter your place");
                    e4.requestFocus();
                }

                else if (email.equalsIgnoreCase(""))
                {
                    e7.setError("Please enter your email address");
                    e7.requestFocus();
                }

                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    e7.setError("Please enter a valid email address");
                    e7.requestFocus();
                }
                else if (phone.equalsIgnoreCase(""))
                {
                    e8.setError("Please enter your phone number");
                    e8.requestFocus();
                }
                else if (phone.length()!=10)
                {
                    e8.setError("Phone number must be 10 numbers");
                    e8.requestFocus();
                }
                else if (username.equalsIgnoreCase(""))
                {
                    e9.setError("Please enter your username");
                    e9.requestFocus();
                }
                else if (password.equalsIgnoreCase(""))
                {
                    e10.setError("Please enter your password");
                    e10.requestFocus();
                }
                else if (password.length()<8)
                {
                    e10.setError("Password must be 8 characters long");
                    e10.requestFocus();
                }

                else {


                    RequestQueue queue = Volley.newRequestQueue(UserRegister.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/user_register";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");
//                            Toast.makeText(login.this, ""+response, Toast.LENGTH_SHORT).show();

                                if (res.equalsIgnoreCase("success")) {

                                    Toast.makeText(UserRegister.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Login.class);
                                    startActivity(ik);


                                } else {

                                    Toast.makeText(UserRegister.this, "Error - username or email already exist", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("name", name);
                            params.put("address", address);
                            params.put("email", email);
                            params.put("contact", phone);
                            params.put("uname", username);
                            params.put("pswd", password);


                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }



            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }


}