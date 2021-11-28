package com.frabbi.myfirebasedemo.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.databinding.TiktokVideoViewBinding;
import com.frabbi.myfirebasedemo.model.VideoModel;

public class MyVideoAdapter extends FirebaseRecyclerAdapter<VideoModel, MyVideoAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyVideoAdapter(@NonNull FirebaseRecyclerOptions<VideoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull VideoModel model) {
        holder.videoLoad(model);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final TiktokVideoViewBinding mBind = TiktokVideoViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(mBind);
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        TiktokVideoViewBinding mBind;

        public MyViewHolder(@NonNull TiktokVideoViewBinding mBind) {
            super(mBind.getRoot());
            this.mBind = mBind;
        }
        private void videoLoad(VideoModel model) {
            mBind.titleText.setText(model.getTitle());
            mBind.descText.setText(model.getDesc());
            mBind.videoView.setVideoPath(model.getUrl());

            mBind.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mBind.progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mBind.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }
}
