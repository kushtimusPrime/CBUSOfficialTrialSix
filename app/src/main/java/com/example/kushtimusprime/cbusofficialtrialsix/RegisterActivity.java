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

public class RegisterActivity extends AppCompatActivity {
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPassword;
    private Button regButton;
    private Button regLoginButton;
    private ProgressBar regProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        regEmail=(EditText)findViewById(R.id.regEmail);
        regPassword=(EditText)findViewById(R.id.regPass);
        regConfirmPassword=(EditText)findViewById(R.id.regConfirmPass);
        regButton=(Button)findViewById(R.id.regButton);
        regLoginButton=(Button)findViewById(R.id.regLoginButton);
        regProgress=(ProgressBar)findViewById(R.id.regProgress);
        regLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendToLoginPage();
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email=regEmail.getText().toString();
                String password=regPassword.getText().toString();
                String confirmPassword=regConfirmPassword.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(confirmPassword)) {
                    if(password.equals(confirmPassword)) {
                        regProgress.setVisibility(View.VISIBLE);
                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    sendToMain();
                                } else {
                                    String errorMessage=task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"Error: "+errorMessage,Toast.LENGTH_LONG).show();

                                }
                                regProgress.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this,"The Confirm Password field and the Password field do not match", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void sendToLoginPage() {
        Intent logInIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(logInIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        if(currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
