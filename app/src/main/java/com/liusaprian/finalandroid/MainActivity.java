package com.liusaprian.finalandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.liusaprian.finalandroid.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private ArrayList<Planet> planets = new ArrayList<>();
    private String email, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.planets.setHasFixedSize(true);

        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()) {
                    planets.add(item.getValue(Planet.class));
                }
                GridPlanetAdapter gridPlanetAdapter = new GridPlanetAdapter(planets);
                mainBinding.planets.setAdapter(gridPlanetAdapter);
                gridPlanetAdapter.setOnItemClickCallback(new GridPlanetAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemCLicked(Planet planet) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", planet.name);
                        bundle.putString("url", planet.url);
                        bundle.putString("desc", planet.description);
                        DetailFragment detailFragment = new DetailFragment();
                        detailFragment.setArguments(bundle);
                        Fragment fragment = fragmentManager.findFragmentByTag(DetailFragment.class.getSimpleName());

                        if(!(fragment instanceof DetailFragment)) {
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_container, detailFragment, DetailFragment.class.getSimpleName())
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mainBinding.planets.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent toProfileActivity = new Intent(this, ProfileActivity.class);
        toProfileActivity.putExtra("email", email);
        toProfileActivity.putExtra("name", name);
        startActivity(toProfileActivity);
        return super.onOptionsItemSelected(item);
    }
}