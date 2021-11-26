package com.frabbi.myfirebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.frabbi.myfirebasedemo.databinding.ActivityMainBinding;
import com.frabbi.myfirebasedemo.model.ProfileModel;
import com.frabbi.myfirebasedemo.model.StudentModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private ActivityMainBinding mainBinding;
    private StudentModel studentModel;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        db = database.getReference("table");


        mainBinding.saveBtn.setOnClickListener(view -> {
            dataReady();
            String id = db.push().getKey();
       /*     db.child(id).setValue(studentModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "DataInserted", Toast.LENGTH_SHORT).show();
                        }
                    });*/
            db.child("-MpROX35q-OJze0vudK5").child("Profile").setValue(new ProfileModel("n.Rabbi","gmail@email","@Address","01775"))
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "DataInserted", Toast.LENGTH_SHORT).show();

                    });
        });

        mainBinding.datagetBtn.setOnClickListener(v->{
            dataRead();
        });

    }

    private void dataRead() {
        db.child("-MpROX35q-OJze0vudK5").child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel value = snapshot.getValue(ProfileModel.class);
                Log.i(TAG, "onDataChange:" + snapshot.getKey() +"\n" +value);
                for(DataSnapshot ss : snapshot.getChildren()){

                    if(ss != null) {

                        //return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dataReady() {
        int roll = Integer.valueOf(mainBinding.roll.getText().toString().trim());
        String name = mainBinding.name.getText().toString().trim();
        String course = mainBinding.course.getText().toString().trim();
        int duration = Integer.valueOf(mainBinding.duration.getText().toString().trim());
        studentModel = new StudentModel(
                roll, name, course, duration
        );
    }
}