package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.plantmanagement.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    TextView tv_signIn;

    final String TAG = "MainActivity";
    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    ActionBar actionBar;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentsController();

        // Auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        // Sign In textview on Action bar
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        firebaseUser = firebaseAuth.getCurrentUser();

        // Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();
        googleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);

        changeFragment(new ProfileFragment());
        // Listen for changes in the back stack of fragments
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        MenuItem signInItem = menu.findItem(R.id.action_sign_in);
//        signInItem.setActionView(R.layout.sign_in_textview);
//        View view = signInItem.getActionView();
//        tv_signIn = view.findViewById(R.id.main_tv_sign_in);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateActionBarUI(user);
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);

        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void updateActionBarUI(FirebaseUser user){
        if(user == null) {
            actionBar.setCustomView(R.layout.sign_in_textview);
            tv_signIn = findViewById(R.id.main_tv_sign_in);
            tv_signIn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(intent);
            });
        } else {
            actionBar.setCustomView(R.layout.user_info_action_bar);
            TextView tv_sign_out = findViewById(R.id.main_tv_sign_out);
            ImageView imageView_user_icon = findViewById(R.id.main_imageView_user_icon);
            try{
                Picasso.get()
                        .load(getUserIcon(user))
                        .into(imageView_user_icon);
                Log.d(TAG, "Get user icon successfully.");
            } catch (Exception e){
                imageView_user_icon.setImageResource(R.drawable.ic_round_tag_faces_24);
                Log.d(TAG, "Get user icon failed.");
            }
            TextView tv_displayName = findViewById(R.id.main_tv_display_name);
            tv_displayName.setText(getUserDisplayName(user));
            Log.d(TAG, "user's displayName: " + tv_displayName.getText().toString());
            tv_sign_out.setOnClickListener(v -> {
                signOut();
            });
        }
    }
    private String getUserDisplayName(FirebaseUser user) {
        if (user != null) {
            String displayName = user.getDisplayName();
            return displayName;
        }
        return "Guest " + Constants.USER_INDEX++;
    }
    private String getUserIcon(FirebaseUser user) {
        if (user != null) {
            String photoUrl = user.getPhotoUrl().toString();
            return photoUrl;
        }
        return null;
    }
    private void fragmentsController(){
        // Always begin with Home fragment
        changeFragment(new HomeFragment());
        // Fragment navigation Controller
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.profile:
                    Log.d(TAG, "Binding Click profile fragment");
                    changeFragment(new ProfileFragment());
                    break;
                case R.id.home:
                    Log.d(TAG, "Binding Click home fragment");
                    changeFragment(new HomeFragment());
                    break;
                case R.id.chart:
                    Log.d(TAG, "Binding Click chart fragment");
                    changeFragment(new ChartFragment());
                    break;
                case R.id.settings:
                    Log.d(TAG, "Binding Click settings fragment");
                    changeFragment(new SettingsFragment());
                    break;
                default:
                    Log.d(TAG, "Binding not matched any fragment id");
            }
            return true;
        });
    }

    private void signOut (){
        firebaseAuth.signOut();
        logoutFromFacebook();
        logoutFromGoogle();
//        isLoggedIn = false;
//        updateActionBarUI();
    }
    private void logoutFromGoogle(){
        googleSignInClient.signOut().addOnCompleteListener(this, task -> Toast.makeText(getApplicationContext(), "Logout successfully", Toast.LENGTH_SHORT).show());
    }
    private void logoutFromFacebook(){
        LoginManager.getInstance().logOut();
    }

    protected void changeFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout1, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Up navigation for fragments
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (canGoBack()) {
                getSupportFragmentManager().popBackStack();
            }
        }
        Log.d(TAG, "onOptionItemSelected clicked.");
        return super.onOptionsItemSelected(item);
    }

    public void hideDisplayHomeUp() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }
    public void showDisplayHomeUp(){
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackStackChanged() {
        Log.d(TAG, "Click on Up navigation");
    }

    private boolean canGoBack(){
        return getSupportFragmentManager().getBackStackEntryCount() > 0;
    }
}