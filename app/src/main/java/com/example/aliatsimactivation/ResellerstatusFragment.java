package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResellerstatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResellerstatusFragment extends Fragment {
    private RecyclerView resellerRecView;
    private int arraysize=0;
    private int varraysize=0;
    private int pagination=0;
    private String a;
    public Connection connsite;
    private Button btnprevious,btnnext,btnmain,btnsearch;
    public ArrayList<ResellerStatusListView> resellers,resellerdb;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResellerstatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mobilebalanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResellerstatusFragment newInstance(String param1, String param2) {
        ResellerstatusFragment fragment = new ResellerstatusFragment();
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
        View V= inflater.inflate(R.layout.fragment_client_status, container, false);
        Spinner s =(Spinner)V.findViewById(R.id.spinner);
        btnprevious=V.findViewById(R.id.btnPrevious2);
        btnnext=V.findViewById(R.id.btnNext2);
        btnmain=V.findViewById(R.id.btnMain);
        btnsearch=V.findViewById(R.id.btnSearch);

        // define recyclerview of sitelistview
        resellerRecView=V.findViewById(R.id.resellerrecview);
     btnsearch.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(s.getSelectedItemPosition()==0){
                 a="In Progress";
             }
             if(s.getSelectedItemPosition()==1){
                 a="Success";
             }
             if(s.getSelectedItemPosition()==2){
                 a="Failed";
             }
             if(s.getSelectedItemPosition()==3){
                 a="Canceled";
             }
             GetResellerData(1,5);

         }
     });
        btnprevious.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                pagination=pagination-2;
                if (pagination <=0 ) {pagination=0;}
                GetResellerData((pagination *5)+1,(pagination*5)+5);
            }

        });
        //button next
        btnnext.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                GetResellerData((pagination*5)+1,(pagination*5)+5);
            }
        });
        btnmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
    return V;}
    public void GetResellerData(int vfrom, int vto) {
        // connect to DB
        OraDB oradb= new OraDB();
        String url = oradb.getoraurl ();
        String userName = oradb.getorausername ();
        String password = oradb.getorapwd ();

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            connsite = DriverManager.getConnection(url,userName,password);
            // Toast.makeText (SpeedActivity.this,"Connected to the database",Toast.LENGTH_SHORT).show ();
        } catch (IllegalArgumentException | ClassNotFoundException | SQLException e) { //catch (IllegalArgumentException e)       e.getClass().getName()   catch (Exception e)
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (IllegalAccessException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        } catch (java.lang.InstantiationException e) {
            System.out.println("error is: " +e.toString());
            Toast.makeText (getActivity(),"" +e.toString(),Toast.LENGTH_SHORT).show ();
        }




        resellers =new ArrayList<>();
        resellerdb=new ArrayList<>();

        //Add data for sitelistview recyclerview
        Statement stmt1 = null;
        int i=0;
        try {
            stmt1 = connsite.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String  sqlStmt = "SELECT * FROM (select ROW_NUMBER() OVER (ORDER BY RES_CHARGE_ID) row_num,RES_CHARGE_ID,RESELLER_SUB_NUMBER, TO_DATE(TO_CHAR(CHARGED_DATE ,'DD-MM-YYYY'),'DD_MM_YYYY') as CHARGED_DATE ,AMOUNT, RECHARGE_STATUSE from RESELLER_CHARGE where  RECHARGE_STATUSE = '"+a+"' AND CHARGED_DATE >= sysdate-2 ) T WHERE row_num >= '" + vfrom +"' AND row_num <='" + vto +"'";

        ResultSet rs1 = null;
        try {
            rs1 = stmt1.executeQuery(sqlStmt);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        while (true) {
            try {
                if (!rs1.next()) break;
                arraysize=arraysize+1;
                resellerdb.add(new com.example.aliatsimactivation.ResellerStatusListView(rs1.getString("RES_CHARGE_ID"),rs1.getString("RESELLER_SUB_NUMBER"),rs1.getString("AMOUNT"),R.drawable.ic_baseline_send_24));
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
            connsite.close ();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        arraysize=resellerdb.size ();

        if (arraysize >0) {
            //System.out.println("Array Size is : "+arraysize);
            resellers.clear ( );
            varraysize = 0;
            for (i = varraysize; i < 5; i++) {
                if (varraysize < arraysize) {
                    resellers.add (new com.example.aliatsimactivation.ResellerStatusListView(resellerdb.get (i).getChargeID ( ), resellerdb.get (i).getMOBILENUMBER ( ), resellerdb.get (i).getAMOUNT ( ),R.drawable.ic_baseline_send_24));
                    varraysize = varraysize + 1;
                }
            }
            pagination = pagination + 1;
            //connect data to coveragelistadapter
            com.example.aliatsimactivation.ResellerStatusRecViewAdapter adapter = new com.example.aliatsimactivation.ResellerStatusRecViewAdapter(getActivity());
            adapter.setContacts(resellers);
            resellerRecView.setAdapter (adapter);
            resellerRecView.setLayoutManager (new LinearLayoutManager(getActivity()));
        }
    }
}