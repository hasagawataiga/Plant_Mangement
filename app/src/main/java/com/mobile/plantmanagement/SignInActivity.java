package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    TextView tv_forgotPassword;
    Button btn_google;
    Button btn_facebook;
    Button btn_login;
    TextView tv_goToRegister;

    // Firebase instance
    FirebaseAuth firebaseAuth;

    // Google sign in
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private final int GOOGLE_SIGN_IN_CODE = 1603;
    String emailInput;
    String passwordInput;

    // User Model
    UserModel userModel;

    private final String TAG = "SIGN_IN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Binding instances to those view id in layout
        et_email = findViewById(R.id.signIn_et_email);
        et_password = findViewById(R.id.signIn_et_password);
        tv_forgotPassword = findViewById(R.id.signIn_tv_forgotPassword);
        btn_google = findViewById(R.id.signIn_btn_google);
        btn_facebook = findViewById(R.id.signIn_btn_Facebook);
        btn_login = findViewById(R.id.btn_login);
        tv_goToRegister = findViewById(R.id.signIn_tv_goToRegister);
        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToResetPassword(v);
            }
        });
        tv_goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister(v);
            }
        });

        // Email SignIn
        firebaseAuth = FirebaseAuth.getInstance();

        // Google SignIn init
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
    }


    public void signInGoogle(View view){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                GoogleSignInAccount accountInfo = GoogleSignIn.getLastSignedInAccount(SignInActivity.this);
//                String personName = accountInfo.getDisplayName();
//                String personGivenName = accountInfo.getGivenName();
//                String personFamilyName = accountInfo.getFamilyName();
//                String personEmail = accountInfo.getEmail();
//                String personId = accountInfo.getId();
//                Uri personPhoto = accountInfo.getPhotoUrl();
//                userModel = new UserModel(personName, personGivenName, personFamilyName,
//                        personEmail, personId, personPhoto);
//                Log.d("User info", userModel.toString());
//                isLoggedIn = true;
                firebaseAuthWithGoogle(accountInfo);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "can not get result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        goToMainActivityByGoogleAccount();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        //  Snackbar.make(findViewById(R.id.pop_up_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                    }
                });
    }

    private void goToMainActivityByGoogleAccount(){
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void signIn (View view){
        emailInput = et_email.getText().toString();
        passwordInput = et_password.getText().toString();

        if(TextUtils.isEmpty(emailInput)){
            et_email.setError("Email is required");
            et_email.requestFocus();
        }else if(TextUtils.isEmpty(passwordInput)){
            et_password.setError("Password is required");
            et_password.requestFocus();
        }else if(passwordInput.length() < 7){
            et_password.setError("Password must be longer than 6 characters");
            et_password.requestFocus();
        }else{
            firebaseAuth.signInWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                        Intent mainActivityIntent = new Intent (SignInActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    }else{
                        Toast.makeText(SignInActivity.this, "Logged In failed", Toast.LENGTH_SHORT).show();
                        Log.d("Logged In", task.getException().getMessage());
                    }
                }
            });
        }
    }

    private void goToRegister (View view){
        Intent registerIntent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(registerIntent);
    }

    private void goToResetPassword (View view){
        Intent intent = new Intent (SignInActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}