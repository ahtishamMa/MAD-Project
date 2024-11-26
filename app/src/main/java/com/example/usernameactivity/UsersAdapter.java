package com.example.usernameactivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<String> usersList;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(String username);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public UsersAdapter(List<String> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_activity_recycler_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String username = usersList.get(position);
        holder.textView.setText(username);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(username);
            }
            Intent i=new Intent(v.getContext(), ChatActivity.class);
            i.putExtra("username", username);
            v.getContext().startActivity(i);
        });
     holder.itemView.setOnLongClickListener(v -> {
         AlertDialog.Builder edit=new AlertDialog.Builder(v.getContext());
         edit.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 FirebaseDatabase.getInstance()
                         .getReference()
                         .child("usernames")
                         .removeValue()
                         .addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                             }
                         })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                         });
             }
         });
         edit.setPositiveButton("Cancel", (dialog, which) -> dialog.dismiss());
         edit.show();
         return false;
     });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvUsername);
        }
    }
}
