package com.example.minh.lapitchat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Minh on 6/16/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder> {
    Context context;
    ArrayList<Friends> usersArrayList;

    public ChatAdapter(Context context, ArrayList<Friends> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }


    @Override
    public ChatAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_users_layout, null);
        ChatAdapter.ItemHolder itemHolder = new ChatAdapter.ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Friends friends = usersArrayList.get(position);
        holder.txtName.setText(friends.getName());
        holder.txtStatus.setText(friends.getDate());
        Picasso.with(context).load(friends.getImage())
                .placeholder(R.drawable.image1)
                .error(R.drawable.image1)
                .into(holder.imageView);
        if (friends.isOnline_status().equals("true")) {
            holder.online_status.setVisibility(View.VISIBLE);
        } else {
            holder.online_status.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public ImageView online_status;
        public TextView txtName, txtStatus;

        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
            txtName = (TextView) itemView.findViewById(R.id.user_single_name);
            txtStatus = (TextView) itemView.findViewById(R.id.user_single_status);
            online_status = itemView.findViewById(R.id.green_dot);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent chatIntent = new Intent(context, ChatActivity.class);
                    chatIntent.putExtra("user_id", FriendsFragment.list_user_friend_key.get(getPosition()));
                    chatIntent.putExtra("user_name", FriendsFragment.friendsArrayList.get(getPosition()).getName());
                    chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(chatIntent);

                }
            });
        }
    }
}
