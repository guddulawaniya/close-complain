package com.example.complaintclose.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.complaintclose.Fragments.current_complaint_Fragment;
import com.example.complaintclose.Fragments.total_complaint;

public class Tabs_complaintsAdapter extends FragmentPagerAdapter {
    public Tabs_complaintsAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new current_complaint_Fragment();
            case 1: return new total_complaint();
            default: return new total_complaint();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        String title =null;
        if(position==0)
        {
            title = "Complaint";

        }
        if(position==1)
        {
            title = "Total Complaints";

        }
        return title;
    }
}
