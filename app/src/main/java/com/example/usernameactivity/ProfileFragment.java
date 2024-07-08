package com.example.usernameactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;


public class ProfileFragment extends Fragment {
    TextInputEditText Username;
    ProgressBar Profileprogressbar;
    Button btnUpdateProfile;
    TextView Logout;


    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.fragment_profile, container, false);
        Username=view.findViewById(R.id.UpdateUsername);
        Profileprogressbar=view.findViewById(R.id.ProfileProgressBar);
        btnUpdateProfile=view.findViewById(R.id.btnUpdateProfile);
        Logout=view.findViewById(R.id.tvLogout);
        Profileprogressbar.setVisibility(View.GONE);
       return view;
    }
}