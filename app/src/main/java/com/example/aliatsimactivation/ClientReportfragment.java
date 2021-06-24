package com.example.aliatsimactivation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClientReportfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClientReportfragment extends Fragment {
    Connection conn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClientReportfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MobileTestfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClientReportfragment newInstance(String param1, String param2) {
        ClientReportfragment fragment = new ClientReportfragment();
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
        //return inflater.inflate(R.layout.fragment_client_reportfragment, container, false);
        View V = inflater.inflate (R.layout.fragment_client_reportfragment, container, false);

        //check network connection
        ConnectivityManager connMgr = (ConnectivityManager) getActivity ( )
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            TextView today_total = V.findViewById(R.id.today_total);
            TextView today_success = V.findViewById(R.id.today_success);
            TextView today_failed = V.findViewById(R.id.today_failed);
            TextView today_progress = V.findViewById(R.id.today_progress);
            TextView week_total = V.findViewById(R.id.week_total);
            TextView week_success = V.findViewById(R.id.week_success);
            TextView week_failed = V.findViewById(R.id.week_failed);
            TextView week_progress = V.findViewById(R.id.week_progress);
            TextView month_total = V.findViewById(R.id.month_total);
            TextView month_success = V.findViewById(R.id.month_success);
            TextView month_failed = V.findViewById(R.id.month_failed);
            TextView month_progress = V.findViewById(R.id.month_progress);
            Button btnmain = V.findViewById(R.id.btnmain);


            connecttoDB();
            Statement stmt1 = null;
            int i = 0;
            try {
                stmt1 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt1 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE CHARGED_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

            ResultSet rs1 = null;
            try {
                rs1 = stmt1.executeQuery(sqlStmt1);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs1.next()) break;
                    today_total.setText(rs1.getString("COUNT(*)"));
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
                //conn.close ( );
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //Today success report
            Statement stmt2 = null;
            try {
                stmt2 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt2 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Success' AND CHARGED_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

            ResultSet rs2 = null;
            try {
                rs2 = stmt2.executeQuery(sqlStmt2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs2.next()) break;
                    today_success.setText(rs2.getString("COUNT(*)"));
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
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            //Today failed report
            Statement stmt3 = null;
            try {
                stmt3 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt3 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Failed' AND CHARGED_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

            ResultSet rs3 = null;
            try {
                rs3 = stmt3.executeQuery(sqlStmt3);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs3.next()) break;
                    today_failed.setText(rs3.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs3.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt3.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            //Today In Progress report
            Statement stmt4 = null;
            try {
                stmt4 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt4 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'In Progress' AND CHARGED_DATE  >= cast(trunc(current_timestamp) as timestamp) ";

            ResultSet rs4 = null;
            try {
                rs4 = stmt4.executeQuery(sqlStmt4);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs4.next()) break;
                    today_progress.setText(rs4.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs4.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt4.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Week Total Report
            Statement stmt5 = null;
            try {
                stmt5 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt5 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE CHARGED_DATE >= sysdate -7 ";

            ResultSet rs5 = null;
            try {
                rs5 = stmt5.executeQuery(sqlStmt5);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs5.next()) break;
                    week_total.setText(rs5.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs5.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt5.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Week Success Report
            Statement stmt6 = null;
            try {
                stmt6 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt6 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Success' AND CHARGED_DATE >= sysdate -7 ";

            ResultSet rs6 = null;
            try {
                rs6 = stmt6.executeQuery(sqlStmt6);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs6.next()) break;
                    week_success.setText(rs6.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs6.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt6.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Week Failed Report
            Statement stmt7 = null;
            try {
                stmt7 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt7 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Failed' AND CHARGED_DATE >= sysdate -7 ";

            ResultSet rs7 = null;
            try {
                rs7 = stmt7.executeQuery(sqlStmt7);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs7.next()) break;
                    week_failed.setText(rs7.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs7.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt7.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Week In Progress Report
            Statement stmt8 = null;
            try {
                stmt8 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt8 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'In Progress' AND CHARGED_DATE >= sysdate -7 ";

            ResultSet rs8 = null;
            try {
                rs8 = stmt8.executeQuery(sqlStmt8);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs8.next()) break;
                    week_progress.setText(rs8.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs8.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt8.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Month Total Report
            Statement stmt9 = null;
            try {
                stmt9 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt9 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE CHARGED_DATE >= sysdate -30 ";

            ResultSet rs9 = null;
            try {
                rs9 = stmt9.executeQuery(sqlStmt9);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs9.next()) break;
                    month_total.setText(rs9.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs9.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt9.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Month Success Report
            Statement stmt10 = null;
            try {
                stmt10 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt10 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Success' AND CHARGED_DATE >= sysdate -30 ";

            ResultSet rs10 = null;
            try {
                rs10 = stmt10.executeQuery(sqlStmt10);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs10.next()) break;
                    month_success.setText(rs10.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs10.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt10.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Month Failed Report
            Statement stmt11 = null;
            try {
                stmt11 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt11 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'Failed' AND CHARGED_DATE >= sysdate -30 ";

            ResultSet rs11 = null;
            try {
                rs11 = stmt11.executeQuery(sqlStmt11);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs11.next()) break;
                    month_failed.setText(rs11.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs11.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt11.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // Month In Progress Report
            Statement stmt12 = null;
            try {
                stmt12 = conn.createStatement();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            String sqlStmt12 = "SELECT COUNT(*) FROM (select ROW_NUMBER() OVER (ORDER BY MOB_CHARGE_ID) row_num,MOB_CHARGE_ID,AGENT_SUB_NUMBER,CLIENT_SUB_NUMBER,CHARGED_DATE,AMOUNT,RECHARGE_STATUS, TOTAL_BALANCE from MOBILE_CHARGE order by MOB_CHARGE_ID) T WHERE RECHARGE_STATUS = 'In Progress' AND CHARGED_DATE >= sysdate -30 ";

            ResultSet rs12 = null;
            try {
                rs12 = stmt12.executeQuery(sqlStmt12);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            while (true) {
                try {
                    if (!rs12.next()) break;
                    month_progress.setText(rs12.getString("COUNT(*)"));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            try {
                rs12.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                stmt12.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // button main for going back to MainActiviity
            btnmain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(getActivity(), "No Connection",Toast.LENGTH_SHORT).show();
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