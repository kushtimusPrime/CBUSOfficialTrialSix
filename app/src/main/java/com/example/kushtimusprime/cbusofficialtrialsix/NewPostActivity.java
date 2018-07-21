package com.example.kushtimusprime.cbusofficialtrialsix;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar newPostToolbar;
    private ImageView newPostImage;
    private EditText newPostDescription;
    private Button postButton;
    private Uri postImageUri=null;
    private ProgressBar postProgressBar;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newPostToolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add a new post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newPostImage=(ImageView)findViewById(R.id.newPostImage);
        newPostDescription=(EditText) findViewById(R.id.newPostDescription);
        postButton=(Button) findViewById(R.id.postButton);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        postProgressBar=(ProgressBar)findViewById(R.id.postProgressBar);
        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropWindowSize(512,512)
                        .start(NewPostActivity.this);
            }
        });
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String desc=newPostDescription.getText().toString();
                if(!TextUtils.isEmpty(desc)&&postImageUri!=null) {
                    postProgressBar.setVisibility(View.VISIBLE);
                    String randomName= FieldValue.serverTimestamp().toString();
                    StorageReference filePath=storageReference.child("Post Images").child(randomName+".jpg");
                    filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                String downloadUri=task.getResult().getDownloadUrl().toString();
                                Map<String,Object> postMap=new HashMap<>();
                                postMap.put("image uri",downloadUri);
                                postMap.put("desc",desc);
                                postMap.put("user id",currentUserID);
                                postMap.put("timestamp",FieldValue.serverTimestamp());
                                firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(NewPostActivity.this,"Post was successful",Toast.LENGTH_LONG).show();
                                            Intent mainIntent=new Intent(NewPostActivity.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();

                                        } else {
                                            String errorMessage=task.getException().getMessage();
                                            Toast.makeText(NewPostActivity.this,"Error: "+errorMessage,Toast.LENGTH_LONG).show();
                                        }
                                        postProgressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            } else {
                                postProgressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
                } else {
                    Toast.makeText(NewPostActivity.this,"Please select an image and/or write a description",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                postImageUri=result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
