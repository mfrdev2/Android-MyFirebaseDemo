package com.frabbi.myfirebasedemo.tiktok;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.adapters.MyVideoAdapter;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityTikTokVideoBinding;
import com.frabbi.myfirebasedemo.model.VideoModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TikTokVideoActivity extends BaseActivity<ActivityTikTokVideoBinding> {
    private MyVideoAdapter adapter;

    @Override
    protected ActivityTikTokVideoBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityTikTokVideoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        DatabaseReference query = FirebaseDatabase.getInstance().getReference().child("Videos");
        FirebaseRecyclerOptions<VideoModel> options = new FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(query,VideoModel.class).build();

        adapter = new MyVideoAdapter(options);
        mBind.viewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}