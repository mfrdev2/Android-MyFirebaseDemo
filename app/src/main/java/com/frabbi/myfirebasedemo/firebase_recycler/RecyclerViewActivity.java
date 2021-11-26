package com.frabbi.myfirebasedemo.firebase_recycler;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.adapters.MyRecyclerAdapter;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityRecyclerViewBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerViewActivity extends BaseActivity<ActivityRecyclerViewBinding> {
    private MyRecyclerAdapter myAdapter;
    @Override
    protected ActivityRecyclerViewBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityRecyclerViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        DatabaseReference contact = FirebaseDatabase.getInstance().getReference().child("Contact");
        FirebaseRecyclerOptions<ContactModel> options =
                new FirebaseRecyclerOptions.Builder<ContactModel>()
                        .setQuery(contact, ContactModel.class)
                        .build();
        myAdapter = new MyRecyclerAdapter(options);
        mBind.recViewContainer.setLayoutManager(new LinearLayoutManager(this));
        mBind.recViewContainer.setAdapter(myAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}