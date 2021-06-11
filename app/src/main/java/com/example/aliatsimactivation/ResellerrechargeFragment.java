package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResellerrechargeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResellerrechargeFragment extends Fragment {
    public Connection conn;
    private String globalchargeid,agentsub,clientsub,amount,status;
    //private SendMessage sendMessage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResellerrechargeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mobileInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResellerrechargeFragment newInstance(String param1, String param2) {
        ResellerrechargeFragment fragment = new ResellerrechargeFragment();
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
        View V= inflater.inflate(R.layout.fragment_client_recharge, container, false);
        Button btnsave = (Button) V.findViewById(R.id.btnsave);
        Button btndelete = (Button) V.findViewById(R.id.btndelete);
        Button btnmain = (Button) V.findViewById(R.id.btnmain);
        Button btnrecharge = (Button) V.findViewById(R.id.btnrecharge);
        Button btngetbalance = (Button) V.findViewById(R.id.btngetbalance);
        TextView editTextagentsub= (TextView) V.findViewById(R.id.editTextagentsub);
        TextView editTextclientsub= (TextView) V.findViewById(R.id.editTextresellersub);
        TextView editTextamount= (TextView) V.findViewById(R.id.editTextamount);
        TextView editTextget = (TextView) V.findViewById(R.id.editTextget);
        editTextagentsub.setText("");
        editTextclientsub.setText("");
        editTextamount.setText("");
        Spinner s =(Spinner)V.findViewById(R.id.myspinner);

        Intent intent = getActivity ().getIntent();
        String str = intent.getStringExtra("message_key");
        globalchargeid = str.toString();
        // connect to Oracle DB
        connecttoDB();

        PreparedStatement stmtinsert1 = null;

        try {
            // if it is a new Warehouse we will use insert
            if (globalchargeid!="0") {

                Statement stmt1 = null;
                stmt1 = conn.createStatement ( );
                String sqlStmt = "select AGENT_SUB_NUMBER,RESELLER_SUB_NUMBER,AMOUNT FROM RESELLER_CHARGE where RES_CHARGE_ID = '"+globalchargeid+"'";
                ResultSet rs1 = null;
                try {
                    rs1 = stmt1.executeQuery (sqlStmt);
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                while (true) {
                    try {
                        if (!rs1.next ( )) break;
                        editTextagentsub.setText(rs1.getString("AGENT_SUB_NUMBER"));
                        editTextclientsub.setText(rs1.getString("RESELLER_SUB_NUMBER"));
                        editTextamount.setText(rs1.getString("AMOUNT"));
                        //System.out.println(rs1.getString("compteur"));

                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }
                }
                rs1.close();
                stmt1.close();
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace ( );
        }


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextagentsub.getText().toString().matches("")||editTextclientsub.getText().toString().matches("")||editTextamount.getText().toString().matches("")){
                    Toast.makeText (getActivity (),"Insert All Fields",Toast.LENGTH_SHORT).show ();
                }
                else {
                String b = null;
                if(s.getSelectedItemPosition()==0){
                    b="In Progress";
                }
                if(s.getSelectedItemPosition()==1){
                    b="Success";
                }
                if(s.getSelectedItemPosition()==2){
                    b="Failed";
                }
                if(s.getSelectedItemPosition()==3){
                    b="Canceled";
                }
                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                String chargeID;
                chargeID= "CHARGE_"+year+"_" ;



                // connect to Oracle DB
                connecttoDB();

                PreparedStatement stmtinsert1 = null;

                try {
                    // if it is a new Warehouse we will use insert
                    if (globalchargeid.equalsIgnoreCase("0")) {

                        Statement stmt1 = null;
                        stmt1 = conn.createStatement ( );
                        String sqlStmt = "select RES_CHARGE_SEQ.nextval as nbr from dual";
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery (sqlStmt);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        while (true) {
                            try {
                                if (!rs1.next ( )) break;
                                globalchargeid=chargeID+rs1.getString ("nbr");
                                //System.out.println(rs1.getString("compteur"));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        // send data from fragment to super activity
                        ((ResellerChargeInfoActivity)getActivity()).getfromfragment(globalchargeid);

                        stmtinsert1 = conn.prepareStatement("insert into RESELLER_CHARGE (RES_CHARGE_ID,AGENT_SUB_NUMBER,RESELLER_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUSE,TOTAL_BALANCE) values " +
                                "('"+globalchargeid +"', '"+ editTextagentsub.getText()  +"', '"+ editTextclientsub.getText ()  +"', sysdate,'"+ editTextamount.getText () +"', '"+b+"', '"+editTextamount.getText()+"')");

                     /*   Bundle bundle = new Bundle();
                        bundle.putString("key1", globalwareid);
                        Imagefragment imgfr = new Imagefragment();
                        imgfr.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.infofragment,imgfr).commit();*/

                        ///added for pass data in fragment
                        //sendMessage.sendData(globalchargeid);

                    }

                    else { // we wil use update where wareid= the one we selected
                        stmtinsert1 = conn.prepareStatement("update  RESELLER_CHARGE  set CHARGED_DATE = sysdate,AGENT_SUB_NUMBER='"+ editTextagentsub.getText()  +"',RESELLER_SUB_NUMBER='"+ editTextclientsub.getText ()  +"',AMOUNT='"+ editTextamount.getText ()  +"',RECHARGE_STATUSE='"+ b +"',TOTAL_BALANCE='"+editTextamount.getText()+"' where RES_CHARGE_ID ='" + globalchargeid +"'");
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText (getActivity (),"Saving Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtinsert1.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


            }}
        });
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // connect to Oracle DB
                connecttoDB();

                PreparedStatement stmtinsert1 = null;
                try {
                    // Delete Ware_id

                    stmtinsert1 = conn.prepareStatement("delete  RESELLER_CHARGE   where RES_CHARGE_ID ='" + globalchargeid +"' ");

                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                try {
                    stmtinsert1.executeUpdate();
                    Toast.makeText (getActivity (),"Delete Completed",Toast.LENGTH_SHORT).show ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }


                try {
                    stmtinsert1.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }

                Intent intent = new Intent(getActivity(),ResellerChargeListViewActivity.class);
                startActivity(intent);

            }





        });
        btnrecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // connect to Oracle DB
                connecttoDB();
                editTextget.setText("");
                PreparedStatement stmtinsert1 = null;
                try {


                        Statement stmt1 = null;
                        stmt1 = conn.createStatement ( );
                        String sqlStmt = "Select AGENT_SUB_NUMBER, AMOUNT FROM RESELLER_CHARGE where res_charge_id='"+globalchargeid+"'" ;
                        ResultSet rs1 = null;
                        try {
                            rs1 = stmt1.executeQuery (sqlStmt);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }

                        while (true) {
                            try {
                                if (!rs1.next ( )) break;
                               editTextget.setText(rs1.getString("AGENT_SUB_NUMBER")+" : "+rs1.getString("AMOUNT"));
                                //System.out.println(rs1.getString("compteur"));

                            } catch (SQLException throwables) {
                                throwables.printStackTrace ( );
                            }
                        }
                        rs1.close();
                        stmt1.close();

                        // send data from fragment to super activity






                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
            }
        });
        btngetbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // connect to Oracle DB
                connecttoDB();
                editTextget.setText("");
                PreparedStatement stmtinsert1 = null;
                try {


                    Statement stmt1 = null;
                    stmt1 = conn.createStatement ( );
                    String sqlStmt = "Select RESELLER_SUB_NUMBER FROM RESELLER_CHARGE where res_charge_id='"+globalchargeid+"'" ;
                    ResultSet rs1 = null;
                    try {
                        rs1 = stmt1.executeQuery (sqlStmt);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace ( );
                    }

                    while (true) {
                        try {
                            if (!rs1.next ( )) break;
                            editTextget.setText(rs1.getString("RESELLER_SUB_NUMBER"));
                            //System.out.println(rs1.getString("compteur"));

                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }
                    }
                    rs1.close();
                    stmt1.close();

                    // send data from fragment to super activity






                } catch (SQLException throwables) {
                    throwables.printStackTrace ( );
                }
            }
        });

   return V; }
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