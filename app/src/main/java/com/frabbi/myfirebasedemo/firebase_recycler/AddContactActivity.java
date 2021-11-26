package com.frabbi.myfirebasedemo.firebase_recycler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityAddContactBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContactActivity extends BaseActivity<ActivityAddContactBinding> {
    DatabaseReference contact;
    @Override
    protected ActivityAddContactBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityAddContactBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        contact = FirebaseDatabase.getInstance().getReference("Contact");
        mBind.addBtn.setOnClickListener(v->{
            addBtnDrive();
        });
        mBind.showBtn.setOnClickListener(v->{
            showBtnDrive();
        });
    }

    private void showBtnDrive() {
        startActivity(new Intent(AddContactActivity.this,RecyclerViewActivity.class));
    }

    private void addBtnDrive() {
        String name = mBind.nameId.getText().toString().trim();
        String phone = mBind.phoneId.getText().toString().trim();
        if(!name.isEmpty() && !phone.isEmpty()){
            contact.child(contact.push().getKey()).setValue(
                    new ContactModel(name,phone)
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    clear();
                    mBind.nameId.requestFocus();
                    Toast.makeText(getApplicationContext(), "Data Successfully Inserted", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void clear(){
        mBind.nameId.getText().clear();
        mBind.nameId.clearFocus();
        mBind.phoneId.getText().clear();
        mBind.phoneId.clearFocus();
    }
}