package com.example.vehicle_history;

import android.annotation.SuppressLint;
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

public class SearchServiceCenter extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;

    ArrayList<String> name, address, email, phone, latitude, longitude, id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_service_center);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        l1 = findViewById(R.id.lst2);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // Call function to fetch history data
        fetchHistoryData();

        // Set ItemClickListener outside network request
        l1.setOnItemClickListener(this);



    }


    private void fetchHistoryData() {
        String url = "http://" + sh.getString("ip", "") + ":5000/view_nearest_service_center"; // Ensure correct endpoint
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ServerResponse", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    name = new ArrayList<>();
                    address = new ArrayList<>();
                    email = new ArrayList<>();
                    phone = new ArrayList<>();
                    latitude = new ArrayList<>();
                    longitude = new ArrayList<>();
                    id = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        name.add(jo.getString("name"));
                        address.add(jo.getString("address"));
                        email.add(jo.getString("email"));
                        phone.add(jo.getString("phone"));
                        latitude.add(jo.getString("lati"));
                        longitude.add(jo.getString("longi"));
                        id.add(jo.getString("lid"));
                    }

                    // Set data into ListView using ArrayAdapter
                    ArrayAdapter<String> ad = new ArrayAdapter<>(SearchServiceCenter.this, android.R.layout.simple_list_item_1, name);
//                    l1.setAdapter(ad);

                    l1.setAdapter(new Custom3(SearchServiceCenter.this,name,address,phone));

                } catch (Exception e) {
                    Log.e("JSONError", e.toString());
                    Toast.makeText(SearchServiceCenter.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(SearchServiceCenter.this, "Network Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lati",Locationservice.lati); // Ensure this key matches backend
                params.put("longi",Locationservice.logi); // Ensure this key matches backend

                return params;
            }
        };

        queue.add(stringRequest);
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

      Intent ik = new Intent(getApplicationContext(), ProceedToBooking.class);
      ik.putExtra("sid", id.get(i));
      startActivity(ik);

    }
}


