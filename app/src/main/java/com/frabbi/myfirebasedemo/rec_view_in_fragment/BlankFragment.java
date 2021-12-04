package com.frabbi.myfirebasedemo.rec_view_in_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.FragmentBlankBinding;

public class BlankFragment extends Fragment {
    private FragmentBlankBinding vBind;
    private String name,phone;
    public BlankFragment() {
        // Required empty public constructor
    }

    public BlankFragment(String name,String phone){
        this.name = name;
        this.phone = phone;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vBind = FragmentBlankBinding.inflate(inflater, container, false);
        return vBind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i("BlankFragment", "onViewCreated: "+name+" "+phone);
    }
}