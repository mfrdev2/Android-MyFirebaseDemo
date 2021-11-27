package com.frabbi.myfirebasedemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.frabbi.myfirebasedemo.R;
import com.frabbi.myfirebasedemo.databinding.FirebaseRecViewBinding;
import com.frabbi.myfirebasedemo.databinding.ViewCustomDialogBinding;
import com.frabbi.myfirebasedemo.model.ContactModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

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
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull ContactModel model) {
        Log.i("Adapter", "onBindViewHolder: " + model);

        holder.mBind.nameId.setText(model.getName());
        holder.mBind.phoneId.setText(model.getPhone());

        holder.itemView.setOnClickListener(v -> {
            updateAndDeleteOperation(holder.itemView.getContext(), position, model);
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FirebaseRecViewBinding mBind = FirebaseRecViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(mBind);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        FirebaseRecViewBinding mBind;

        public MyViewHolder(@NonNull FirebaseRecViewBinding mBind) {
            super(mBind.getRoot());
            this.mBind = mBind;
        }
    }


    private void updateAndDeleteOperation(Context context, int position, ContactModel model) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                .setContentHolder(new ViewHolder(R.layout.view_custom_dialog))
                //  .setExpanded(true)
                // .setCancelable(true)
                .setInAnimation(R.anim.abc_fade_in)
                .setOutAnimation(R.anim.abc_fade_out)
                .create();
        ViewCustomDialogBinding mBind = ViewCustomDialogBinding.bind(dialogPlus.getHolderView());
        mBind.nameId.setText(model.getName());
        mBind.phoneId.setText(model.getPhone());

        //update
        mBind.updateBtn.setOnClickListener(v -> {
            Map<String, Object> cMap = new HashMap<>();
            cMap.put("name", mBind.nameId.getText().toString().trim());
            cMap.put("phone", mBind.phoneId.getText().toString().trim());

            FirebaseDatabase.getInstance().getReference().child("Contact")
                    .child(getRef(position).getKey()).updateChildren(cMap,
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            dialogPlus.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Done " + ref, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        //delete
        mBind.deleteBtn.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference().child("Contact")
                    .child(getRef(position).getKey()).removeValue((error, ref) -> {
                dialogPlus.dismiss();
                Toast.makeText(context.getApplicationContext(), "Remove At:: " + ref, Toast.LENGTH_SHORT).show();
            });
        });

        dialogPlus.show();

    }
}
