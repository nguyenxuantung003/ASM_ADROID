package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText edtEmail,edtPass;
    private Button btnsingup;
    private TextView tv;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        auth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.singup_email);
        edtPass = findViewById(R.id.singup_pass);
        btnsingup = findViewById(R.id.btnsingup);
        tv = findViewById(R.id.tvsinguptologin);

        btnsingup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtEmail.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();

                if (user.isEmpty()){
                    edtEmail.setError("Khong bo trong ");
                }
                if (pass.isEmpty()){
                    edtPass.setError("Khong bo trong");
                } else {
                    auth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SingUpActivity.this,"Singup thanh cong", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SingUpActivity.this,Login_Activity.class));
                            } else {
                                Toast.makeText(SingUpActivity.this,"Singup khong thanh cong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SingUpActivity.this,Login_Activity.class));
            }
        });
    }
}