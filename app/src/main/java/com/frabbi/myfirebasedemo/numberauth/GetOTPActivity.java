package com.frabbi.myfirebasedemo.numberauth;

import android.content.Intent;
import android.os.Bundle;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityGetOtpactivityBinding;

public class GetOTPActivity extends BaseActivity<ActivityGetOtpactivityBinding> {

    @Override
    protected ActivityGetOtpactivityBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityGetOtpactivityBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        mBind.getOtpBtn.setOnClickListener(v->{
            getOTPBtnDrive();
        });
    }

    private void getOTPBtnDrive() {

        mBind.ccp.registerCarrierNumberEditText(mBind.numberText);
        if(mBind.ccp.isValidFullNumber()){
            goToNext(mBind.ccp.getFullNumberWithPlus());
        }
    }

   private void goToNext(String fullNumberWithPlus){
        startActivity(new Intent(GetOTPActivity.this,OTPManageActivity.class)
                .putExtra("phone",fullNumberWithPlus));
    }
}