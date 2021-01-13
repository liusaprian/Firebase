package com.liusaprian.finalandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liusaprian.finalandroid.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding loginBinding;
    private boolean signedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        loginBinding.login.setOnClickListener(this);
        loginBinding.signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login :
                signedIn = false;
                validateLogin();
                break;
            case R.id.sign_up :
                Intent toSignUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(toSignUpActivity);
                break;
        }
    }

    private void checkFromDatabase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    if(loginBinding.email.getText().toString().equals(child.getValue(User.class).email) &&
                            loginBinding.password.getText().toString().equals(child.getValue(User.class).password)) {
                        Intent toMainActivity = new Intent(LoginActivity.this, MainActivity.class);
                        toMainActivity.putExtra("email", loginBinding.email.getText().toString());
                        toMainActivity.putExtra("name", child.getValue(User.class).name);
                        startActivity(toMainActivity);
                        signedIn = true;
                        finish();
                    }
                }
                if(!signedIn) Toast.makeText(LoginActivity.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateLogin() {
        if(loginBinding.email.getText().toString().isEmpty()) {
            loginBinding.email.setError("Email can't be empty");
            return;
        }
        if(loginBinding.password.getText().toString().isEmpty()) {
            loginBinding.password.setError("Password can't be empty");
            return;
        }
        checkFromDatabase();
    }
}