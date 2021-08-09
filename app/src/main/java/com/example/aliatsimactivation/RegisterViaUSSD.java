package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterViaUSSD extends AppCompatActivity {
    private EditText txtussd;
    Button sendussd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtussd=(EditText)findViewById(R.id.txtussd);
        sendussd=(Button)findViewById(R.id.button1);


        /*sendussd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String UssdCode=txtussd.getText().toString();
                if (UssdCode.startsWith("*") && UssdCode.endsWith("#")) {
                    System.out.println("start with "+UssdCode);
                    //we want to remove the last # from the ussd code as we need to encode it. so *555# becomes *555
                    UssdCode = UssdCode.substring(0, UssdCode.length() - 1);

                    String UssdCodeNew = UssdCode + Uri.encode("#");

                    //request for permission
                    if (ActivityCompat.checkSelfPermission(RegisterViaUSSD.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                        System.out.println("NO ACTION");
                    } else {
                        System.out.println("start sending "+UssdCode);
                        //dial Ussd code
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + UssdCodeNew)));

                    }


                } else {
                    System.out.println("Please enter a valid ussd code");
                    Toast.makeText(getApplicationContext(), "Please enter a valid ussd code", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(getApplicationContext(),
                "USSD: " + requestCode + "  " + resultCode + " ", Toast.LENGTH_LONG).show();

        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                // String result=data.getStringExtra("result");
                String dd = data.toString();
                Toast.makeText(getApplicationContext(), dd, Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "No response comes from USSD the code returned is : "+ requestCode, Toast.LENGTH_LONG).show();
        }*/
    }
}