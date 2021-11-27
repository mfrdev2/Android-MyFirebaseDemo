package com.frabbi.myfirebasedemo.image_slider;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.frabbi.myfirebasedemo.base.BaseActivity;
import com.frabbi.myfirebasedemo.databinding.ActivityImageSliderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageSliderActivity extends BaseActivity<ActivityImageSliderBinding> {

    @Override
    protected ActivityImageSliderBinding onCreateViewBind(Bundle savedInstanceState) {
        return ActivityImageSliderBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setViewCreated(Bundle savedInstanceState) {
        List<SlideModel> slideImages = new ArrayList<>();

        slideImages.add(new SlideModel("https://bit.ly/2YoJ77H", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.FIT));
        slideImages.add(new SlideModel("https://bit.ly/2BteuF2", "Elephants and tigers may become extinct.", ScaleTypes.FIT));
        slideImages.add(new SlideModel("https://bit.ly/3fLJf72", "And people do that.", ScaleTypes.FIT));

        FirebaseDatabase.getInstance().getReference().child("ImageSlider")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren())
                            slideImages.add(new SlideModel(data.child("imageUrl").getValue().toString(),data.child("title").getValue().toString(),ScaleTypes.FIT));
                        mBind.imageSlider.setImageList(slideImages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}