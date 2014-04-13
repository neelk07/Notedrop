package com.example.android.geofence.home_fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.geofence.GeofenceMap;
import com.example.android.geofence.R;

/**
 * Created by neel on 4/13/14.
 */
public class MyNotes extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mynotes,null);
        return view;
    }


}