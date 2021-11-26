package com.frabbi.myfirebasedemo.numberauth;

import android.app.AlertDialog;
import android.os.Bundle;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityPDashBoardBinding;

public class DashBoardPActivity extends BaseActivity<ActivityPDashBoardBinding> {

    @Override
    protected ActivityPDashBoardBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityPDashBoardBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to exit this app?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    finishAffinity();
                })
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

}