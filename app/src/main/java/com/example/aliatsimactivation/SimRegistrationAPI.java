package com.example.aliatsimactivation;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;

import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SimRegistrationAPI extends AsyncTask<String, Void, String> {
    String fname,  mname,  lname, msisdn, idType, idNumber, dob, gender,  email,  altnumber,  address1,  state,  agentmsisdn;
    private String api_response_code,response_message,globalsimid,registration_status;
    Connection conn;
    private String data=null;
    private boolean connectflag=false;
    private int flag=0;



    public SimRegistrationAPI(String globalsimid,String fname, String mname, String lname, String msisdn, String idType, String idNumber, String dob, String gender, String email, String altnumber, String address1, String state, String agentmsisdn) {
        this.globalsimid=globalsimid;
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

        System.out.println(globalsimid);
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
            try {
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                        out, "UTF-8"));
                writer.write(postData.toString());
                writer.flush();
                flag = 1;
            } catch (Exception e) {
                e.printStackTrace();
                flag = 0;
            }


            if (flag == 1){
                System.out.println("Sent successfully to server");


            int code = urlConnection.getResponseCode();

            System.out.println("Get code " + code);

            //if (code !=  201) {
            //     throw new IOException ("Invalid response from server: " + code);
            //  }

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            System.out.println("Read result step4");
            String line = null;
            while ((line = rd.readLine()) != null) {
                //Log.i("data", line);
                System.out.println("Get result " + line);

                if (line.contains("responseCode")) {
                    System.out.println("Found");
                    int m = 0;
                    m = line.indexOf(";");
                    String response_code = line.substring(m + 1, line.length());
                    String[] test1 = response_code.split("[:,]");
                    System.out.println(test1[1].toString());
                    String[] splitterString = test1[1].split("\"");

                    for (String s : splitterString) {

                        api_response_code = s;
                        System.out.println("code : " + api_response_code);

                    }
                }

                if (line.contains("message")) {
                    System.out.println("Found");

                    int n = 0;
                    n = line.indexOf(";");
                    String message = line.substring(n + 1, line.length());
                    String[] test1 = message.split("[:,]");
                    String[] splitterString = test1[1].split("\"");
                    for (String s : splitterString) {
                        response_message = s;
                        System.out.println("response message : " + response_message);


                    }


                }

            }


        }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        if(api_response_code=="0")
        {
            registration_status="Success";
        }else{
            registration_status="Failed";
        }
        System.out.println("status : "+registration_status);




                thread1.start();



     
        data=api_response_code+"!!"+response_message;
        System.out.println("data: "+data);

        return data;
    }

    public void SaveUpdatedSimStatus() {

        try {


                PreparedStatement stmtinsert1 = null;

                try {
                    stmtinsert1 = conn.prepareStatement("UPDATE SIM_REGISTRATION" +
                            " SET " +
                            " RESPONSE_CODE='" + api_response_code + "'," +
                            "RESPONSE_MESSAGE='" + response_message + "'," +
                            "REGISTRATION_STATUS='" + registration_status + "'," +
                            "STATUS='" + registration_status + "'" +
                            "WHERE SIM_REG_ID='" + globalsimid + "'");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtinsert1.executeUpdate();
                    //Toast.makeText(getApplicationContext(), "Saving Completed", Toast.LENGTH_SHORT).show();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmtinsert1.close();
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    Thread thread1 = new Thread(new Runnable() {

        @Override
        public void run() {
            boolean flg=false;
            try {
                if ((flg = connecttoDB()) == true) {
                    SaveUpdatedSimStatus();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


    public boolean connecttoDB() {
        // connect to DB
        OraDB oradb = new OraDB();
        String url = oradb.getoraurl();
        String userName = oradb.getorausername();
        String password = oradb.getorapwd();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                //Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                conn = DriverManager.getConnection(url, userName, password);
                if (conn != null) {
                    connectflag = true;
                } else {
                    connectflag = false;
                }

                //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
            } catch (SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
                System.out.println("error is: " + e.toString());

                connectflag = false;
            } /*catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getApplicationContext(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
            connectflag=false;
        }*/ catch (Exception e) {
                System.out.println("error is: " + e.toString());

                connectflag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return connectflag;
    }




}
