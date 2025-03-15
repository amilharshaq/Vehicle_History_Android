package com.example.vehicle_history;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

public class Login extends AppCompatActivity {


    SharedPreferences sh;
    EditText e1,e2;
    Button b1;
    TextView t1;
    String username,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1 = findViewById(R.id.editTextTextPersonName2);
        e2 = findViewById(R.id.editTextTextPersonName3);
        b1 = findViewById(R.id.loginbtn);
        t1 = findViewById(R.id.textView);






        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = e1.getText().toString();
                password = e2.getText().toString();

                if (username.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter your username");
                    e1.requestFocus();
                }
                else if (password.equalsIgnoreCase(""))
                {
                    e2.setError("Please enter your password");
                    e2.requestFocus();
                }

                else {

                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/login";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");
//                            Toast.makeText(Login.this, ""+response, Toast.LENGTH_SHORT).show();

                                if (res.equalsIgnoreCase("valid")) {
                                    String lid = json.getString("id");

                                    SharedPreferences.Editor ed = sh.edit();
                                    ed.putString("lid", lid);
                                    ed.commit();

                                    Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();

                                    Intent location = new Intent(getApplicationContext(), Locationservice.class);
                                    startService(location);
//
                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);


                                } else {

                                    Toast.makeText(Login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

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
                            params.put("uname", username);
                            params.put("pswd", password);

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }

            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),UserRegister.class);
                startActivity(i);
            }
        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder ald = new AlertDialog.Builder(Login.this);
        ald.setTitle("Do you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in = new Intent(Intent.ACTION_MAIN);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.addCategory(Intent.CATEGORY_HOME);
                        startActivity(in);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });

        AlertDialog al = ald.create();
        al.show();;
    }

}