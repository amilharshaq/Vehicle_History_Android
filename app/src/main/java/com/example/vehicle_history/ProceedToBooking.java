package com.example.vehicle_history;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProceedToBooking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText e1,e2,e3;
    Spinner sp1, sp2;
    Button b1;
    String[] services = {"Oil Change", "Engine Repair", "Battery Replacement", "Wheel Alignment", "Brake Service", "AC Repair", "General Maintenance"};

    String[] vehicles = {"Car", "Motorcycle", "Truck", "Bus", "SUV", "Van", "Electric Vehicle"};

    String service_type, vehicle_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proceed_to_booking);




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(ProceedToBooking.this);

        e1 = findViewById(R.id.editTextText2);
        e2 = findViewById(R.id.editTextText3);
        e3 = findViewById(R.id.editTextText4);
        b1 = findViewById(R.id.button);
        sp1 = findViewById(R.id.spinner);
        sp2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, services);
        sp1.setAdapter(adapter);

        sp1.setOnItemSelectedListener(ProceedToBooking.this);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vehicles);
        sp2.setAdapter(adapter2);

        sp2.setOnItemSelectedListener(ProceedToBooking.this);

        e3.setOnClickListener(view -> showDatePicker());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regno, details, date;

                Toast.makeText(ProceedToBooking.this, "Ok", Toast.LENGTH_SHORT).show();

                regno = e1.getText().toString();
                details = e2.getText().toString();
                date = e3.getText().toString();


                RequestQueue queue = Volley.newRequestQueue(ProceedToBooking.this);
                String url = "http://" + sh.getString("ip", "") + ":5000/book_service_center";

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

                                Toast.makeText(ProceedToBooking.this, "Successfully Booked", Toast.LENGTH_SHORT).show();

                                Intent location = new Intent(getApplicationContext(), Locationservice.class);
                                startService(location);
//
                                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(i);


                            } else {

                                Toast.makeText(ProceedToBooking.this, "Something went wrong", Toast.LENGTH_SHORT).show();

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
                        params.put("regno", regno);
                        params.put("details", details);
                        params.put("date", date);
                        params.put("service_type", service_type);
                        params.put("vehicle_type", vehicle_type);
                        params.put("sid", getIntent().getStringExtra("sid"));
                        params.put("lid", sh.getString("lid",""));

                        return params;
                    }
                };
                queue.add(stringRequest);


            }
        });


    }

    private void showDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format date as DD/MM/YYYY
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    e3.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (adapterView.getId() == R.id.spinner) {
            service_type = services[i];
        } else if (adapterView.getId() == R.id.spinner2) {
            vehicle_type = vehicles[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
