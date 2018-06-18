package com.example.minh.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Minh on 6/8/2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ItemHolder> {
    Context context;
    ArrayList<Users> usersArrayList;

    public UsersAdapter(Context context, ArrayList<Users> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_users_layout, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.txtName.setText(users.getName());
        holder.txtStatus.setText(users.getStatus());
        Picasso.with(context).load(users.getImage())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView txtName, txtStatus;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
            txtName = (TextView) itemView.findViewById(R.id.user_single_name);
            txtStatus = (TextView) itemView.findViewById(R.id.user_single_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("user_id",UsersActivity.user_id_list.get(getPosition()));
                    profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(profileIntent);
                }
            });
        }
    }
}
