package com.example.aliatsimactivation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ClientChargeInfoActivity extends AppCompatActivity {
    TextView txtmobilechargeid;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_charge_info);
        txtmobilechargeid = findViewById(R.id.txtmobilechargeid);


        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabClientRecharge = findViewById(R.id.tabClientRecharge);
        TabItem tabClientStatus = findViewById(R.id.tabClientStatus);
        TabItem tabMobileTest = findViewById(R.id.tabClientReport);
        ViewPager viewPager = findViewById(R.id.viewPager);


        ClientChargePagerAdapter pagerAdapter  = new ClientChargePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //to focus on TAB name after selecting it like focus on Info or image or Scan
                System.out.println("tabs is : " + tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        viewPager.setCurrentItem(tab.getPosition());
                        break;
                }


            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //read passes value of mobilechargeid from recylerview
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        txtmobilechargeid.setText(str);

    }

    // to read data coming from fragment
    public void getfromfragment(String test) {
        try {
            txtmobilechargeid.setText(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String settofragment() {
        return txtmobilechargeid.getText().toString();

    }



}


