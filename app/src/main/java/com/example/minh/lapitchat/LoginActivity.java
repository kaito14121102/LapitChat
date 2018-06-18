package com.example.minh.lapitchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout mEmail,mPass;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ProgressDialog mLoginDialog;
    private DatabaseReference mUsDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getEditText().getText().toString();
                String pass = mPass.getEditText().getText().toString();
                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                    mLoginDialog.setTitle("Logging in");
                    mLoginDialog.setMessage("Please wait while we check crendetial!");
                    mLoginDialog.setCanceledOnTouchOutside(false);
                    mLoginDialog.show();
                    LoginUser(email,pass);
                }
            }
        });
    }

    private void LoginUser(String email,String pass) {
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  mLoginDialog.dismiss();
                  String current_user_id=mAuth.getCurrentUser().getUid();
                  String deviceToken = FirebaseInstanceId.getInstance().getToken();
                  mUsDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                          mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(mainIntent);
                          finish();
                      }
                  });

              }else {
                  mLoginDialog.hide();
                  Toast.makeText(LoginActivity.this, "Can not sign in! Please try again", Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    private void init() {
        mEmail = findViewById(R.id.log_email);
        mPass = findViewById(R.id.log_pass);
        btnLogin = findViewById(R.id.btn_login);
        mToolbar = findViewById(R.id.login_toolbar);
        mLoginDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
}
