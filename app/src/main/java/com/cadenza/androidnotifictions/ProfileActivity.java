package com.cadenza.androidnotifictions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public static final String NODE_USERS = "users";

    private List<User> userList;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("update");

        //......get registation token ..........................................

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        saveToken(token);
                    } else {
                    }
                });

        progressBar = findViewById(R.id.progressBar);

        loadUsers();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    private void saveToken(String token) {

        //get current user email address ;
        String email = mAuth.getCurrentUser().getEmail();

        User user = new User(email ,token);

        //create firebase data base reference....
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);

        dbUsers.child(mAuth.getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProfileActivity.this,"Token save",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        userList = new ArrayList<>();

        recyclerView = findViewById(R.id.quick_trip_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        DatabaseReference dbUser = FirebaseDatabase.getInstance().getReference("users");
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                if(datasnapshot.exists()){
                    for(DataSnapshot dbuser :datasnapshot.getChildren()){
                        User user = dbuser.getValue(User.class);
                        userList.add(user);
                    }

                    userAdapter = new UserAdapter(ProfileActivity.this,userList);
                    recyclerView.setAdapter(userAdapter);

                }else{
                    Toast.makeText(ProfileActivity.this,"No users",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}