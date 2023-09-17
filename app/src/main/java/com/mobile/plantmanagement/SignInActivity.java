package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    TextView tv_forgotPassword;
    Button btn_google;
    Button btn_facebook;
    LoginButton sign_in_Facebook_button;
    Button btn_login;
    TextView tv_goToRegister;

    // Firebase instance
    FirebaseAuth firebaseAuth;

    // Google sign-in
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private final int RC_GOOGLE_SIGN_IN = 1000;
    String emailInput;
    String passwordInput;

    // Facebook sign-in
    CallbackManager callbackManager;

//    // User Model
//    UserModel userModel;

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
//        btn_facebook = findViewById(R.id.signIn_btn_Facebook);


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
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Facebook SignIn init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        FacebookSdk.setIsDebugEnabled(true);
        callbackManager = CallbackManager.Factory.create();
        sign_in_Facebook_button = findViewById(R.id.sign_in_Facebook_button);
        Log.d(TAG, sign_in_Facebook_button.getText().toString());
        sign_in_Facebook_button.setPermissions(Arrays.asList("email", "public_profile"));
//        btn_facebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("public_profile"));
//            }
//        });
        sign_in_Facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle successful login
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                        AccessToken accessToken = loginResult.getAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                        Log.d(TAG, "registerCallback");
                        if(isLoggedIn){
                            Log.d(TAG, "get loginResult successfully");
                            firebaseAuthWithFacebook(accessToken);
                        } else {
                            Log.d(TAG, "Access token is NULL");
                        }
                    }

                    @Override
                    public void onCancel() {
                        // Handle canceled login
                        Log.d(TAG, "Facebook login cancelled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // Handle login error
                        Log.d(TAG, "Error login with Facebook", exception);
                    }
                });
    }

    public void signInGoogle(View view){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.d(TAG, "GoogleSignInAccount: " + task.toString());
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "Account ID Token" + account.getIdToken());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d(TAG, "Error getting result", e);
                Toast.makeText(getApplicationContext(), "can not get result", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information

                        Log.d(TAG, "Authentication successfully");
                        goToMainActivity();
                    } else {
                        Log.d(TAG, "Error authentication");
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        goToMainActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        //  Snackbar.make(findViewById(R.id.pop_up_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                    }
                });
    }

    private void goToMainActivity(){
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