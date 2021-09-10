package com.example.verificationnumberapp.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class EnetrScreenTabsAdapter extends FragmentPagerAdapter {
   private Context context;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> fragmentTitles = new ArrayList<>();

   public EnetrScreenTabsAdapter(@NonNull FragmentManager fm, int behavior ){
       super(fm, behavior);
   }
    public void addFragment(Fragment fragment, String title){
        fragments.add(fragment);
        fragmentTitles.add(title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) { return fragments.get(position); }


    @Override
    public int getCount() { return fragments.size();    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
}
