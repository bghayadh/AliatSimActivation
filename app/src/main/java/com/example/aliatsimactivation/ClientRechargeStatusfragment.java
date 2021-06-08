package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientRechargeStatusfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientRechargeStatusfragment extends Fragment {

    public Connection conn;
    private RecyclerView remainstatusRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    public ArrayList<RemainStatus> mobilecharge,mobilechargedb;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientRechargeStatusfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MobileBalancefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientRechargeStatusfragment newInstance(String param1, String param2) {
        ClientRechargeStatusfragment fragment = new ClientRechargeStatusfragment();
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
        //return inflater.inflate(R.layout.fragment_client_recharge_statusfragment, container, false);
        View V = inflater.inflate (R.layout.fragment_client_recharge_statusfragment, container, false);
        TextView txtreport= (TextView) V.findViewById(R.id.txtreport);
        Button btnmain = (Button) V.findViewById(R.id.btnmain);
        Spinner remainSpinner = (Spinner) V.findViewById(R.id.remainSpinner);


        // Creating the status remainig spinner
        ArrayList<String> remaining = new ArrayList<>();
        remaining.add("In Progress");
        remaining.add("Failed");

        ArrayAdapter<String> remainingAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                remaining);
        remainSpinner.setAdapter(remainingAdapter);

        remainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), remaining.get(position) + " Selected" ,Toast.LENGTH_SHORT).show();
                txtreport.setText(remaining.get(position) + " "+ "clients recharges from last 2 days");


                // creating the remaining status list
                connecttoDB();
                remainstatusRecView=V.findViewById(R.id.remainstatusRecView);
                mobilecharge =new ArrayList<>();
                mobilechargedb=new ArrayList<>();

                //Add data for moobilechargelistview recyclerview
                Statement stmt2 = null;
                int i=0;
                try {
                    stmt2 = conn.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                String remain = remaining.get(position).toString();
                String  sqlStmt2 = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = '"+remain+"' AND CHARGED_DATE >= sysdate -2 ORDER BY CHARGED_DATE ASC";
                ResultSet rs2 = null;
                try {
                    rs2 = stmt2.executeQuery(sqlStmt2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true) {
                    try {
                        if (!rs2.next()) break;
                        arraysize=arraysize+1;
                        mobilechargedb.add(new RemainStatus(rs2.getString("MOB_CHARGE_ID"),rs2.getString("CLIENT_SUB_NUMBER"),rs2.getString("AMOUNT")));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                try {
                    rs2.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    stmt2.close();
                    conn.close ();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                arraysize=mobilechargedb.size ();
                if (arraysize >0) {
                    //System.out.println("Array Size is : "+arraysize);
                    mobilecharge.clear ( );
                    varraysize = 0;
                    for (i = varraysize; i < 10; i++) {
                        if (varraysize < arraysize) {
                            mobilecharge.add (new RemainStatus(mobilechargedb.get (i).getMOBCHARGEID ( ), mobilechargedb.get (i).getCLIENTSUBNUM ( ), mobilechargedb.get (i).getAMOUNT ( )));
                            varraysize = varraysize + 1;
                        }
                    }
                    pagination = pagination + 1;
                    //connect data to MobileChargeRecViewadapter
                    RemainStatusRecViewAdapter adapter = new RemainStatusRecViewAdapter (getActivity());
                    adapter.setContacts (mobilecharge);
                    remainstatusRecView.setAdapter (adapter);
                    remainstatusRecView.setLayoutManager (new LinearLayoutManager(getActivity()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // button main for going back to MainActiviity
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        return V;
    }

    // connecting to database

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
