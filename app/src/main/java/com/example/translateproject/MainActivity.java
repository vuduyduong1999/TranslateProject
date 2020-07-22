package com.example.translateproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.example.translateproject.fragment.DichFragment;
import com.example.translateproject.fragment.LSuFragment;
import com.example.translateproject.fragment.ThemTuFragment;
import com.example.translateproject.fragment.YoutubeFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    Fragment fragment;
    FragmentContainerView fragmentContainerView;
    WebView webViewYT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webViewYT = findViewById(R.id.wbyt);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentContainerView = findViewById(R.id.nav_host_fragment);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        int menu = intent.getIntExtra("menu",1);
        fragmentFromMenuActivity(menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_dich:
//                        getActionBar().setTitle("Dịch");
                        fragment =new DichFragment();
                        loadFragment(fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_themtu:
//                        getActionBar().setTitle("Thêm từ mới");
                        fragment = new ThemTuFragment();
                        loadFragment(fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_lichsu:
//                        getActionBar().setTitle("Lịch sử");
                        fragment = new LSuFragment();
                        loadFragment(fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.nav_youtube:
//                        getActionBar().setTitle("Liên kết kênh học tiếng anh");
                        fragment = new YoutubeFragment();
                        loadFragment(fragment);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void fragmentFromMenuActivity(int menu) {
        switch (menu)
        {
            case 1:
                fragment =new DichFragment();
                loadFragment(fragment);
                break;
            case 2:
                fragment = new ThemTuFragment();
                loadFragment(fragment);
                break;
            case 3:

                fragment = new LSuFragment();
                loadFragment(fragment);
                break;
            case 4:
                fragment = new YoutubeFragment();
                loadFragment(fragment);
                break;
            default: {
                fragment = new DichFragment();
                loadFragment(fragment);
            }
        }
    }

    public void loadFragment(Fragment frament)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,frament);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        if(webViewYT!= null && webViewYT.canGoBack())
            webViewYT.goBack();
        if(fragmentContainerView.getChildCount() == 1)
            this.finish();
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
//        if(fragmentContainerView.getChildCount() == 0)
//        {
//            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
//            startActivity(intent);
//            finish();
//        }
        else {
            super.onBackPressed();
        }
    }

}
