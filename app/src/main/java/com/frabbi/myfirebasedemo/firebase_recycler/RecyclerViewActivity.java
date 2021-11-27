package com.frabbi.myfirebasedemo.firebase_recycler;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.adapters.MyRecyclerAdapter;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityRecyclerViewBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu_item, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                processSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processSearch(String str) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Contact")
                .orderByChild("name").startAt(str).endAt(str + "\uf8ff");
        FirebaseRecyclerOptions<ContactModel> options =
                new FirebaseRecyclerOptions.Builder<ContactModel>()
                        .setQuery(query, ContactModel.class)
                        .build();
        myAdapter = new MyRecyclerAdapter(options);
        myAdapter.startListening();
        mBind.recViewContainer.setAdapter(myAdapter);
    }


}