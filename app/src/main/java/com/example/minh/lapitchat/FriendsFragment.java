package com.example.minh.lapitchat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    View mainView;
    private DatabaseReference mFriendsDatabse;
    private DatabaseReference mUserDatabse;
    private FirebaseAuth mAuth;
    private String mCurrent_user_id;
    private RecyclerView mFriendRecycleView;
    public static ArrayList<Friends> friendsArrayList;
    private FriendsAdapter friendsAdapter;
    public static ArrayList<String> list_user_friend_key;
    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_friends,container,false);
        init();
        mFriendsDatabse.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String date = dataSnapshot.child("date").getValue().toString();
                String user_id = dataSnapshot.getKey().toString();
                mUserDatabse.child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("image").getValue().toString();
                        String userOnline = dataSnapshot.child("online").getValue().toString();

                        if(list_user_friend_key.contains(dataSnapshot.getKey().toString())){
                            int position = list_user_friend_key.indexOf(dataSnapshot.getKey().toString());
                            friendsArrayList.get(position).setOnline_status(userOnline);
                            friendsAdapter.notifyDataSetChanged();
                        }else {
                            list_user_friend_key.add(dataSnapshot.getKey().toString());
                            friendsArrayList.add(new Friends(date, name, image, userOnline));
                            friendsAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return mainView;
    }

    public void init(){
        mFriendRecycleView = mainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabse = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mUserDatabse = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDatabse.keepSynced(true);

        friendsArrayList = new ArrayList<>();
        list_user_friend_key = new ArrayList<>();
        friendsAdapter= new FriendsAdapter(getContext(),friendsArrayList);

        mFriendRecycleView.setHasFixedSize(true);
        mFriendRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFriendRecycleView.setAdapter(friendsAdapter);
    }
}
