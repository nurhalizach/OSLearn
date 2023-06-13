package com.example.oslearn;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity{

    EditText editTextEmail, editTextPassword, editTextUsername;
    Button buttonCreate;
    FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressBar progressBar;
    TextView loginLink;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        mAuth= FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        editTextEmail = findViewById(R.id.createTextEmailAddress);
        editTextPassword = findViewById(R.id.createTextPassword);
        editTextUsername = findViewById(R.id.createTextUsername);
        loginLink = findViewById(R.id.Login_Link);

        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, username, password;

                database = FirebaseDatabase.getInstance("https://oslearn-fee4d-default-rtdb.asia-southeast1.firebasedatabase.app");
                reference = database.getReference("users");

                email = String.valueOf(editTextEmail.getText());
                username = String.valueOf(editTextUsername.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignupActivity.this, "masukkan Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(SignupActivity.this, "masukkan Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "masukkan Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Account Created!",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    String uid = auth.getCurrentUser().getUid().toString();

                                    HelperClass helperClass = new HelperClass(username, email, password);
                                    reference.child(uid).setValue(helperClass);
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
