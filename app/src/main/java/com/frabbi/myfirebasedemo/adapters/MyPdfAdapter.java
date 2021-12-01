package com.frabbi.myfirebasedemo.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.databinding.PdfSingleRowViewBinding;
import com.frabbi.myfirebasedemo.model.DocumentsModel;
import com.frabbi.myfirebasedemo.pdf_reader.PdfViewActivity;

public class MyPdfAdapter extends FirebaseRecyclerAdapter<DocumentsModel,MyPdfAdapter.MyViewHOlder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyPdfAdapter(@NonNull FirebaseRecyclerOptions<DocumentsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHOlder holder, int position, @NonNull DocumentsModel model) {
        holder.vBind.fileName.setText(model.getFileName());
        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(holder.itemView.getContext(), PdfViewActivity.class);
            intent.putExtra("title",model.getFileName());
            intent.putExtra("fileUrl",model.getFilePath());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public MyViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final PdfSingleRowViewBinding vBind = PdfSingleRowViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHOlder(vBind);
    }

    protected static class MyViewHOlder extends RecyclerView.ViewHolder{
        final PdfSingleRowViewBinding vBind;
        public MyViewHOlder(@NonNull PdfSingleRowViewBinding vBind) {
            super(vBind.getRoot());
            this.vBind = vBind;
        }
    }
}
