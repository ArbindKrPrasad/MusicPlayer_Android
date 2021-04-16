package com.arbind.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    public boolean isJsonMade = false;
    String permission[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public String myPath;

    //For Drawer
    DrawerLayout dl;
    ActionBarDrawerToggle abdt;
    Button upload;
    ImageView imageView;
    View view;
    int RESULT_LOAD_IMAGE = 101;
    int indx = -1;

    TextView userName, userMobile, userEmail;

    public static final String jsonPreference = "JPref";
    SharedPreferences jsp;
    public static final String uName = "userName";
    public static final String uMobile = "userMobile";
    public static final String uemail = "userEmail";

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, permission, 34);
        } else{
            execute();
        }

        jsp = this.getSharedPreferences(jsonPreference, Context.MODE_PRIVATE);

        dl = findViewById(R.id.dl);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.open, R.string.close);
        abdt.syncState();
        dl.addDrawerListener(abdt);

        logicOfnavigationDrawer();







    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 34){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1] == PackageManager.PERMISSION_GRANTED){
                execute();
            }
            else{
                finishAndRemoveTask();
            }
        }
    }

    public void execute(){
        tabLayout= findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setBackgroundColor(Color.rgb(0, 0, 0));
        tabLayout.setSelectedTabIndicatorColor(Color.rgb(255, 255, 255));

        tabLayout.setTabTextColors(Color.rgb(138, 138, 138), Color.rgb(255, 255, 255));
        tabLayout.addTab(tabLayout.newTab().setText("Player"));
        tabLayout.addTab(tabLayout.newTab().setText("Songs"));
        tabLayout.addTab(tabLayout.newTab().setText("About"));
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.fb));

        final Adapter adapter = new Adapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void logicOfnavigationDrawer(){
        NavigationView nv = findViewById(R.id.nvl);
        View header = nv.getHeaderView(0);


        userName = header.findViewById(R.id.tv4);
        userEmail = header.findViewById(R.id.tv3);


        String name = jsp.getString(uName, "UserName");
        String email = jsp.getString(uemail, "email");
        userName.setText("Hi "+name);
        userEmail.setText(jsp.getString(uemail, "email id"));


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment f = null;

//                View header = getLayoutInflater().inflate(R.layout.header_layout, null, false);
//
//                userName = header.findViewById(R.id.tv4);
//                userEmail = header.findViewById(R.id.tv3);
//
//
//                String name = jsp.getString(uName, "UserName");
//                String email = jsp.getString(uemail, "email");
//                userName.setText("Hi"+name);
//                userEmail.setText(jsp.getString(uemail, "email id"));

                int id = menuItem.getItemId();
                if(id==R.id.playermenu){
                    //f = new PlayerFragment();
                    indx = 0;
                }
                else if(id==R.id.songsmenu){
                    //f = new SonglistFragment();
                    indx = 1;
                }
                else if(id==R.id.about){

                    indx = 2;
                }

                if(indx!=-1){
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.fl, f);
//                    ft.commit();
//                    dl.closeDrawers();

                        TabLayout tabhost = (TabLayout) findViewById(R.id.tabLayout);
                        tabhost.getTabAt(indx).select();
                        dl.closeDrawers();


                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(abdt.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}