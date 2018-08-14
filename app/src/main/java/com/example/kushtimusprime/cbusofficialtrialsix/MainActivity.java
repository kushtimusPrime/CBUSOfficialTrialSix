package com.example.kushtimusprime.cbusofficialtrialsix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton addPostButton;
    private FloatingActionButton refreshButton;
    private FirebaseFirestore firebaseFirestore;
    private String currentUserID;
    private BottomNavigationView mainBottomNav;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;
    private ArrayList<BlogPost> blogPosts=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("CBUS");
        //starts user on homepage once logged in
        if (mAuth.getCurrentUser() != null){
        mainBottomNav = findViewById(R.id.mainBottomNav);
        addPostButton = (FloatingActionButton) findViewById(R.id.addPostButton);
        refreshButton=findViewById(R.id.refreshButton);
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        notificationFragment = new NotificationFragment();
        replaceFragment(notificationFragment);
        //allows user to navigate between screens with menu on bottom
        mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                   /* case R.id.bottomHome:
                        replaceFragment(homeFragment);
                        finish();
                        startActivity(getIntent());
                        return true;*/
                    case R.id.bottomAccount:
                        replaceFragment(accountFragment);
                        addPostButton.setVisibility(View.GONE);
                        refreshButton.setVisibility(View.GONE);
                        return true;
                    case R.id.bottomNotification:
                        replaceFragment(notificationFragment);
                        addPostButton.setVisibility(View.VISIBLE);
                        refreshButton.setVisibility(View.VISIBLE);
                        return true;
                    default:
                        return false;
                }
            }
        });
        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);

            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    }

    //checks to see if a user is already logged in and directs them accordingly
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null) {
            sendToLogin();
        } else {
            currentUserID=mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(currentUserID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        if(!task.getResult().exists()) {
                            Intent setUpIntent=new Intent(MainActivity.this,SetupActivity.class);
                            startActivity(setUpIntent);
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void sendToLogin() {
        Intent loginIntent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionLogoutButton:
                logOut();
                return true;

            default:

                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("RestrictedApi")
    private void makeButtonInvisible() {
        refreshButton.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    private void makeButtonVisible() {
        refreshButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        accountFragment.onActivityResult(requestCode, resultCode, data);
    }
}
