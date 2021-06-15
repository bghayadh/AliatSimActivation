package com.example.aliatsimactivation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    //define buttons
    private Button BtnMobCharge, BtnResCharge,BtnExit,BtnSIMReg;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        //initialize objects on Load
        BtnSIMReg=findViewById(R.id.Btnsimreg);
        BtnMobCharge=findViewById(R.id.Btnmobcharge);
        BtnResCharge=findViewById(R.id.Btnrescharge);
        BtnExit=findViewById(R.id.BtnExit);

        // check if we have permission to get our location in manifest xml file
        try {
            if (ContextCompat.checkSelfPermission (getApplicationContext ( ), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions (this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }

        // click to move to Sim Registration List
        BtnSIMReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show message to know where are we transferred
                Toast.makeText(MainActivity.this,  "Welcome to Sim Registration page",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SimRegListViewActivity.class);
                 startActivity(intent);
            }

        });



        // click to move to Mobile Charge List
        BtnMobCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show message to know where are we transferred
                Toast.makeText(MainActivity.this,  "Welcome to Mobile Charge page",Toast.LENGTH_SHORT).show();
                 Intent intent =new Intent(MainActivity.this, ClientChargeListViewActivity.class);
                 startActivity(intent);
            }
        });

        //click to move to Reseller Charge List
        BtnResCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show message to know where are we transferred
                Toast.makeText(MainActivity.this,  "Welcome to Reseller Charge page",Toast.LENGTH_SHORT).show();
                 Intent intent =new Intent(MainActivity.this,ResellerChargeListViewActivity.class);
                 startActivity(intent);
            }
        });


        // exit button
        BtnExit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                System.exit(-3);
            }
        });

    }
}