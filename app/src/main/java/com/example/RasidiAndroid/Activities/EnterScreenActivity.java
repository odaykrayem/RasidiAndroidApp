package com.example.RasidiAndroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.RasidiAndroid.Adapters.EnetrScreenTabsAdapter;
import com.example.RasidiAndroid.Fragments.LoginTabFragment;
import com.example.RasidiAndroid.Fragments.RegisterTabFragment;
import com.example.RasidiAndroid.R;
import com.example.RasidiAndroid.Utils.SharedPrefs;
import com.google.android.material.tabs.TabLayout;

public class EnterScreenActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_screen);

        // check if the user is already logged in
        Boolean loginState = SharedPrefs.getBoolean(this, SharedPrefs.KEY_LOG_IN_STATE, false);
        if(loginState ){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        tabLayout = findViewById(R.id.login_tab_layout);
        viewPager = findViewById(R.id.login_view_pager);

        LoginTabFragment loginTabFragment = new LoginTabFragment();
        RegisterTabFragment registerTabFragment = new RegisterTabFragment();

        final EnetrScreenTabsAdapter enetrScreenTabsAdapter = new EnetrScreenTabsAdapter(getSupportFragmentManager(), 0);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        enetrScreenTabsAdapter.addFragment(loginTabFragment, "LogIn");
        enetrScreenTabsAdapter.addFragment(registerTabFragment, "Register");
        viewPager.setAdapter(enetrScreenTabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}