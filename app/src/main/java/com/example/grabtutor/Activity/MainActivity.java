package com.example.grabtutor.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.example.grabtutor.Fragment.CategoryFragment;
import com.example.grabtutor.Fragment.SearchFragment;
import com.example.grabtutor.Fragment.ProfileFragment;
import com.example.grabtutor.Fragment.FavouriteFragment;
import com.example.grabtutor.Fragment.MessagesFragment;
import com.example.grabtutor.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button button_logout;
    private GoogleSignInClient mGoogleSignInClient;
    private BottomNavigationView bottomNav;
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new CategoryFragment();
                            break;
                        case R.id.nav_favourites:
                            selectedFragment = new FavouriteFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileFragment();
                            break;

                        //fragment that displays last seen messages
                        case R.id.nav_messages:
                            selectedFragment = new MessagesFragment();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;

                }
            };


    RecyclerView featuredRecycler;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new CategoryFragment()).commit();


        firebaseAuth = FirebaseAuth.getInstance();
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void status(String status) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        status("offline");
//    }


//    // Create request for google email (the pop - up)
//    private void createRequest() {
//
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//    }
//
//    private void signOut() {
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                });
//    }

}