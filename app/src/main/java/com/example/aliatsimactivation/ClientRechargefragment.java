package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientRechargefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientRechargefragment extends Fragment {

    public Connection conn;
    private Spinner statusSpinner;
    private String globalmobchargeid;
    private String file = "MSISDN.txt";
    private String s0,s1,Result;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientRechargefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MobileInfofragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientRechargefragment newInstance(String param1, String param2) {
        ClientRechargefragment fragment = new ClientRechargefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_mobile_infofragment, container, false);
        View V = inflater.inflate (R.layout.fragment_client_rechargefragment, container, false);

        //check network connection
        ConnectivityManager connMgr = (ConnectivityManager) getActivity ( )
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


            // Creating the status dropdown spinner

            Spinner statusSpinner = (Spinner) V.findViewById(R.id.statusSpinner);
            TextView editTextstatus = (TextView) V.findViewById(R.id.editTextstatus);
            ArrayList<String> status = new ArrayList<>();
            status.add("In Progress");
            status.add("Success");
            status.add("Failed");
            status.add("Cancelled");

            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    status);
            statusSpinner.setAdapter(statusAdapter);

            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String Mytext = statusSpinner.getSelectedItem().toString();
                    editTextstatus.setText(Mytext);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btnsave = (Button) V.findViewById(R.id.btnsave);
            Button btndelete = (Button) V.findViewById(R.id.btndelete);
            Button btnmain = (Button) V.findViewById(R.id.btnmain);
            TextView editTextagent = (TextView) V.findViewById(R.id.editTextagent);
            TextView editTextclient = (TextView) V.findViewById(R.id.editTextclient);
            TextView editTextamount = (TextView) V.findViewById(R.id.editTextamount);
            Button btnrecharge = (Button) V.findViewById(R.id.btnrecharge);
            Button btnbalance = (Button) V.findViewById(R.id.btnbalance);
            TextView editTextcodehere = (TextView) V.findViewById(R.id.editTextcodehere);
            TextView tv = (TextView) V.findViewById(R.id.text_view);

            File file = new File(getActivity().getFilesDir(), "MSISDN.txt");
            if (file.exists()) {

                StringBuilder text = new StringBuilder();

                try {
                    FileInputStream fIn = getActivity().openFileInput("MSISDN.txt");
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

                tv.setText(text.toString());
                tv.setVisibility(View.INVISIBLE);
                Result = String.valueOf(tv.getText());
                System.out.println("RESULT" + Result);
                String[] data = Result.split(":");
                String s0 = data[0];

                editTextagent.setText(s0);
            } else {
                System.out.println("login file don't exist");
            }

            //read passes value of mobcharge_id from recylserview

            Intent intent = getActivity().getIntent();
            String str = intent.getStringExtra("message_key");
            globalmobchargeid = str.toString();


            //Display mobile_charge selected
            // connect to Oracle DB
            connecttoDB();
            Statement stmt1 = null;
            try {
                stmt1 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String sqlStmt = "select * from MOBILE_CHARGE where MOB_CHARGE_ID ='" + globalmobchargeid + "' ";

            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery(sqlStmt);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs1.next()) break;
                    editTextagent.setText(rs1.getString("AGENT_SUB_NUMBER"));
                    editTextclient.setText(rs1.getString("CLIENT_SUB_NUMBER"));
                    editTextamount.setText(rs1.getString("AMOUNT"));
                    editTextstatus.setText(rs1.getString("RECHARGE_STATUS"));

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt1.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            //// recharge button
            btnrecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Agent = String.valueOf(editTextagent.getText());
                    String Amount = String.valueOf(editTextamount.getText());
                    editTextcodehere.setText("RECHARGE :" + Agent + ":" + Amount);

                }
            });


            //// getbalance button
            btnbalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Client = String.valueOf(editTextclient.getText());
                    editTextcodehere.setText("GET BALANCE :" + Client);

                }
            });


            /// save button
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = new Date();
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    int year = calendar.get(Calendar.YEAR);
                    String mobchargeID;
                    mobchargeID = "MOB_CHARGE_" + year + "_";

                    // connect to Oracle DB
                    connecttoDB();

                    PreparedStatement stmtinsert1 = null;

                    try {
                        // if it is a new MOB_charge we will use insert
                        if (globalmobchargeid.equalsIgnoreCase("0")) {

                            Statement stmt1 = null;
                            stmt1 = conn.createStatement();
                            String sqlStmt = "select MOB_CHARGE_SEQ.nextval as nbr from dual";
                            ResultSet rs1 = null;
                            try {
                                rs1 = stmt1.executeQuery(sqlStmt);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            while (true) {
                                try {
                                    if (!rs1.next()) break;
                                    globalmobchargeid = mobchargeID + rs1.getString("nbr");
                                    //System.out.println(rs1.getString("compteur"));

                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            }
                            rs1.close();
                            stmt1.close();

                            // send data from fragment to super activity
                            ((ClientChargeInfoActivity) getActivity()).getfromfragment(globalmobchargeid);

                            stmtinsert1 = conn.prepareStatement("insert into MOBILE_CHARGE (MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS) values " +
                                    "('" + globalmobchargeid + "', '" + editTextagent.getText() + "', '" + editTextclient.getText() + "', sysdate,'" + editTextamount.getText() + "','" + editTextstatus.getText() + "')");


                        } else { // we wil use update where mobchargeid= the one we selected
                            stmtinsert1 = conn.prepareStatement("update  MOBILE_CHARGE  set AGENT_SUB_NUMBER='" + editTextagent.getText() + "',CLIENT_SUB_NUMBER='" + editTextclient.getText() + "',CHARGED_DATE=sysdate,AMOUNT='" + editTextamount.getText() + "',RECHARGE_STATUS='" + editTextstatus.getText() + "' where MOB_CHARGE_ID ='" + globalmobchargeid + "' ");
                        }

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        stmtinsert1.executeUpdate();
                        Toast.makeText(getActivity(), "Saving Completed", Toast.LENGTH_SHORT).show();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                    try {
                        stmtinsert1.close();
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                }
            });


            btndelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // connect to Oracle DB
                    connecttoDB();

                    PreparedStatement stmtinsert1 = null;
                    try {
                        // Delete Mob_charge_id

                        stmtinsert1 = conn.prepareStatement("delete  MOBILE_CHARGE   where MOB_CHARGE_ID ='" + globalmobchargeid + "' ");

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    try {
                        stmtinsert1.executeUpdate();
                        Toast.makeText(getActivity(), "Delete Completed", Toast.LENGTH_SHORT).show();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                    try {
                        stmtinsert1.close();
                        conn.close();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Intent intent = new Intent(getActivity(), ClientChargeListViewActivity.class);
                    startActivity(intent);

                }

            });


            //// return to main page
            btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });







            //counting the number of files
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File[] files = dir.listFiles();
            int count =0;
            for (File f: files)
            {
                String name = f.getName();
                if (name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" +count);
            }


            //BtnData appear in case offline files exist
            Button BtnData = (Button) V.findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(),ClientOfflineDataActivity.class);
                    startActivity(i);
                }
            });

            if (count !=0)
            {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }

            if (count >=5) {
                btnsave.setEnabled(false);
            }



        } else {


            // Creating the status dropdown spinner
            Spinner statusSpinner = (Spinner) V.findViewById(R.id.statusSpinner);
            TextView editTextstatus = (TextView) V.findViewById(R.id.editTextstatus);
            ArrayList<String> status = new ArrayList<>();
            status.add("In Progress");
            status.add("Success");
            status.add("Failed");
            status.add("Cancelled");

            ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    status);
            statusSpinner.setAdapter(statusAdapter);

            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String Mytext = statusSpinner.getSelectedItem().toString();
                    editTextstatus.setText(Mytext);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // End of status dropdown spinner


            // set the Agentsubnumber stable
            TextView tv = (TextView) V.findViewById(R.id.text_view);
            TextView editTextagent = (TextView) V.findViewById(R.id.editTextagent);

            File file = new File(getActivity().getFilesDir(), "MSISDN.txt");
            if (file.exists()) {

                StringBuilder text = new StringBuilder();

                try {
                    FileInputStream fIn = getActivity().openFileInput("MSISDN.txt");
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
                tv.setText(text.toString());
                tv.setVisibility(View.INVISIBLE);
                Result = String.valueOf(tv.getText());
                System.out.println("RESULT" + Result);
                String[] data = Result.split(":");
                String s0 = data[0];

                editTextagent.setText(s0);

            } else {
                System.out.println("login file don't exist");
            }
            // End Of set the Agentsubnumber stable


            //// recharge button
            Button btnrecharge = (Button) V.findViewById(R.id.btnrecharge);
            Button btnbalance = (Button) V.findViewById(R.id.btnbalance);
            TextView editTextcodehere = (TextView) V.findViewById(R.id.editTextcodehere);
            TextView editTextamount = (TextView) V.findViewById(R.id.editTextamount);
            TextView editTextclient = (TextView) V.findViewById(R.id.editTextclient);
            btnrecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Agent = String.valueOf(editTextagent.getText());
                    String Amount = String.valueOf(editTextamount.getText());
                    editTextcodehere.setText("RECHARGE :" + Agent + ":" + Amount);

                }
            });


            //// getbalance button
            btnbalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Client = String.valueOf(editTextclient.getText());
                    editTextcodehere.setText("GET BALANCE :" + Client);

                }
            });


            //counting the number of files
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File[] files = dir.listFiles();
            int count =0;
            for (File f: files)
            {
                String name = f.getName();
                if (name.endsWith(".txt"))
                    count++;
                System.out.println("COUNT IS:" +count);
            }


            //save button that write data and navigate to offlinedataRecyclerView
            Button btnsave = (Button) V.findViewById(R.id.btnsave);
            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView editTextagent = (TextView) V.findViewById(R.id.editTextagent);
                    TextView editTextclient = (TextView) V.findViewById(R.id.editTextclient);
                    TextView editTextamount = (TextView) V.findViewById(R.id.editTextamount);
                    String Agent = editTextagent.getText().toString()+"\n";
                    String Client = editTextclient.getText().toString()+"\n";
                    String Amount = editTextamount.getText().toString()+"\n";
                    String Status = editTextstatus.getText().toString();
                    String title = editTextclient.getText().toString();
                    try {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},23);
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        dir.mkdirs();
                        String fileName = "Client_" + title + ".txt";
                        File file = new File(dir,fileName);
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(Agent);
                        bw.write(Client);
                        bw.write(Amount);
                        bw.write(Status);
                        bw.close();
                        Intent intent = new Intent(getActivity(),ClientOfflineDataActivity.class);
                        startActivity(intent);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // End Of save button


            //BtnData appear in case offline files exist
            Button BtnData = (Button) V.findViewById(R.id.BtnData);
            BtnData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(),ClientOfflineDataActivity.class);
                    startActivity(i);
                }
            });

            if (count !=0)
            {
                BtnData.setVisibility(View.VISIBLE); //SHOW the button
                BtnData.setText(String.valueOf(count));
            }

            if (count >=5) {
                btnsave.setEnabled(false);
            }

        }

            return V;
    }

    public void connecttoDB() {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            conn = DriverManager.getConnection(url,userName,password);
            //Toast.makeText (MainActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity (),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }
    }

}


