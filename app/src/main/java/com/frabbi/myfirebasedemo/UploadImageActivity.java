package com.frabbi.myfirebasedemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.frabbi.myfirebasedemo.databinding.ActivityUploadImageBinding;
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

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REC_CODE = 200;
    private ActivityUploadImageBinding mBinding;
    private Uri uriPath;
    private Bitmap bitmapIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUploadImageBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.browseBtn.setOnClickListener(this);
        mBinding.uploadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.browse_btn:
                pickImage();
                return;
            case R.id.upload_btn:
                uploadImage();
                return;
        }
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference upload = storage.getReference();
        StorageReference child = upload.child("images");

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("File uploader");

        if (uriPath != null) {
            dialog.show();
            child.putFile(uriPath)
                    .addOnSuccessListener(taskSnapshot -> {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload is Success", Toast.LENGTH_SHORT).show();
                    })
                    .addOnProgressListener(snapshot -> {
                        float percentage = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded: " + (int) percentage + " %");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("TAG", "uploadImage: "+e.getMessage(),e);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    });
        }


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
        startActivityForResult(intent, REC_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REC_CODE:
                if (resultCode == RESULT_OK) {
                    uriPath = data.getData();
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriPath);
                        bitmapIMG = BitmapFactory.decodeStream(inputStream);
                        mBinding.imageView.setImageBitmap(bitmapIMG);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return;
        }
    }
}