package com.example.vehicle_history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBookings extends AppCompatActivity {

    SharedPreferences sh;
    ListView l1;
    ArrayList<String> name, regno, date, status;


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


                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("name"));
                        regno.add(jo.getString("vehicle_reg_no"));
                        date.add(jo.getString("date"));
                        status.add(jo.getString("status"));

                    }

                    // Set data into ListView using ArrayAdapter
                    ArrayAdapter<String> ad = new ArrayAdapter<>(ViewBookings.this, android.R.layout.simple_list_item_1, name);
//                    l1.setAdapter(ad);

                    l1.setAdapter(new Custom4(ViewBookings.this,regno,name,date,status));

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


}