package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class UserLoginActivity extends AppCompatActivity {
    private TextView tv;
    private String RegisterResult;
    private String file = "MSISDN.txt";
    private String s0,s1;
    private String fileContents,fileContents2;
    private Button BtnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_layout);

        Button btnlogin = findViewById(R.id.login);
        Button btnregister = findViewById(R.id.signup);
        BtnExit=findViewById(R.id.BtnExit);

        //logging into main page
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the values of the msisdn and pin edittexts
                EditText msisdn =(EditText) findViewById(R.id.edtphnbr);
                EditText pin =(EditText) findViewById(R.id.edtloginpin);
                String msisdn1=msisdn.getText().toString();
                String pin1=pin.getText().toString();

                //check if the values are equal to the actual values
                if(msisdn1.equals(s0) && pin1.equals(s1))
                {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Invalid MSISDN or PIN",Toast.LENGTH_LONG).show();
                }

            }
        });
        //move to the register form
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserRegister.class);
                startActivity(intent);
            }
        });

        //check if the file is found in the given directory and call the load function
        File fileDir = new File(getFilesDir(),"MSISDN.txt");
        File file = new File(getApplicationContext().getFilesDir(),"MSISDN.txt");
        if(file.exists())
        {
            Toast.makeText(getApplicationContext(),"Found",Toast.LENGTH_LONG).show();

            LoadData();
        }

        BtnExit.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {

                System.exit(0);
            }
        });
    }

    //function to have the ability to load a file from the storage and fill the values in edittexts
    public void LoadData()
    {
        StringBuilder text = new StringBuilder();

        try {
            FileInputStream fIn = openFileInput("MSISDN.txt");
            int c;
            String temp = "";

            while ((c = fIn.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            text.append(temp);
            text.append('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }
        tv = (TextView) findViewById(R.id.text_view1);
        tv.setText(text.toString());
        tv.setVisibility(View.INVISIBLE);
        RegisterResult = String.valueOf(tv.getText());

        //split the line through : and set them into the edittexts of msisdn and pin
        String[] data = RegisterResult.split(":");
        s0 = data[0];
        s1 = data[1];


        EditText msisdn =(EditText) findViewById(R.id.edtphnbr);
        EditText pin =(EditText) findViewById(R.id.edtloginpin);

        msisdn.setText(s0);
        pin.setText(s1);

    }

    @Override
    protected void onRestart() {
        this.recreate();
        super.onRestart();
    }


}