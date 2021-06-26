package com.example.aliatsimactivation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    public ArrayList<ClientStatusListView> mobilecharge,mobilechargedb;
    DatePickerDialog datePickerDialog;


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

        //check network connection
        ConnectivityManager connMgr = (ConnectivityManager) getActivity ( )
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {



        Button btndate = (Button) V.findViewById(R.id.btndate);
        EditText  editTextdate = (EditText) V.findViewById (R.id.editTextdate);
        Button btnmain = (Button) V.findViewById(R.id.btnmain);
        Button btnprevious = (Button) V.findViewById(R.id.btnprevious);
        Button btnnext = (Button) V.findViewById(R.id.btnnext);
        remainstatusRecView = V.findViewById(R.id.remainstatusRecView);
        Spinner remainSpinner = (Spinner) V.findViewById(R.id.remainSpinner);


            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            editTextdate.setText(df.format(c));


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

                btndate.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View v) {
                        Calendar c = Calendar.getInstance ( );
                        int year = c.get (Calendar.YEAR);
                        int month = c.get (Calendar.MONTH);
                        int dayOfMonth = c.get (Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener ( ) {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                if (day < 10 && month < 9) {
                                    editTextdate.setText (year + "-0" + ( month + 1 ) + "-0" + day);
                                }
                                if (day > 10 && month < 9) {
                                    editTextdate.setText (year + "-0" + ( month + 1 ) + "-" + day);
                                }
                                if (day < 10 && month > 9) {
                                    editTextdate.setText (year + "-" + ( month + 1 ) + "-0" + day);
                                }
                                if (day > 10 && month > 9) {
                                    editTextdate.setText (year + "-" + ( month + 1 ) + "-" + day);
                                }
                            }
                        }, year, month, dayOfMonth);
                        datePickerDialog.show ( );

                    }

                });

                // On TextChanged action
                editTextdate.addTextChangedListener (new TextWatcher( ) {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        connecttoDB();
                        remainstatusRecView=V.findViewById(R.id.remainstatusRecView);
                        mobilecharge = new ArrayList<> ( );
                        mobilechargedb = new ArrayList<> ( );

                        //Add data for moobilechargelistview recyclerview
                        Statement stmt2 = null;
                        int i=0;
                        try {
                            stmt2 = conn.createStatement();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        String remain = remaining.get(position).toString();
                        String sqlStmt2 = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS,TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = '"+remain+"' AND TO_CHAR(CHARGED_DATE, 'YYYY-MM-DD') = '" + editTextdate.getText ( ) + "'";
                        System.out.println(remain);
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
                                mobilechargedb.add(new ClientStatusListView(rs2.getString("MOB_CHARGE_ID"),rs2.getString("CLIENT_SUB_NUMBER"),rs2.getString("AMOUNT")));
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                        try {
                            rs2.close ( );
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }
                        try {
                            stmt2.close ( );
                            conn.close ( );
                        } catch (SQLException throwables) {
                            throwables.printStackTrace ( );
                        }
                        arraysize=mobilechargedb.size ();
                        if (arraysize >0) {
                            //System.out.println("Array Size is : "+arraysize);
                            mobilecharge.clear ( );
                            varraysize = 0;
                            for (i = varraysize; i < 10; i++) {
                                if (varraysize < arraysize) {
                                    mobilecharge.add (new ClientStatusListView(mobilechargedb.get (i).getMOBCHARGEID ( ), mobilechargedb.get (i).getCLIENTSUBNUM ( ), mobilechargedb.get (i).getAMOUNT ( )));
                                    varraysize = varraysize + 1;
                                }
                            }
                            pagination = pagination + 1;
                            //connect data to ClientStatusRecViewAdapter
                            ClientStatusRecViewAdapter adapter = new ClientStatusRecViewAdapter(getActivity());
                            adapter.setContacts (mobilecharge);
                            remainstatusRecView.setAdapter (adapter);
                            remainstatusRecView.setLayoutManager (new LinearLayoutManager(getActivity()));
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //button previuos
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination = pagination - 2;
                if (pagination <= 0) {
                    pagination = 0;
                }
                GetMobileChargeData (( pagination * 10 ) + 1, ( pagination * 10 ) + 10);
            }

        });

        //button Next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetMobileChargeData (( pagination * 10 ) + 1, ( pagination * 10 ) + 10);
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

        } else {
            Toast.makeText(getActivity(), "No Connection",Toast.LENGTH_SHORT).show();

            Button btnmain = (Button) V.findViewById(R.id.btnmain);
            btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });

        }

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


    private void GetMobileChargeData(int vfrom, int vto) {

        connecttoDB();

        // define recyclerview of ClientStatuslistview
        remainstatusRecView=getView().findViewById(R.id.remainstatusRecView);
        mobilecharge = new ArrayList<>();
        mobilechargedb = new ArrayList<>();

        //Add data for moobilechargelistview recyclerview
        Statement stmt1 = null;
        int i = 0;
        try {
            stmt1 = conn.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER, AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE row_num >= '" + vfrom + "' AND row_num <='" + vto + "'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize = arraysize + 1;
                mobilechargedb.add(new ClientStatusListView(rs1.getString("MOB_CHARGE_ID") , rs1.getString("CLIENT_SUB_NUMBER"), rs1.getString("AMOUNT")));
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
        arraysize = mobilechargedb.size();

        if (arraysize > 0) {
            //System.out.println("Array Size is : "+arraysize);
            mobilecharge.clear();
            varraysize = 0;
            for (i = varraysize; i < 10; i++) {
                if (varraysize < arraysize) {
                    mobilecharge.add(new ClientStatusListView(mobilechargedb.get(i).getMOBCHARGEID(), mobilechargedb.get(i).getCLIENTSUBNUM(), mobilechargedb.get(i).getAMOUNT()));
                    varraysize = varraysize + 1;
                }
            }
            pagination = pagination + 1;
            //connect data to statuslistadapter
            ClientStatusRecViewAdapter adapter = new ClientStatusRecViewAdapter(getActivity());
            adapter.setContacts(mobilecharge);
            remainstatusRecView.setAdapter(adapter);
            remainstatusRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }


}
