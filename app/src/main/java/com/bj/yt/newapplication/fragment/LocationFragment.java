package com.bj.yt.newapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bj.yt.newapplication.R;

public class LocationFragment extends BaseFragment {
    private  View view;
    private TextView title_center;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);

        initView();
        return view;
    }

    private void initView() {
        title_center= (TextView)view.findViewById(R.id.title_center);
        title_center.setText("位置");
    }

}
