package com.example.userenrolmentapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.sources);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        getSupportActionBar().hide();
        tabLayout.setupWithViewPager(viewPager);
        addList(adapter);
        findViewById(R.id.close).setOnClickListener( view -> {
            finish();
        });
    }

    void addList(ViewPagerAdapter adapter){
        adapter.addFragment(new UserList(),"Users");
        adapter.addFragment(new Enrol(),"Enroll");
        adapter.notifyDataSetChanged();
    }
}