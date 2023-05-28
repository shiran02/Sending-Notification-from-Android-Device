package com.cadenza.androidnotifictions;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNotificationActivity extends AppCompatActivity {

    private TextView emailTextView;
    private EditText EditTexteditTextTitle,EditTexteditTextBody;
    private Button btnSendNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        User user = (User)getIntent().getSerializableExtra("user");

        emailTextView = findViewById(R.id.textViewuser);
        EditTexteditTextTitle = findViewById(R.id.editTextTitle);
        EditTexteditTextBody = findViewById(R.id.editTextBody);
        btnSendNotification = findViewById(R.id.btnSend);

        emailTextView.setText("Sending To "+user.email);

        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(user);
            }
        });



    }

    private void sendNotification(User user) {
        String title = EditTexteditTextTitle.getText().toString().trim();
        String body = EditTexteditTextBody.getText().toString().trim();

        if(title.isEmpty()){
            EditTexteditTextTitle.setError("Title required");
            EditTexteditTextTitle.requestFocus();
            return;
        }

        if(body.isEmpty()){
            EditTexteditTextBody.setError("body required");
            EditTexteditTextBody.requestFocus();
            return;
        }

        //https://fcm.googleapis.com/v1/projects/androidnotifictions/messages:send

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com/v1/projects/androidnotifictions/messages:/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(user.token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

               //     Toast.makeText(SendNotificationActivity.this,response.body().toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}