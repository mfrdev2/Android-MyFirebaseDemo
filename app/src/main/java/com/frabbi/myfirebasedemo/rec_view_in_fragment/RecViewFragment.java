package com.frabbi.myfirebasedemo.rec_view_in_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.adapters.MyFragmentRecViewAdapter;
import com.frabbi.myfirebasedemo.databinding.FirebaseRecViewBinding;
import com.frabbi.myfirebasedemo.databinding.FragmentRecViewBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.google.firebase.database.FirebaseDatabase;

public class RecViewFragment extends Fragment {
    private FragmentRecViewBinding mBind;
    private MyFragmentRecViewAdapter adapter;

    public RecViewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBind = FragmentRecViewBinding.inflate(inflater,container,false);
        return mBind.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseRecyclerOptions<ContactModel> options = new FirebaseRecyclerOptions.Builder<ContactModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Contact"),ContactModel.class)
                .build();
        adapter = new MyFragmentRecViewAdapter(options);

        mBind.recViewId.setLayoutManager(new LinearLayoutManager(getContext()));
        mBind.recViewId.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}