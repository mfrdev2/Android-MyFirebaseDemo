package com.frabbi.myfirebasedemo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.FirebaseRecViewBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.frabbi.myfirebasedemo.rec_view_in_fragment.BlankFragment;

public class MyFragmentRecViewAdapter extends FirebaseRecyclerAdapter<ContactModel,MyFragmentRecViewAdapter.MyViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyFragmentRecViewAdapter(@NonNull FirebaseRecyclerOptions<ContactModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ContactModel model) {
        holder.vBind.phoneId.setText(model.getPhone());
        holder.vBind.nameId.setText(model.getName());

        holder.itemView.setOnClickListener(v->{
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView,new BlankFragment(model.getName(), model.getPhone()))
                    .addToBackStack(null).commit();
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final FirebaseRecViewBinding vBind = FirebaseRecViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(vBind);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final FirebaseRecViewBinding vBind;
        public MyViewHolder(@NonNull FirebaseRecViewBinding vBind) {
            super(vBind.getRoot());
            this.vBind = vBind;
        }
    }
}
