package com.frabbi.myfirebasedemo.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {
    protected V mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBind = onCreateViewBind(savedInstanceState);
        setContentView(mBind.getRoot());
        setViewCreated(savedInstanceState);
    }

    protected abstract V onCreateViewBind(Bundle savedInstanceState);

    protected abstract void setViewCreated(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        mBind = null;
        super.onDestroy();
    }
}
