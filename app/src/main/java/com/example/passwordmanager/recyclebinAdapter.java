package com.example.passwordmanager;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclebinAdapter extends RecyclerView.Adapter <recyclebinAdapter.ViewHolder> {
    ArrayList<passwordEntryModel> binEntries;
    Context context;

    public recyclebinAdapter(Context c, ArrayList<passwordEntryModel> list)
    {
        context = c;
        binEntries = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_recyclebin_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(binEntries.get(position).getUsername());
        holder.tvPassword.setText(binEntries.get(position).getPassword());
        holder.tvURL.setText(binEntries.get(position).getURL());

        holder.ivRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDatabaseHelper = new DatabaseHelper(context);
                myDatabaseHelper.restoreEntryFromRecycleBin(binEntries.get(holder.getAdapterPosition()).getId());

                binEntries.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                Intent intent = new Intent(context,VaultActivity.class);
                startActivity(context,intent,null);
                ((Activity) context).finish();
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDatabaseHelper = new DatabaseHelper(context);
                myDatabaseHelper.deleteFromRecycleBin(binEntries.get(holder.getAdapterPosition()).getId());
                binEntries.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return binEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUsername, tvPassword,tvURL;
        ImageView ivRestore,ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvURL = itemView.findViewById(R.id.tvURL);

            ivRestore = itemView.findViewById(R.id.ivRestore);
            ivDelete=itemView.findViewById(R.id.ivDelete);
        }
    }
}

