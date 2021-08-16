package com.inlog.ecommerce.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.inlog.ecommerce.fragments.FragmentDescription;
import com.inlog.ecommerce.fragments.FragmentSpecification;

public class AdapterDescriptionSpecification extends FragmentPagerAdapter {

    public AdapterDescriptionSpecification(FragmentManager fm) {
        super(fm);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        return new FragmentDescription();
        else
        return new FragmentSpecification();
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return "Description";
        else
            return  "Specification";

    }
}
