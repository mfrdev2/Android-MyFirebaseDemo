package com.frabbi.myfirebasedemo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.MultiFileSingleRowViewBinding;

import java.util.List;

public class MyMultiFileUploadAdapter extends RecyclerView.Adapter<MyMultiFileUploadAdapter.MyViewHolder> {
    List<String> fileNameList, statusList;

    public MyMultiFileUploadAdapter(List<String> fileNameList, List<String> statusList) {
        this.fileNameList = fileNameList;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final MultiFileSingleRowViewBinding vBind = MultiFileSingleRowViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(vBind);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String fileName = fileNameList.get(position);
        String status = statusList.get(position);
        holder.vBind.filename.setText(fileName);

        if (status.equals("Loading")) {
            holder.vBind.pbar.setImageResource(R.drawable.progress);
        } else {
            holder.vBind.pbar.setImageResource(R.drawable.check);
        }

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final MultiFileSingleRowViewBinding vBind;

        public MyViewHolder(@NonNull MultiFileSingleRowViewBinding vBind) {
            super(vBind.getRoot());
            this.vBind = vBind;
        }
    }
}
