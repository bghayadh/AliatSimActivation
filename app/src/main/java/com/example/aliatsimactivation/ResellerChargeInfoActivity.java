package com.example.aliatsimactivation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ResellerChargeInfoActivity extends AppCompatActivity{
    TextView txtchargeid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_charge);
        txtchargeid = findViewById(R.id.txtchargeid);
        TabLayout tabLayout = findViewById(R.id.tabBar1);
        TabItem tabChats = findViewById(R.id.tabmInfo);
        TabItem tabStatus = findViewById(R.id.tabbalance);
        TabItem tabCalls = findViewById(R.id.tabtest);
        ViewPager viewPager = findViewById(R.id.viewPager);


        ResellerPagerAdapter pagerAdapter = new ResellerPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
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


        //read passes value of ware_id from recylserview
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        txtchargeid.setText(str);
    }
    public void getfromfragment(String test) {
        try {
            txtchargeid.setText(test);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String settofragment() {
        return txtchargeid.getText().toString();

    }


}