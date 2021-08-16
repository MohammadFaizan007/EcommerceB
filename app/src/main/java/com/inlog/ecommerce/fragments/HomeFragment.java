package com.inlog.ecommerce.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inlog.ecommerce.R;


public class HomeFragment extends Fragment {
    Context context;
    TextView titlelbl;
    private RecyclerView recycleView;
    LinearLayout layout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        try {
            context = getActivity();
            titlelbl = (TextView) container.getRootView().findViewById(R.id.title);
            titlelbl.setText("Home");
            layout = (LinearLayout) container.getRootView().findViewById(R.id.layout);
            layout.setVisibility(View.VISIBLE);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
