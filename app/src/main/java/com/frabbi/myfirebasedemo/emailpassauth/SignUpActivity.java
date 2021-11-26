package com.frabbi.myfirebasedemo.emailpassauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.ActivitySignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySignUpBinding mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
        mBind.alreadyRegBtn.setOnClickListener(this);
        mBind.regBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg_btn:
                registerBtnDrive();
                return;
            case R.id.already_reg_btn:
                alreadyRegBtnDrive();
                return;
        }
    }

    private void registerBtnDrive() {
        if (isValidInputField()) {
            authenticEmailAndPass();
        }
    }

    private void authenticEmailAndPass() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mBind.progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(mBind.emailText.getText().toString().trim(),mBind.passwordConfirmText.getText().toString().trim())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mBind.progressBar.setVisibility(View.INVISIBLE);
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCustomToken:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        mBind.progressBar.setVisibility(View.INVISIBLE);
                        Log.w("TAG", "signInWithCustomToken:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Email Already Existed",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private Boolean isValidInputField() {
        if (isValidEmail() && isValidPass()) {
            return true;
        }
        return false;
    }

    private boolean isValidEmail() {
        if (mBind.emailText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email Field Blank", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private boolean isValidPass() {
        String pass = mBind.passwordText.getText().toString().trim();
        String pass2 = mBind.passwordConfirmText.getText().toString().trim();
        if (!pass.isEmpty() && !pass2.isEmpty()) {
            if (pass.equals(pass2)) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Check Your Password", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(getApplicationContext(), "Password Field Blank", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void alreadyRegBtnDrive() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        }
    }
}