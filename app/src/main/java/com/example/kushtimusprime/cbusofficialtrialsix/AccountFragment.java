package com.example.kushtimusprime.cbusofficialtrialsix;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.gms.maps.MapView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    View view;
    private CheckBox sportBox;
    private CheckBox musicBox;
    private CheckBox artBox;
    private CheckBox foodBox;
    private CheckBox academiaBox;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sportBox = view.findViewById(R.id.checkBox);
        musicBox = view.findViewById(R.id.checkBox2);
        artBox = view.findViewById(R.id.checkBox3);
        foodBox = view.findViewById(R.id.checkBox4);
        academiaBox = view.findViewById(R.id.checkBox5);

        }

    public void onChecked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox:
                if (checked)
                // Put some meat on the sandwich
                break;
            case R.id.checkBox2:
                if (checked)
                // Cheese me
                break;
            case R.id.checkBox3:
                if (checked)
                // Put some meat on the sandwich
                break;
            case R.id.checkBox4:
                if (checked)
                // Put some meat on the sandwich
                break;
            case R.id.checkBox5:
                if (checked)
                // Put some meat on the sandwich
                break;
        }
    }

}
