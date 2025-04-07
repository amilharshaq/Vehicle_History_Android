package com.example.vehicle_history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class Report extends AppCompatActivity {

    EditText e1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        e1 = findViewById(R.id.editTextText5);
        b1 = findViewById(R.id.button5);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String details = e1.getText().toString();

                if (details.equalsIgnoreCase(""))
                {
                    e1.setError("Please enter the details");
                    e1.requestFocus();
                }

                else {

                    RequestQueue queue = Volley.newRequestQueue(Report.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/report";

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

                                if (res.equalsIgnoreCase("success")) {

                                    Toast.makeText(Report.this, "Report submitted, You will get a mail after the review", Toast.LENGTH_SHORT).show();

//
                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);


                                } else {

                                    Toast.makeText(Report.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

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
                            params.put("details", details);
                            params.put("sid", sh.getString("sid",""));
                            params.put("uid", sh.getString("lid",""));
                            params.put("regno", sh.getString("regno",""));

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }

            }
        });


    }
}