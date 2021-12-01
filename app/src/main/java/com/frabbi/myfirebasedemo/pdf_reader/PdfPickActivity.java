package com.frabbi.myfirebasedemo.pdf_reader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityPdfPickBinding;
import com.frabbi.myfirebasedemo.model.DocumentsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PdfPickActivity extends BaseActivity<ActivityPdfPickBinding> {
    private static final String TAG = "TAG";
    private Uri uri;

    @Override
    protected ActivityPdfPickBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityPdfPickBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        mBind.browsePdf.setOnClickListener(v -> {
            browsePdfBtnDrive();
        });

        mBind.submitPdf.setOnClickListener(v -> {
            submitPdfBtnDrive();
        });

        mBind.showDataList.setOnClickListener(v->{
            startActivity(new Intent(PdfPickActivity.this,PdfRecViewActivity.class));
        });
    }

    private void submitPdfBtnDrive() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        StorageReference child = FirebaseStorage.getInstance().getReference().child("PDF_FILE/" + System.currentTimeMillis() + ".pdf");
        progressDialog.setTitle("File Uploading....!!!");
        progressDialog.show();
        //file uploading
        child.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> child.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            progressDialog.dismiss();
                            mBind.imageView.setVisibility(View.INVISIBLE);
                            mBind.browsePdf.setText("Browse_pdf");

                            child.getDownloadUrl().addOnSuccessListener(uri1 -> {
                                //data save to realTime database
                                FirebaseDatabase.getInstance().getReference().child("Documents")
                                        .push().setValue(new DocumentsModel(
                                        mBind.fileName.getText().toString().trim(),
                                        uri1.toString()
                                )).addOnSuccessListener(unused -> {
                                   mBind.fileName.getText().clear();
                                });
                            });

                        }))
                .addOnProgressListener(snapshot -> {
                    float progress = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded: " + (int) progress + " %");
                });

    }

    private void browsePdfBtnDrive() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        launcher.launch(Intent.createChooser(intent, "Select pdf file."));
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

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                uri = result.getData().getData();
                mBind.browsePdf.setText("Browse_Again");
                mBind.imageView.setVisibility(View.VISIBLE);
                Log.i(TAG, "onActivityResult: " + uri.getPath());
            }
        }
    });
}