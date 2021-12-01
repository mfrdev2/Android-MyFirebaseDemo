package com.frabbi.myfirebasedemo.pdf_reader;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.adapters.MyPdfAdapter;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityPdfRecViewBinding;
import com.frabbi.myfirebasedemo.model.DocumentsModel;
import com.google.firebase.database.FirebaseDatabase;

public class PdfRecViewActivity extends BaseActivity<ActivityPdfRecViewBinding> {
    private MyPdfAdapter adapter;
    @Override
    protected ActivityPdfRecViewBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityPdfRecViewBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        FirebaseRecyclerOptions<DocumentsModel> options = new FirebaseRecyclerOptions.Builder<DocumentsModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Documents"),DocumentsModel.class)
                .build();
        adapter = new MyPdfAdapter(options);
        mBind.pdfRecView.setLayoutManager(new LinearLayoutManager(this));
        mBind.pdfRecView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }
}