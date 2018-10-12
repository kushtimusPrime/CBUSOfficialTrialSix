package com.trialsix.kushtimusprime.cbusofficialtrialsix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {
    private android.support.v7.widget.Toolbar newPostToolbar;
    private ImageView newPostImage;
    private EditText newPostDescription;
    private EditText eventName;
    private EditText eventTicketLink;
    private EditText eventAddress;
    private EditText eventDate;
    private Button postButton;
    private Spinner category;
    private Uri postImageUri=null;
    private ProgressBar postProgressBar;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private Bitmap compressedImageFile;
    private Object item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        newPostToolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.newPostToolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add a new post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        newPostImage=(ImageView)findViewById(R.id.newPostImage);
        newPostDescription=(EditText)findViewById(R.id.newPostDescription);
        eventName=(EditText)findViewById(R.id.eventName);
        eventTicketLink=(EditText)findViewById(R.id.eventTicketLink);
        eventAddress=(EditText)findViewById(R.id.eventAddress);
        eventDate =(EditText)findViewById(R.id.eventDate);
        postButton=(Button)findViewById(R.id.postButton);

        category = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                 item = adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });

        storageReference=FirebaseStorage.getInstance().getReference();
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
                final String name=eventName.getText().toString();
                final String ticket=eventTicketLink.getText().toString();
                final String address=eventAddress.getText().toString();
                final String date=eventDate.getText().toString();
                final String theCategory=(String)item;

                if(!TextUtils.isEmpty(desc)&&postImageUri!=null&&!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(ticket)&&!TextUtils.isEmpty(address)&&!TextUtils.isEmpty(date)) {
                    postProgressBar.setVisibility(View.VISIBLE);
                    final String randomName= UUID.randomUUID().toString();
                    StorageReference filePath=storageReference.child("Post Images").child(randomName+".jpg");
                    filePath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                            final String downloadUri=task.getResult().getDownloadUrl().toString();

                            if(task.isSuccessful()) {
                                File imageFile = new File(postImageUri.getPath());
                                try {
                                    compressedImageFile = new Compressor(NewPostActivity.this)
                                            .setMaxHeight(100)
                                            .setMaxWidth(100)
                                            .setQuality(2)
                                            .compressToBitmap(imageFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray();
                                UploadTask uploadTask = storageReference.child("Post Images/Thumbnails").child(randomName + ".jpg").putBytes(thumbData);
                                StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String downloadThumbUri = taskSnapshot.getDownloadUrl().toString();
                                        String blogID = firebaseFirestore.collection("Posts").document().getId();
                                        String theDate=date;
                                        String tickets=ticket;
                                        String theAddress=address;
                                        String title=name;
                                        final BlogPost blogPost = new BlogPost(currentUserID, downloadUri, downloadThumbUri, blogID, desc,date,tickets,address,title,theCategory);
                                        blogPost.setBlogID(blogID);
                                        firebaseFirestore.collection("Posts").document(blogID).set(blogPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(NewPostActivity.this, "Post was successful", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                        startActivity(mainIntent);
                                                        finish();
                                                    } else {
                                                        String errorMessage = task.getException().getMessage();
                                                        Toast.makeText(NewPostActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                                    }
                                                    postProgressBar.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });
                                       /* firebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(NewPostActivity.this, "Post was successful", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {
                                                    String errorMessage = task.getException().getMessage();
                                                    Toast.makeText(NewPostActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                                }
                                                postProgressBar.setVisibility(View.INVISIBLE);
                                            }
                                        });*/
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        String errorMessage = e.getMessage();
                                        Toast.makeText(NewPostActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                postProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }


                    });

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
