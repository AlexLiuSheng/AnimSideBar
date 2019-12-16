package com.allenliu.sidebardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.allenliu.sidebar.ISideBarSelectCallBack;
import com.allenliu.sidebar.SideBar;


public class SideBarDemoActivity extends AppCompatActivity {
 private SideBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar= (SideBar) findViewById(R.id.bar);
        bar.setOnStrSelectCallBack(new ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
          //      Toast.makeText(SideBarDemoActivity.this,selectStr,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
