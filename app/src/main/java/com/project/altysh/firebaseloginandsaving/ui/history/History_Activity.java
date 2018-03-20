package com.project.altysh.firebaseloginandsaving.ui.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

public class History_Activity extends AppCompatActivity {
    FireBaseConnection fireBaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }
}
