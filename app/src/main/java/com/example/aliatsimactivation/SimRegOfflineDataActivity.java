package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;

public class SimRegOfflineDataActivity extends AppCompatActivity {


    private String globalMode,stroffline;
    private RecyclerView offlinedataRecView;
    private Button btnmain;
    private String agentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_reg_offline_data);
        offlinedataRecView = findViewById(R.id.offlinedataRecView);
        Button btnback = findViewById(R.id.btnback);
        Intent intent = this.getIntent();
        String simid = intent.getStringExtra("message_key");
        globalMode = intent.getStringExtra("globalMode");
        btnmain = findViewById(R.id.BtnMainn);
        stroffline = intent.getStringExtra("db-offline-to-main");
        agentNumber = intent.getStringExtra("agentNumber");

        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("globalMode", globalMode);
                i.putExtra("agentNumber", agentNumber);
                if (globalMode.equalsIgnoreCase("Online")) {
                    i.putExtra("db-offline-to-main", "1");
                } else {
                    i.putExtra("db-offline-to-main", "-100");
                }
                startActivity(i);

            }
        });
        //counting the number of files
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = dir.listFiles();
        int count = 0;
        ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
        for (File f : files) {
            String name = f.getName();
            if (name.startsWith("SIM") && name.endsWith(".txt")) {
                count++;
            }

            offlinedata.add(new SimRegOfflineDataListView(name));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this, getIntent().getStringExtra("globalMode"), getIntent().getStringExtra("db-offline-to-main"), getIntent().getStringExtra("agentNumber"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this));

        }

    }

}

