package com.frabbi.myfirebasedemo.numberauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityOtpmanageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPManageActivity extends BaseActivity<ActivityOtpmanageBinding> {
    private String phoneNumber;
    private String otp;
    private FirebaseAuth mAuth;

    @Override
    protected ActivityOtpmanageBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityOtpmanageBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phone");
        }

        mAuth = FirebaseAuth.getInstance();
        initializeOTP();

        mBind.SignIn.setOnClickListener(v -> {
            signInBtnDrive();
        });
    }

    //==============================================================================================//
    private void initializeOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otp = s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                                Log.d("auto", "onVerificationCompleted:" + credential);
                                Toast.makeText(getApplicationContext(), ""+credential, Toast.LENGTH_SHORT).show();
                                signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.w("TAG", "onVerificationFailed", e);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInBtnDrive() {
        if(!mBind.otpText.getText().toString().trim().isEmpty()){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(
                    otp, mBind.otpText.getText().toString().trim()
            );
            signInWithPhoneAuthCredential(credential);
        }
        // goToNext();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        startActivity(new Intent(OTPManageActivity.this, DashBoardPActivity.class));
        finish();
    }

}