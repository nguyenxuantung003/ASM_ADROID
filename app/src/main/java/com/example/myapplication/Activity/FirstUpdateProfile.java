package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Profiles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FirstUpdateProfile extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText edtgender, edtage, edtheight, edtweight;
    Button btndone;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_update_profile);
        edtgender = findViewById(R.id.edt_update_profile_gender);
        edtage = findViewById(R.id.edt_update_profile_age);
        edtheight = findViewById(R.id.edt_update_profile_height);
        edtweight = findViewById(R.id.edt_update_profile_weight);
        btndone = findViewById(R.id.btn_update_profile_done);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Profiles profiles = new Profiles();
                String gender = edtgender.getText().toString();
                int age = Integer.parseInt(edtage.getText().toString());
                int height = Integer.parseInt(edtheight.getText().toString());
                int weight = Integer.parseInt(edtweight.getText().toString());
                double bmi = calculateBMI(height, weight);
                String updatedAt = getCurrentDateTime();
                Profiles newProfile = new Profiles(gender,age,height,weight,bmi,updatedAt);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("profile");

                profileRef.setValue(newProfile)
                        .addOnSuccessListener(aVoid -> {
                            // Xử lý sau khi lưu thành công, ví dụ hiển thị thông báo hoặc chuyển màn hình khác
                            Toast.makeText(FirstUpdateProfile.this, "Thông tin đã được lưu", Toast.LENGTH_SHORT).show();
                            // Ví dụ: Chuyển về màn hình chính
                            Intent intent = new Intent(FirstUpdateProfile.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Đóng ProfileActivity để người dùng không quay lại khi ấn nút back
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý khi lưu dữ liệu thất bại
                            Toast.makeText(FirstUpdateProfile.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

    }

    public double calculateBMI(double height, double weight) {
        // Công thức tính BMI: BMI = weight / (height * height)
        // Trong đó, height là chiều cao tính bằng mét, weight là cân nặng tính bằng kilogram
        if (height <= 0 || weight <= 0) {
            throw new IllegalArgumentException("Chiều cao và cân nặng phải lớn hơn 0.");
        }

        double heightInMeter = height / 100.0; // Chuyển chiều cao từ centimet sang mét

        double bmi = weight / (heightInMeter * heightInMeter);
        // Làm tròn chỉ số BMI đến 2 chữ số thập phân
        return Math.round(bmi * 100.0) / 100.0;
    }
    private String getCurrentDateTime() {
        // Lấy thời gian hiện tại dưới dạng chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}