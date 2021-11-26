package com.frabbi.myfirebasedemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.frabbi.myfirebasedemo.databinding.ActivitySignUpWithImageBinding;
import com.frabbi.myfirebasedemo.model.StudentModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpWithImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REC_CODE = 200;
    private ActivitySignUpWithImageBinding mBind;
    private Uri uriPath;
    private Bitmap bitmap;
    private static StudentModel studentModel;
    //new activity result
    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = ActivitySignUpWithImageBinding.inflate(getLayoutInflater());
        setContentView(mBind.getRoot());
       //new Activity for result
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    uriPath = result.getData().getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriPath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        mBind.imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBind.floatingActionButton.setOnClickListener(this);
        mBind.saveBtn.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.floatingActionButton:
                pickImage();
                return;
            case R.id.save_btn:
                dataStoreInFirebase();
                return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void dataStoreInFirebase() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File uploader");

        FirebaseStorage sDb = FirebaseStorage.getInstance();
        StorageReference storageReference = sDb.getReference("Images");
        String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        StorageReference child = storageReference.child(dateTimeStr);
        if(uriPath !=null){
            progressDialog.show();
            child.putFile(uriPath) //pic upload
                    .addOnSuccessListener(taskSnapshot -> { //listener
                        progressDialog.dismiss();
                        child.getDownloadUrl().addOnSuccessListener(uri -> {
                           FirebaseDatabase db = FirebaseDatabase.getInstance();
                            DatabaseReference student = db.getReference("Student");
                            String key = student.push().getKey();

                            dataReady();
                            studentModel.setImagePath(uri.getPath());

                            student.child(key).setValue(studentModel) //data store
                                    .addOnSuccessListener(unused -> {
                                       Toast.makeText(getApplicationContext(),"Data Successfully Inserted",Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnProgressListener(snapshot -> {
                        float progress = (snapshot.getBytesTransferred()*100)/snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded: "+(int) progress+" %");
                    });
        }


    }

    private void dataReady() {
        int roll =-1;
        if(!mBind.roll.getText().toString().trim().isEmpty()){
            roll = Integer.valueOf(mBind.roll.getText().toString().trim());
        }
        String name = mBind.name.getText().toString().trim();
        String course = mBind.course.getText().toString().trim();
        int duration = -1;
        if(!mBind.duration.getText().toString().trim().isEmpty()){
            duration = Integer.valueOf(mBind.duration.getText().toString().trim());
        }
        String imagePath = uriPath.getPath();

       studentModel = new StudentModel(roll,name,course,duration);

    }

    private void pickImage() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        browseImage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    private void browseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
      //  startActivityForResult(Intent.createChooser(intent,"Select Image"),REC_CODE);
        activityResultLauncher.launch(Intent.createChooser(intent,"Select Image"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REC_CODE:
                if(resultCode == RESULT_OK){
                 uriPath = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriPath);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        mBind.imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;
        }
    }


}