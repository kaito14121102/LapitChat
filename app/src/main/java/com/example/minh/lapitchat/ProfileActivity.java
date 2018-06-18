package com.example.minh.lapitchat;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView mProfileName,mProfileStatus,mProfileFriendsCount;
    private ImageView mProfileImage;
    private Button mProfileSendRqBtn,mProfileDecLineBtn;
    private DatabaseReference mUserDatabase;
    private ProgressDialog mProgressDialog;
    private String mcurrentState;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private FirebaseUser mCurrent_User;
    private String user_id;
    private DatabaseReference mNotificationDatabase;
    private DatabaseReference mRootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initWidget();
        init();
        even();
    }

    private void even() {
        mProfileSendRqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileSendRqBtn.setEnabled(false);

                //- ---------------------NOT FRIEND STATE-------------------------- -
                if(mcurrentState.equals("not_friends")){

                    DatabaseReference newNotificationref = mRootRef.child("notifications").child(user_id).push();
                    String newNotificationId = newNotificationref.getKey();
                    HashMap<String,String> notificationData = new HashMap<String, String>();
                    notificationData.put("from",mCurrent_User.getUid());
                    notificationData.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("Friend_rq/"+mCurrent_User.getUid() + "/" + user_id + "/request_type","sent");
                    requestMap.put("Friend_rq/"+user_id+"/"+mCurrent_User.getUid()+"/request_type","received");
                    requestMap.put("notifications/"+user_id+"/"+newNotificationId,notificationData);
                    mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){
                                Toast.makeText(ProfileActivity.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();
                            }
                            mProfileSendRqBtn.setEnabled(true);
                            mcurrentState="req_sent";
                            mProfileSendRqBtn.setText("Cancel Friend Request");

                        }
                    });
                }

                //- ---------------------CANCEL REQUEST STATE-------------------------- -
                if(mcurrentState.equals("req_sent")){
                    mFriendReqDatabase.child(mCurrent_User.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendReqDatabase.child(user_id).child(mCurrent_User.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProfileSendRqBtn.setEnabled(true);
                                    mcurrentState="not_friends";
                                    mProfileSendRqBtn.setText("Send Friend Request");

                                    mProfileDecLineBtn.setVisibility(View.INVISIBLE);
                                    mProfileDecLineBtn.setEnabled(false);
                                }
                            });
                        }
                    });
                }

                //- ------------------------- REQ RECEIVE STATE --------------------------
                if(mcurrentState.equals("req_received")){
                    final String currentDate = java.text.DateFormat.getDateTimeInstance().format(new Date());

                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/" + mCurrent_User.getUid() + "/" + user_id + "/date",currentDate);
                    friendsMap.put("Friends/" + user_id + "/" + mCurrent_User.getUid() + "/date",currentDate);

                    friendsMap.put("Friend_rq/" + mCurrent_User.getUid() + "/" + user_id,null);
                    friendsMap.put("Friend_rq/" + user_id + "/" + mCurrent_User.getUid(),null);
                    Toast.makeText(ProfileActivity.this, "ádasd", Toast.LENGTH_SHORT).show();

                    mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                mProfileSendRqBtn.setEnabled(true);
                                mcurrentState="friends";
                                mProfileSendRqBtn.setText("Unfriend this person");

                                mProfileDecLineBtn.setVisibility(View.INVISIBLE);
                                mProfileDecLineBtn.setEnabled(false);

                            }else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                //- ------------------------- UNFRIEND STATE --------------------------
                if(mcurrentState.equals("friends")){
                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/"+ mCurrent_User.getUid()+"/"+user_id,null);
                    unfriendMap.put("Friends/"+ user_id+"/"+mCurrent_User.getUid(),null);
                    mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError == null){
                                mcurrentState="not_friends";
                                mProfileSendRqBtn.setText("Send Friend Request");

                                mProfileDecLineBtn.setVisibility(View.INVISIBLE);
                                mProfileDecLineBtn.setEnabled(false);

                            }else {
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfileActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                            mProfileSendRqBtn.setEnabled(true);

                        }
                    });
                }
            }
        });
    }


    private void initWidget() {
        user_id = getIntent().getStringExtra("user_id");
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_rq");
        mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mProfileName = findViewById(R.id.profile_displayname);
        mProfileStatus = findViewById(R.id.profile_status);
        mProfileFriendsCount = findViewById(R.id.profile_total_friend);
        mProfileImage = findViewById(R.id.profile_image);
        mProfileSendRqBtn = findViewById(R.id.profile_btn_send_request);
        mProfileDecLineBtn = findViewById(R.id.profile_btn_decline_request);
        mcurrentState = "not_friends";
        mProfileDecLineBtn.setVisibility(View.INVISIBLE);
        mProfileDecLineBtn.setEnabled(false);
    }
    private void init() {
        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please wait while we load the user data.");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                mProfileName.setText(display_name);
                mProfileStatus.setText(status);
                Picasso.with(ProfileActivity.this)
                        .load(image)
                        .placeholder(R.drawable.image1)
                        .error(R.drawable.image1)
                        .into(mProfileImage);

                //-----------------------------FRIEND LIST / REQUEST FEATURE -----------
                mFriendReqDatabase.child(mCurrent_User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user_id)){
                            String req_type=dataSnapshot.child(user_id).child("request_type").getValue().toString();
                            if(req_type.equals("received")){
                                mcurrentState="req_received";
                                mProfileSendRqBtn.setText("Accept Friend Request");
                                mProfileDecLineBtn.setVisibility(View.VISIBLE);
                                mProfileDecLineBtn.setEnabled(true);
                            }else if(req_type.equals("sent")){
                                mcurrentState = "req_sent";
                                mProfileSendRqBtn.setText("Cancel Friend Request");
                                mProfileDecLineBtn.setVisibility(View.INVISIBLE);
                                mProfileDecLineBtn.setEnabled(false);
                            }
                            mProgressDialog.dismiss();

                        }else {
                            mFriendDatabase.child(mCurrent_User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(user_id)){
                                        Toast.makeText(ProfileActivity.this, "sdsd", Toast.LENGTH_SHORT).show();
                                        mcurrentState="friends";
                                        mProfileSendRqBtn.setText("Unfriend this person");
                                        mProfileDecLineBtn.setVisibility(View.INVISIBLE);
                                        mProfileDecLineBtn.setEnabled(false);
                                    }
                                    mProgressDialog.dismiss();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();

                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
