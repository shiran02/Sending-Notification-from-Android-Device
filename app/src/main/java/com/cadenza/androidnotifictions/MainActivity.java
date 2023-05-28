package com.cadenza.androidnotifictions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {



    //1. notification channel
    // 2. Notification Builder
    // 3. Notificaton Manager

    public static final String channel_id = "code_thread";
    private static final String channel_name = "code_thread";
    private static final String channel_desc = "Well come Code Threads";

    EditText EditTextEmail,EditTextPassword;
    Button LoginButton;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notiChannel = new NotificationChannel(channel_id,channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            notiChannel.setDescription(channel_desc);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notiChannel);
        }

        mAuth = FirebaseAuth.getInstance();

        EditTextEmail = findViewById(R.id.email);
        EditTextPassword = findViewById(R.id.password);
        LoginButton = findViewById(R.id.login_bn);
        progressBar = findViewById(R.id.progressBar);



        // ..............................



        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }
        });

    }

    private void CreateUser() {

        String email = EditTextEmail.getText().toString().trim();
        String password = EditTextEmail.getText().toString().trim();

        if(email.isEmpty()){
            EditTextEmail.setError("Email Required");
            EditTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            EditTextEmail.setError("Password Required");
            EditTextEmail.requestFocus();
            return;
        }

        if(password.length()<6){
            EditTextEmail.setError("password should 6 characters");
            EditTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startProfileActivity();
                        }else{

                            // email eka alredy have nam
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                userLoginActivity(email,password);
                            }else{
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

    }

    private void userLoginActivity(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startProfileActivity();
                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            startProfileActivity();
        }

    }

    private void startProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}