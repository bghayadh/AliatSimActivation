package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class SimRegistrationAPI extends AsyncTask<String, Void, String> {
    String fname,  mname,  lname, msisdn, idType, idNumber, dob, gender,  email,  altnumber,  address1,  state,  agentmsisdn;

    private ActivationResponse activationResponse;

    public SimRegistrationAPI(ActivationResponse activationResponse){
        this.activationResponse=activationResponse;
    }

    public SimRegistrationAPI(String fname, String mname, String lname, String msisdn, String idType, String idNumber, String dob, String gender, String email, String altnumber, String address1, String state, String agentmsisdn) {
        this.fname=fname;
        this.mname=mname;
        this.lname=lname;
        this.msisdn=msisdn;
        this.idType=idType;
        this.idNumber=idNumber;
        this.dob=dob;
        this.gender=gender;
        this.email=email;
        this.altnumber=altnumber;
        this.address1=address1;
        this.state=state;
        this.agentmsisdn=agentmsisdn;
    }

    @Override
    protected String doInBackground(String... params) {


        System.out.println(fname);
        System.out.println(mname);
        System.out.println(lname);
        System.out.println(msisdn);
        System.out.println(idType);
        System.out.println(idNumber);
        System.out.println(dob);
        System.out.println(gender);
        System.out.println(email);
        System.out.println(altnumber);
        System.out.println(address1);
        System.out.println(state);
        System.out.println(agentmsisdn);


        HttpURLConnection urlConnection = null;

        try {
            JsonObject postData = new JsonObject();
            postData.addProperty("requestId", "REQ9");
            postData.addProperty("serviceId", "SIMREG");
            postData.addProperty("clientId", "1");
            postData.addProperty("msisdn", msisdn);
            postData.addProperty("firstName", fname);
            postData.addProperty("middleName", mname);
            postData.addProperty("lastName", lname);
            postData.addProperty("idType", idType);
            postData.addProperty("idNumber", idNumber);
            postData.addProperty("dateOfBirth", dob);
            postData.addProperty("minorFlag", "NO");
            postData.addProperty("gender", gender);
            postData.addProperty("emailId", email);
            postData.addProperty("alternateNumber", altnumber);
            postData.addProperty("address_1", address1);
            postData.addProperty("address_2", "");
            postData.addProperty("address_3", "");
            postData.addProperty("cityName", "city test");
            postData.addProperty("stateName", state);
            postData.addProperty("country", "KENYA");
            postData.addProperty("userId", "USSD");
            postData.addProperty("clientPassword", "iPacsUssd@123");
            postData.addProperty("agentMsisdn", "776761539");

            URL url = new URL("http://10.22.25.10:8995/ipacs/ussd/api/");
            System.out.println("step1");
            urlConnection = (HttpURLConnection) url.openConnection();
            System.out.println("step2");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            System.out.println("step3");
            OutputStream out = new BufferedOutputStream (urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter (
                    out, "UTF-8"));
            writer.write(postData.toString());
            writer.flush();

            System.out.println("Sent successfully to server");


            int code = urlConnection.getResponseCode();

            System.out.println("Get code "+code);

            //if (code !=  201) {
           //     throw new IOException ("Invalid response from server: " + code);
          //  }

            BufferedReader rd = new BufferedReader(new InputStreamReader (
                    urlConnection.getInputStream()));
            System.out.println("Read result step4");
            String line =null;
            while ((line = rd.readLine()) != null) {
                //Log.i("data", line);
                System.out.println("Get result "+line);



            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s!=null)
        {
            activationResponse.SuccessData(s);
        }
    }
}
