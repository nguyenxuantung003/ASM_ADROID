package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Activity.Login_Activity;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button btnexit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnexit = findViewById(R.id.btnexit);
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndKeepUserData();
                startActivity(new Intent(MainActivity.this, Login_Activity.class));
                finish();
            }
        });

    }
    private void signOutAndKeepUserData() {
        // Đăng xuất người dùng khỏi Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Ví dụ: Làm một số xử lý khác nếu cần thiết, chẳng hạn như cập nhật UI

        // Tiếp tục giữ nguyên thông tin người dùng trên Realtime Database
        // Không có hành động cụ thể cần thực hiện ở đây vì Realtime Database không bị ảnh hưởng bởi việc đăng xuất
    }
}