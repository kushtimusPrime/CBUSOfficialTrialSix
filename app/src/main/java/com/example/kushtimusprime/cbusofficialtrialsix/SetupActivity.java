package com.example.kushtimusprime.cbusofficialtrialsix;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private Uri mainImageUri=null;
    private EditText setupName;
    private Button setupButton;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private ProgressBar setupBar;
    private FirebaseFirestore firebaseFirestore;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar setupToolbar=findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Profile Setup");
        profilePicture=(ImageView)findViewById(R.id.profilePicture);
        setupName=(EditText)findViewById(R.id.setupName);
        setupButton=(Button)findViewById(R.id.setupButton);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        userID=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore=FirebaseFirestore.getInstance();
        setupBar=(ProgressBar)findViewById(R.id.setupBar);
        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        String name=task.getResult().getString("name");
                        String image=task.getResult().getString("image");
                        setupName.setText(name);
                        RequestOptions placeholderRequest=new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.profile_picture);
                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(profilePicture);
                    } else {
                        Toast.makeText(SetupActivity.this,"Data doesn't exist",Toast.LENGTH_LONG).show();

                    }

                } else {
                    String errorMessage=task.getException().getMessage();
                    Toast.makeText(SetupActivity.this,"Firestore Retrieve  Error: "+errorMessage,Toast.LENGTH_LONG).show();
                }
            }
        });
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username=setupName.getText().toString();
                if(!TextUtils.isEmpty(username)&&mainImageUri!=null) {
                    userID=firebaseAuth.getCurrentUser().getUid();
                    setupBar.setVisibility(View.VISIBLE);
                    final StorageReference imagePath=storageReference.child("profile images").child(userID+".jpg");
                    imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()) {
                                Uri downloadURI=task.getResult().getDownloadUrl();
                                Map<String,String> userMap=new HashMap<>();
                                userMap.put("name",username);
                                userMap.put("image",downloadURI.toString());
                                firebaseFirestore.collection("Users").document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(SetupActivity.this,"User settings are updated",Toast.LENGTH_LONG).show();
                                            Intent mainIntent=new Intent(SetupActivity.this,MainActivity.class);
                                            startActivity(mainIntent);
                                            finish();

                                        } else {
                                            String errorMessage=task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this,"Firestore Error: "+errorMessage,Toast.LENGTH_LONG).show();
                                        }
                                        setupBar.setVisibility(View.INVISIBLE);

                                    }
                                });
                            } else {
                                String errorMessage=task.getException().getMessage();
                                Toast.makeText(SetupActivity.this,"Image Error: "+errorMessage,Toast.LENGTH_LONG).show();
                                setupBar.setVisibility(View.INVISIBLE);

                            }


                        }
                    });
                }
            }
        });
        profilePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this,"Please change your phone's settings to allow for storage access",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(SetupActivity.this);

                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SetupActivity.this);
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
                mainImageUri  = result.getUri();
                profilePicture.setImageURI(mainImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
