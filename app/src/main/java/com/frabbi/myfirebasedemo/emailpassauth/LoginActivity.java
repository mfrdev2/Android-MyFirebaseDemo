package com.frabbi.myfirebasedemo.emailpassauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding mBind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());

        mBind.signupBtn.setOnClickListener(this);
        mBind.loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                loginBtnDrive();
                return;
            case R.id.signup_btn:
                driveSignUpBtn();
                return;
        }
    }

    private void loginBtnDrive() {
        if(isValidEmail() && isValidPass()){
            mBind.progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(mBind.emailText.getText().toString().trim(),mBind.passwordText.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mBind.progressBar.setVisibility(View.INVISIBLE);
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCustomToken:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                mBind.progressBar.setVisibility(View.INVISIBLE);
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCustomToken:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Login failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
            finish();
        }
    }

    private boolean isValidPass() {
        String pass = mBind.passwordText.getText().toString().trim();
        if(!pass.isEmpty()){
            return true;
        }
        return false;
    }

    private boolean isValidEmail() {
        String email = mBind.emailText.getText().toString().trim();
        if(!email.isEmpty()){
            return true;
        }
        return false;
    }

    private void driveSignUpBtn() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // updateUI(user);
    }
}