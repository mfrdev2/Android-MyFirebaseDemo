package com.frabbi.myfirebasedemo.multiple_file_upload;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.frabbi.myfirebasedemo.adapters.MyMultiFileUploadAdapter;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityMultiFileUploadBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class MultiFileUploadActivity extends BaseActivity<ActivityMultiFileUploadBinding> {
    MyMultiFileUploadAdapter adapter;
    List<String> fileNameList, statusList;

    @Override
    protected ActivityMultiFileUploadBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityMultiFileUploadBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        fileNameList = new ArrayList<>();
        statusList = new ArrayList<>();
        mBind.recview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyMultiFileUploadAdapter(fileNameList, statusList);
        mBind.recview.setAdapter(adapter);

        mBind.btnUpload.setOnClickListener(v -> {
            if (!fileNameList.isEmpty()) {
                fileNameList.clear();
                statusList.clear();
                adapter.notifyDataSetChanged();
            }
            uploadBtnDrive();
        });
    }

    private void uploadBtnDrive() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        browseImages();
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

    private void browseImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launcher.launch(Intent.createChooser(intent, "Select Multiple File>>"));
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri uri = clipData.getItemAt(i).getUri();
                            String fileName = getFileName(uri);
                            fileNameList.add(fileName);
                            statusList.add("Loading");
                            adapter.notifyDataSetChanged();

                            //File Upload to FireStorage
                            final int index = i;
                            StorageReference fileUpload = FirebaseStorage.getInstance().getReference().child("MultipleFileUpload")
                                    .child(fileName);
                            fileUpload.putFile(uri)
                                    .addOnSuccessListener(taskSnapshot -> {
                                        statusList.remove(index);
                                        statusList.add(index, "done");
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                }
            }
        }
    });

    //getFileName from Uri
    @SuppressLint("Range")
    public String getFileName(Uri filepath) {
        String result = null;
        if (filepath.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(filepath, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                assert cursor != null;
                cursor.close();
            }
        }
        if (result == null) {
            result = filepath.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}