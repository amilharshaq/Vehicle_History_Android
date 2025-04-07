package com.example.vehicle_history;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBookings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    SharedPreferences sh;
    ListView l1;
    ArrayList<String> name, regno, date, status, bid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_bookings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        l1 = findViewById(R.id.lst3);

        sh = PreferenceManager.getDefaultSharedPreferences(ViewBookings.this);

        fetchBooking();



    }




    private void fetchBooking() {
        String url = "http://" + sh.getString("ip", "") + ":5000/view_bookings"; // Ensure correct endpoint
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ServerResponse", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    name = new ArrayList<>();
                    regno = new ArrayList<>();
                    date = new ArrayList<>();
                    status = new ArrayList<>();
                    bid = new ArrayList<>();


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("name"));
                        regno.add(jo.getString("vehicle_reg_no"));
                        date.add(jo.getString("date"));
                        status.add(jo.getString("status"));
                        bid.add(jo.getString("id"));

                    }

                    // Set data into ListView using ArrayAdapter
                    ArrayAdapter<String> ad = new ArrayAdapter<>(ViewBookings.this, android.R.layout.simple_list_item_1, name);
//                    l1.setAdapter(ad);

                    l1.setAdapter(new Custom4(ViewBookings.this,regno,name,date,status));
                    l1.setOnItemClickListener(ViewBookings.this);

                } catch (Exception e) {
                    Log.e("JSONError", e.toString());
                    Toast.makeText(ViewBookings.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(ViewBookings.this, "Network Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lid",sh.getString("lid",""));
                return params;
            }
        };

        queue.add(stringRequest);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        AlertDialog.Builder ald = new AlertDialog.Builder(ViewBookings.this);
        ald.setTitle("Do You Want To Cancel Booking ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                        RequestQueue queue = Volley.newRequestQueue(ViewBookings.this);
                        String url = "http://" + sh.getString("ip", "") + ":5000/cancel_booking";

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

                                        Toast.makeText(ViewBookings.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();

//
                                        Intent i = new Intent(getApplicationContext(),ViewBookings.class);
                                        startActivity(i);


                                    } else {

                                        Toast.makeText(ViewBookings.this, "Error", Toast.LENGTH_SHORT).show();

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
                                params.put("details", bid.get(i));

                                return params;
                            }
                        };
                        queue.add(stringRequest);



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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}