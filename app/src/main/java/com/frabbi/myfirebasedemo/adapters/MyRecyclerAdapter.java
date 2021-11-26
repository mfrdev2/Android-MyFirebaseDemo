package com.frabbi.myfirebasedemo.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.databinding.FirebaseRecViewBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;

public class MyRecyclerAdapter extends FirebaseRecyclerAdapter<ContactModel, MyRecyclerAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyRecyclerAdapter(@NonNull FirebaseRecyclerOptions<ContactModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ContactModel model) {
        Log.i("Adapter", "onBindViewHolder: "+model);

        holder.mBind.nameId.setText(model.getName());
        holder.mBind.phoneId.setText(model.getPhone());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FirebaseRecViewBinding mBind = FirebaseRecViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(mBind);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        FirebaseRecViewBinding mBind;
        public MyViewHolder(@NonNull FirebaseRecViewBinding mBind) {
            super(mBind.getRoot());
            this.mBind = mBind;
        }
    }
}
