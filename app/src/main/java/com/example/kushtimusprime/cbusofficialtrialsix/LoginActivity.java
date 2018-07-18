package com.example.kushtimusprime.cbusofficialtrialsix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPasswordText;
    private Button loginButton;
    private Button newAccountButton;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        loginEmailText= (EditText)findViewById(R.id.regEmail);
        loginPasswordText=(EditText)findViewById(R.id.regConfirmPass);
        loginButton=(Button)findViewById(R.id.LoginButton);
        newAccountButton=(Button)findViewById(R.id.NewAccountButton);
        loginProgress=(ProgressBar)findViewById(R.id.loginProgress);
        newAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginEmail=loginEmailText.getText().toString();
                String loginPass=loginPasswordText.getText().toString();
                if(!TextUtils.isEmpty(loginEmail)&&!TextUtils.isEmpty(loginPass)) {
                    loginProgress.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                sendToMain();
                            } else {
                                String e=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"Error: "+e, Toast.LENGTH_LONG).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= mAuth.getCurrentUser();
        if(currentUser!=null) {
            sendToMain();

        }
    }

    private void sendToMain() {
        Intent mainIntent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
