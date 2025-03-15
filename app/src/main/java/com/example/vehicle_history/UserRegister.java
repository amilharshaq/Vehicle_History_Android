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
    String fname, lname , place, post, pin, email, phone, username, password;

    String age,hname,dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1 = findViewById(R.id.editTextTextPersonName4);
        e2 = findViewById(R.id.editTextTextPersonName5);
        e4 = findViewById(R.id.editTextTextPersonName8);
        e5 = findViewById(R.id.editTextTextPersonName6);
        e6 = findViewById(R.id.editTextTextPersonName9);
        e7 = findViewById(R.id.editTextTextPersonName10);
        e8 = findViewById(R.id.editTextTextPersonName11);
        e9 = findViewById(R.id.editTextTextPersonName12);
        e10 = findViewById(R.id.editTextTextPersonName13);
        e11 = findViewById(R.id.editTextTextPersonName7);
        e12 = findViewById(R.id.editTextTextPersonName16);
        e13 = findViewById(R.id.editTextTextPersonName19);

        b1 = findViewById(R.id.button3);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = e1.getText().toString();
                lname = e2.getText().toString();
                place = e4.getText().toString();
                post = e5.getText().toString();
                pin = e6.getText().toString();
                email = e7.getText().toString();
                phone = e8.getText().toString();
                username = e9.getText().toString();
                password = e10.getText().toString();


                age = e11.getText().toString();
                dist = e12.getText().toString();
                hname = e13.getText().toString();




                if (fname.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter your first name");
                    e1.requestFocus();
                }
                else if (!fname.matches("^[a-z A-Z]*$"))
                {
                    e1.setError("Only characters are allowed");
                    e1.requestFocus();
                }
                else if (lname.equalsIgnoreCase(""))
                {
                    e2.setError("Please enter your last name");
                    e2.requestFocus();
                }
                else if (!lname.matches("^[a-z A-Z]*$"))
                {
                    e2.setError("Only characters are allowed");
                    e2.requestFocus();
                }
                else if (place.equalsIgnoreCase(""))
                {
                    e4.setError("Please enter your place");
                    e4.requestFocus();
                }
                else if (post.equalsIgnoreCase(""))
                {
                    e5.setError("Please enter your post");
                    e5.requestFocus();
                }
                else if (pin.equalsIgnoreCase(""))
                {
                    e6.setError("Please enter your pin");
                    e6.requestFocus();
                }
                else if (pin.length()!=6)
                {
                    e6.setError("Pin number must be 6 numbers");
                    e6.requestFocus();
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

                                if (res.equalsIgnoreCase("valid")) {

                                    Toast.makeText(UserRegister.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    Intent ik = new Intent(getApplicationContext(), Login.class);
                                    startActivity(ik);


                                } else {

                                    Toast.makeText(UserRegister.this, "Error - username already exist", Toast.LENGTH_SHORT).show();

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

                            params.put("fname", fname);
                            params.put("lname", lname);
                            params.put("place", place);
                            params.put("post", post);
                            params.put("pin", pin);
                            params.put("email", email);
                            params.put("phone", phone);
                            params.put("uname", username);
                            params.put("pswd", password);
                            params.put("age", age);
                            params.put("hname", hname);
                            params.put("dist", dist);

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
        Intent i = new Intent(getApplicationContext(),Login.class);
        startActivity(i);
    }


}