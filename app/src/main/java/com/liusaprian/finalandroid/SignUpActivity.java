package com.liusaprian.finalandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liusaprian.finalandroid.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySignUpBinding signUpBinding;
    private FirebaseAuth signUpAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        signUpBinding.signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_up) {
            if(!validateEmail(signUpBinding.newEmail.getText().toString())) {
                signUpBinding.newEmail.setError("Wrong email format");
                return;
            }
            if(signUpBinding.newName.length() < 5) {
                signUpBinding.newName.setError("Name must be at least 5 characters");
                return;
            }
            if(!signUpBinding.newPassword.getText().toString().equals(signUpBinding.newConfirm.getText().toString())) {
                signUpBinding.newConfirm.setError("Password is not the same");
                return;
            }

            createNewAccount(signUpBinding.newEmail.getText().toString(),
                    signUpBinding.newName.getText().toString(),
                    signUpBinding.newConfirm.getText().toString());
        }
    }

    private boolean validateEmail(String newEmail) {
        return newEmail.contains("@") && newEmail.endsWith(".com");
    }
    //realtime database * 2
    private void createNewAccount(String email, String name, final String password) {
        signUpAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").push();
                            ref.setValue(new User(signUpBinding.newName.getText().toString(),
                                    signUpBinding.newEmail.getText().toString(),
                                    signUpBinding.newConfirm.getText().toString()));
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}