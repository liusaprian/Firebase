package com.liusaprian.finalandroid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.liusaprian.finalandroid.databinding.ActivityProfileBinding;


public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding profileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        profileBinding.name.setText(getIntent().getStringExtra("name"));
        profileBinding.email.setText(getIntent().getStringExtra("email"));
    }
}
