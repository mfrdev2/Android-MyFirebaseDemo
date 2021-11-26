package com.frabbi.myfirebasedemo.emailpassauth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityDashBoardBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends BaseActivity<ActivityDashBoardBinding> {

    @Override
    protected ActivityDashBoardBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityDashBoardBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        mBind.signOut.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(),
                    "sign out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
            finish();
        });
    }
}