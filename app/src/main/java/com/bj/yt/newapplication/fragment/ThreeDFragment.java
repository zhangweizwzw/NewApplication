package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bj.yt.newapplication.R;

public class ThreeDFragment extends BaseFragment {
    private  View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_threed, container, false);

        return view;
    }

}
