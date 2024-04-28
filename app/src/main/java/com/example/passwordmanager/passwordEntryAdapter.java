package com.example.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class passwordEntryAdapter  extends RecyclerView.Adapter <passwordEntryAdapter.ViewHolder> {
    ArrayList<passwordEntryModel> passwordEntries;
    Context context;

    public passwordEntryAdapter(Context c, ArrayList<passwordEntryModel> list)
    {
        context = c;
        passwordEntries = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_password_entry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(passwordEntries.get(position).getUsername());
        holder.tvPassword.setText(passwordEntries.get(position).getPassword());
        holder.tvURL.setText(passwordEntries.get(position).getURL());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("Confirmation");
                deleteDialog.setMessage("Do you really want to delete it?");
                deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete code
                        DatabaseHelper database = new DatabaseHelper(context);

                        database.deleteEntryAndMoveToRecycleBin(passwordEntries.get(holder.getAdapterPosition()).getId());

                        passwordEntries.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                deleteDialog.show();

                return false;
            }
        });
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog editDialog = new AlertDialog.Builder(context).create();
                View view = LayoutInflater.from(context).inflate(R.layout.edit_password_layout, null, false);
                editDialog.setView(view);

                EditText etuserName = view.findViewById(R.id.etUsername);
                EditText etPassword = view.findViewById(R.id.etPassword);
                EditText etURL= view.findViewById(R.id.etURL);
                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                Button btnCancel = view.findViewById(R.id.btnCancel);

                etuserName.setText(passwordEntries.get(holder.getAdapterPosition()).getUsername());
                etPassword.setText(passwordEntries.get(holder.getAdapterPosition()).getPassword());
                etURL.setText(passwordEntries.get(holder.getAdapterPosition()).getURL());

                editDialog.show();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userName = etuserName.getText().toString().trim();
                        String password = etPassword.getText().toString();
                        String URL =etURL.getText().toString().trim();
                        DatabaseHelper myDatabaseHelper = new DatabaseHelper(context);
                        myDatabaseHelper.updateEntry(passwordEntries.get(holder.getAdapterPosition()).getId(),userName, password,URL);

                        editDialog.dismiss();

                        passwordEntries.get(holder.getAdapterPosition()).setUsername(userName);
                        passwordEntries.get(holder.getAdapterPosition()).setPassword(password);
                        passwordEntries.get(holder.getAdapterPosition()).setURL(URL);
                        notifyDataSetChanged();

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return passwordEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvUsername, tvPassword,tvURL;
        ImageView ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvURL = itemView.findViewById(R.id.tvURL);
            ivEdit = itemView.findViewById(R.id.ivEdit);
        }
    }
}
