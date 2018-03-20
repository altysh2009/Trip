package com.project.altysh.firebaseloginandsaving.ui.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

public class HistoryDetails extends AppCompatActivity {
    int id;
    TextView stuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("id", 0);
        FireBaseConnection fireBaseConnection = FireBaseConnection.getInstance(getApplicationContext());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(fireBaseConnection.getHistoryId(id).getTrip_dto().getTripName());
            stuts = findViewById(R.id.status);
            stuts.setText(fireBaseConnection.getHistoryId(id).getStatus());
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Log.i("click", "onCreate: " + id);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FragmentDetails fragmentDetails = (FragmentDetails) getSupportFragmentManager().findFragmentByTag("historyDetailFrag");
        fragmentDetails.setid(id);
    }
}
