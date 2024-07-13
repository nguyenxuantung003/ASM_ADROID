package com.example.myapplication.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Users;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Login_Activity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference mdatabase;
    private GoogleSignInClient mgoogleSignInClient;
    private int RC_SIGN_IN = 20;
    private EditText edtemail,edtpass;
    private Button btnlogin;
    private TextView tv;
    private ImageView imgbtngg;
    private CallbackManager mCallbackManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //btnlogingg = findViewById(R.id.btnlogingoogle);
        edtemail = findViewById(R.id.login_email);
        edtpass = findViewById(R.id.login_pass);
        btnlogin = findViewById(R.id.btnlogin);
        tv = findViewById(R.id.tvlogintosingup);
        imgbtngg = findViewById(R.id.imagebtngoogle);

        FacebookSdk.sdkInitialize(getApplicationContext());


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mdatabase = database.getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mgoogleSignInClient = GoogleSignIn.getClient(this,gso);



        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.btnloginfacebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebook", "facebook:onCancel");
            }
            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "facebook:onError", error);
            }
        });

        imgbtngg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutAndGoogleSignIn();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail.getText().toString();
                String pass = edtpass.getText().toString();
                 if (!email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        if (!pass.isEmpty()){
                            auth.signInWithEmailAndPassword(email,pass)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(Login_Activity.this, "Login thanh cong",Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login_Activity.this,MainActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Login_Activity.this, "Login khong thanh cong",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            edtpass.setError("Khong duoc de trong pass");
                        }
                 } else if(email.isEmpty()){
                            edtemail.setError("Khong duoc de trong email");
                } else {
                     edtemail.setError("Kiem tra lai email");
                 }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,SingUpActivity.class));
            }
        });
    }
    private void googleSingIn() {
        Intent intent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        checkUserProfile(user);
                    } else {
                        // Xử lý khi đăng nhập thất bại
                        Log.w("gg", "signInWithCredential:failure", task.getException());
                        Toast.makeText(Login_Activity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentDateTime() {
        // Lấy thời gian hiện tại dưới dạng chuỗi
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void signOutAndGoogleSignIn() {
        // Sign out Firebase
        FirebaseAuth.getInstance().signOut();

        // Sign out Google
        mgoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                googleSingIn();
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("facebook", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null;
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("id",user.getUid());
                            map.put("name",user.getDisplayName());
                            map.put("profile",user.getPhotoUrl().toString());
                            database.getReference().child("users").child(user.getUid()).setValue(map);
                            startActivity(new Intent(Login_Activity.this, MainActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_Activity.this, "Đăng nhập Firebase thất bại.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void checkUserProfile(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();
            mdatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Kiểm tra xem profile có đầy đủ thông tin hay không
                        boolean hasCompleteProfile = snapshot.child("profile").exists()
                                && snapshot.child("profile").child("gender").exists()
                                && snapshot.child("profile").child("height").exists()
                                && snapshot.child("profile").child("weight").exists()
                                && snapshot.child("profile").child("bmi").exists();

                        if (hasCompleteProfile) {
                            // Chuyển đến màn hình chính
                            Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Chuyển đến màn hình cập nhật profile
                            Intent intent = new Intent(Login_Activity.this, FirstUpdateProfile.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        // Trường hợp người dùng mới, lưu thông tin cơ bản và chuyển đến màn hình cập nhật profile
                        saveUserData(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }
    }

    private void saveUserData(FirebaseUser user) {
        String userId = user.getUid();
        String username = user.getDisplayName();
        String email = user.getEmail();
        String profileUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";
        String createdAt = getCurrentDateTime(); // Lấy thời điểm hiện tại
        String updatedAt = createdAt; // Khởi tạo updatedAt bằng createdAt ban đầu

        // Tạo một đối tượng User để lưu vào Firebase Realtime Database
        Users newUser = new Users(userId, username,"", email, createdAt, updatedAt);

        // Lưu thông tin vào Firebase Realtime Database
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        usersRef.setValue(newUser)
                .addOnSuccessListener(aVoid -> {
                    checkUserProfile(auth.getCurrentUser());
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi lưu dữ liệu thất bại
                    Toast.makeText(Login_Activity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
}




}
