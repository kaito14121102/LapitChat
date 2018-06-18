package com.example.minh.lapitchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Minh on 6/15/2018.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ItemHolder>{
    Context context;
    ArrayList<Messages> messagesArrayList;
    DatabaseReference mUserDatabase;

    public MessageAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @Override
    public MessageAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout, null);
        MessageAdapter.ItemHolder itemHolder = new MessageAdapter.ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        Messages messages    = messagesArrayList.get(position);

        SimpleDateFormat pre = new SimpleDateFormat("EEE MM dd HH:mm:ss zzz yyyy");
        holder.txtTime.setText(pre.format(messages.getTime())+"");

        String from_user = messages.getFrom();
        String message_type = messages.getType();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                holder.displayName.setText(name);
                Picasso.with(context).load(image)
                        .placeholder(R.drawable.image1)
                        .error(R.drawable.image1)
                        .into(holder.imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(message_type.equals("text")){
            holder.txtChat.setText(messages.getMessage());
            holder.imageViewChat.setVisibility(View.INVISIBLE);
        }else {
            holder.txtChat.setVisibility(View.INVISIBLE);
            Picasso.with(context).load(messages.getMessage())
                    .placeholder(R.drawable.image1)
                    .error(R.drawable.image1)
                    .into(holder.imageViewChat);
        }

//        if(current_user_id.equals(from_user)){
//            holder.txtChat.setBackgroundResource(R.drawable.message_text);
//            holder.txtChat.setTextColor(Color.BLACK);
//        }else {
//            holder.txtChat.setBackgroundResource(R.drawable.message_text_background);
//            holder.txtChat.setTextColor(Color.WHITE);
//
//        }

    }




    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView txtChat,txtTime,displayName;
        public ImageView imageViewChat;


        public ItemHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            txtChat = (TextView) itemView.findViewById(R.id.message_text_layout);
            displayName = itemView.findViewById(R.id.message_display_name);
            txtTime = itemView.findViewById(R.id.message_time);
            imageViewChat = itemView.findViewById(R.id.message_image_layout);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent profileIntent = new Intent(context, ProfileActivity.class);
//                    profileIntent.putExtra("user_id",UsersActivity.user_id_list.get(getPosition()));
//                    profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(profileIntent);
//                }
//            });
        }
    }
}
