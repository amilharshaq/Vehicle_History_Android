package com.example.vehicle_history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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

public class ViewFullHistory extends AppCompatActivity {

    TextView t1,t2,t3,t4,t5, vtype;
    ArrayList<String> name,phone, address;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_full_history);



        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView5);
        t3 = findViewById(R.id.textView7);
        t4 = findViewById(R.id.textView16);
        t5 = findViewById(R.id.textView15);
        vtype = findViewById(R.id.textViewVehicleType);

        t1.setText(getIntent().getStringExtra("date"));
        t2.setText(getIntent().getStringExtra("details"));
        t3.setText(getIntent().getStringExtra("cost"));
        vtype.setText(getIntent().getStringExtra("vtype"));

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Report.class);
                startActivity(i);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Call function to fetch history data
        fetchServiceCenterDetails();


    }



    private void fetchServiceCenterDetails() {
        String url = "http://" + sh.getString("ip", "") + ":5000/service_center_details"; // Ensure correct endpoint
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ServerResponse", response);
                try {
                    JSONArray ar = new JSONArray(response);
                    String details = "";

                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject jo = ar.getJSONObject(i);

                        details = jo.getString("name")+", "+jo.getString("phone")+", "+jo.getString("address");

                    }

                    t5.setText(details);
                    // Set data into ListView using ArrayAdapter

                } catch (Exception e) {
                    Log.e("JSONError", e.toString());
                    Toast.makeText(ViewFullHistory.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
                Toast.makeText(ViewFullHistory.this, "Network Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("sid", getIntent().getStringExtra("sid")); // Ensure this key matches backend
                return params;
            }
        };

        queue.add(stringRequest);
    }


}