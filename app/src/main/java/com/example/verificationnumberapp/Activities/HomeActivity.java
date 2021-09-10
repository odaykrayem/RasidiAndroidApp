package com.example.verificationnumberapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.verificationnumberapp.Fragments.BillsFragment;
import com.example.verificationnumberapp.Fragments.OperationsFragment;
import com.example.verificationnumberapp.Fragments.UserProfileFragment;
import com.example.verificationnumberapp.R;
import com.example.verificationnumberapp.Utils.SharedPrefs;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView mBottomNavigationView;

    //    MaterialToolbar mToolBar;
//    ArrayList<String> titles;

    //to inflate all fragments at the beginning and prevent fragment recreations
    final Fragment mUserDetailsFragment = new UserProfileFragment();
    final Fragment mOperationFragment = new OperationsFragment();
    final Fragment mBillsFragment = new BillsFragment();

    //fragments manager to handel fragment transactions
    final FragmentManager fm = getSupportFragmentManager();
    //specify the active fragment we are working on right now
    Fragment active = mOperationFragment;
    FloatingActionButton floatingActionButton;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.e("user_id", SharedPrefs.getInt(this, SharedPrefs.KEY_USER_ID)+"");
        mBottomNavigationView = findViewById(R.id.home_screen_bottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        //setting up home fragments
        fm.beginTransaction().add(R.id.home_fragments_container, mUserDetailsFragment, "3").hide(mUserDetailsFragment).commit();
        fm.beginTransaction().add(R.id.home_fragments_container, mBillsFragment, "2").hide(mBillsFragment).commit();
        fm.beginTransaction().add(R.id.home_fragments_container,mOperationFragment, "1").commit();

        //setting starting values
        mBottomNavigationView.setSelectedItemId(R.id.nav_home);

        floatingActionButton = findViewById(R.id.fb);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddOperationActivity.class);
                startActivity(intent);
            }
        });
    }

    //when a tab is selected ...
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        BadgeDrawable badgeDrawable = mBottomNavigationView.getOrCreateBadge(R.id.nav_bills);
        badgeDrawable.setVisible(true);
// An icon only badgeDrawable will be displayed unless a number is set:
        badgeDrawable.setNumber(99);
        if (badgeDrawable != null) {
            badgeDrawable.setVisible(false);
            badgeDrawable.clearNumber();
        }
        switch (item.getItemId()) {
            case R.id.nav_home:
                fm.beginTransaction().hide(active).show(mUserDetailsFragment).commit();
                active = mUserDetailsFragment;
//                mToolBar.setTitle(titles.get(0));
                return true;

            case R.id.nav_operations:
                fm.beginTransaction().hide(active).show(mOperationFragment).commit();
                active = mOperationFragment;
                return true;

            case R.id.nav_bills:
                fm.beginTransaction().hide(active).show(mBillsFragment).commit();
                active = mBillsFragment;
                return true;
        }
        return false;
    }

    //handel onBackPressed actions
    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.home_fragments_container);
            //check if we are at the starting navigation point
            if(mBottomNavigationView.getSelectedItemId() != R.id.nav_operations){
                //no?...go back to the start point
                mBottomNavigationView.setSelectedItemId(R.id.nav_operations);
            }else{
                //yes then get out from the app
                super.onBackPressed();
            }
    }

}