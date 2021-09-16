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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim_reg_offline_data);
        offlinedataRecView=findViewById(R.id.offlinedataRecView);
        Button btnback = findViewById(R.id.btnback);
        Intent intent = this.getIntent();
        String simid=intent.getStringExtra("message_key");
        globalMode=intent.getStringExtra("globalMode");
        btnmain=findViewById(R.id.BtnMainn);

        stroffline=intent.getStringExtra("db-offline-to-main");

        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("globalMode",globalMode);
                if(globalMode.equalsIgnoreCase("Online")) {
                    i.putExtra("db-offline-to-main", "1");
                }else{
                    i.putExtra("db-offline-to-main", "-100");
                }
                startActivity(i);

            }
        });
        //counting the number of files
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File[] files = dir.listFiles();
        int count =0;
        for (File f: files)
        {
            String name = f.getName();
            if (name.startsWith("SIM") && name.endsWith(".txt"))
                count++;
        }


        if (count == 1) {
            String a = files[0].getName();
            ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new SimRegOfflineDataListView(a));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this,getIntent().getStringExtra("globalMode"),getIntent().getStringExtra("db-offline-to-main"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this));
        }

        if (count == 2) {
            String a = files[0].getName();
            String b = files[1].getName();
            ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new SimRegOfflineDataListView(a));
            offlinedata.add(new SimRegOfflineDataListView(b));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this,getIntent().getStringExtra("globalMode"), getIntent().getStringExtra("db-offline-to-main"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this)); }

        if (count == 3) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new SimRegOfflineDataListView(a));
            offlinedata.add(new SimRegOfflineDataListView(b));
            offlinedata.add(new SimRegOfflineDataListView(c));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this,getIntent().getStringExtra("globalMode"), getIntent().getStringExtra("db-offline-to-main"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this)); }

        if (count == 4) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            String d = files[3].getName();
            ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new SimRegOfflineDataListView(a));
            offlinedata.add(new SimRegOfflineDataListView(b));
            offlinedata.add(new SimRegOfflineDataListView(c));
            offlinedata.add(new SimRegOfflineDataListView(d));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this,getIntent().getStringExtra("globalMode"), getIntent().getStringExtra("db-offline-to-main"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this)); }

        if (count == 5) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            String d = files[3].getName();
            String e = files[4].getName();
            ArrayList<SimRegOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new SimRegOfflineDataListView(a));
            offlinedata.add(new SimRegOfflineDataListView(b));
            offlinedata.add(new SimRegOfflineDataListView(c));
            offlinedata.add(new SimRegOfflineDataListView(d));
            offlinedata.add(new SimRegOfflineDataListView(e));
            SimRegOfflineDataRecViewAdapter adapter = new SimRegOfflineDataRecViewAdapter(SimRegOfflineDataActivity.this,getIntent().getStringExtra("globalMode"), getIntent().getStringExtra("db-offline-to-main"));
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(SimRegOfflineDataActivity.this));    }


   }
}

