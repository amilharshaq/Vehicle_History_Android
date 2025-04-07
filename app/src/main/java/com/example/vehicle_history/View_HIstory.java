package com.example.vehicle_history;

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

public class View_HIstory extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView l1;
    SharedPreferences sh;
    ArrayList<String> details, date, amount, vtype, sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_history);

        // Initialize ListView
        l1 = findViewById(R.id.lst1);
        sh = PreferenceManager.getDefaultSharedPreferences(this);

        // Handle window insets for better UI layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences.Editor ed = sh.edit();
        ed.putString("regno", getIntent().getStringExtra("regno"));
        ed.commit();

        // Call function to fetch history data
        fetchHistoryData();

        // Set ItemClickListener outside network request
        l1.setOnItemClickListener(this);
    }

    private void fetchHistoryData() {
        String url = "http://" + sh.getString("ip", "") + ":5000/user_view_history"; // Ensure correct endpoint
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ServerResponse", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    details = new ArrayList<>();
                    date = new ArrayList<>();
                    amount = new ArrayList<>();
                    vtype = new ArrayList<>();
                    sid = new ArrayList<>();

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);
                        details.add(jo.getString("details"));
                        date.add(jo.getString("date"));
                        amount.add(jo.getString("cost"));
                        vtype.add(jo.getString("vtype"));
                        sid.add(jo.getString("sid"));
                    }

                    // Set data into ListView using ArrayAdapter
                    ArrayAdapter<String> ad = new ArrayAdapter<>(View_HIstory.this, android.R.layout.simple_list_item_1, date);
                    l1.setAdapter(ad);

                } catch (Exception e) {
                    Log.e("JSONError", e.toString());
                    Toast.makeText(View_HIstory.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(View_HIstory.this, "Network Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("regno", getIntent().getStringExtra("regno")); // Ensure this key matches backend
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String det = details.get(position);
        String cost = amount.get(position);
        String date2 = date.get(position);
        String veh_type = vtype.get(position);
        String service_center = sid.get(position);

        SharedPreferences.Editor ed = sh.edit();
        ed.putString("sid", service_center);
        ed.commit();

        Intent i = new Intent(getApplicationContext(), ViewFullHistory.class);
        i.putExtra("date", date2);
        i.putExtra("details", det);
        i.putExtra("cost", cost);
        i.putExtra("vtyep", veh_type);
        i.putExtra("sid", service_center);
        startActivity(i);

    }
}
