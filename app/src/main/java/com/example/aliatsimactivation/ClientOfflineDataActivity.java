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

public class ClientOfflineDataActivity extends AppCompatActivity {
    Intent intent;
    private RecyclerView offlinedataRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_offline_data);
        offlinedataRecView=findViewById(R.id.offlinedataRecView);
        Button btnback = findViewById(R.id.btnback);

        //counting the number of files
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = dir.listFiles();
        int count =0;
        for (File f: files)
        {
            String name = f.getName();
            if (name.endsWith(".txt"))
                count++;
        }


        if (count == 1) {
            String a = files[0].getName();
            ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new ClientOfflineDataListView(a));
            ClientOfflineDataRecViewAdapter adapter = new ClientOfflineDataRecViewAdapter(ClientOfflineDataActivity.this);
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(ClientOfflineDataActivity.this));
        }

        if (count == 2) {
            String a = files[0].getName();
            String b = files[1].getName();
            ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new ClientOfflineDataListView(a));
            offlinedata.add(new ClientOfflineDataListView(b));
            ClientOfflineDataRecViewAdapter adapter = new ClientOfflineDataRecViewAdapter(ClientOfflineDataActivity.this);
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(ClientOfflineDataActivity.this));
        }

        if (count == 3) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new ClientOfflineDataListView(a));
            offlinedata.add(new ClientOfflineDataListView(b));
            offlinedata.add(new ClientOfflineDataListView(c));
            ClientOfflineDataRecViewAdapter adapter = new ClientOfflineDataRecViewAdapter(ClientOfflineDataActivity.this);
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(ClientOfflineDataActivity.this));
        }

        if (count == 4) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            String d = files[3].getName();
            ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new ClientOfflineDataListView(a));
            offlinedata.add(new ClientOfflineDataListView(b));
            offlinedata.add(new ClientOfflineDataListView(c));
            offlinedata.add(new ClientOfflineDataListView(d));
            ClientOfflineDataRecViewAdapter adapter = new ClientOfflineDataRecViewAdapter(ClientOfflineDataActivity.this);
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(ClientOfflineDataActivity.this));
        }

        if (count == 5) {
            String a = files[0].getName();
            String b = files[1].getName();
            String c = files[2].getName();
            String d = files[3].getName();
            String e = files[4].getName();
            ArrayList<ClientOfflineDataListView> offlinedata = new ArrayList<>();
            offlinedata.add(new ClientOfflineDataListView(a));
            offlinedata.add(new ClientOfflineDataListView(b));
            offlinedata.add(new ClientOfflineDataListView(c));
            offlinedata.add(new ClientOfflineDataListView(d));
            offlinedata.add(new ClientOfflineDataListView(e));
            ClientOfflineDataRecViewAdapter adapter = new ClientOfflineDataRecViewAdapter(ClientOfflineDataActivity.this);
            adapter.setContacts(offlinedata);
            offlinedataRecView.setAdapter(adapter);
            offlinedataRecView.setLayoutManager(new LinearLayoutManager(ClientOfflineDataActivity.this));
        }



        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ClientChargeInfoActivity.class);
                startActivity(i);
            }
        });

    }
}
