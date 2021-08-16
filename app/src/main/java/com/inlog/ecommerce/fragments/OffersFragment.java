package com.inlog.ecommerce.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inlog.ecommerce.R;


public class OffersFragment extends Fragment {

    private RecyclerView recycleView;


    public OffersFragment() {
        // Required empty public constructor
    }

    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offeres, container, false);
    }
}
