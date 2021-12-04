package com.frabbi.myfirebasedemo.rec_view_in_fragment;

import android.os.Bundle;

import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityFragmentContainerBinding;

public class FragmentContainerActivity extends BaseActivity<ActivityFragmentContainerBinding> {

    @Override
    protected ActivityFragmentContainerBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityFragmentContainerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {

    }
}