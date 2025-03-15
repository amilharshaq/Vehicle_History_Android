package com.example.vehicle_history;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewFullHistory extends AppCompatActivity {

    TextView t1,t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_full_history);



        t1 = findViewById(R.id.textView3);
        t2 = findViewById(R.id.textView5);
        t3 = findViewById(R.id.textView7);

        t1.setText(getIntent().getStringExtra("date"));
        t2.setText(getIntent().getStringExtra("details"));
        t3.setText(getIntent().getStringExtra("cost"));



    }
}