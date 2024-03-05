package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;
    String emailInput;
    String passwordInput;
    Button btn_register;
    TextView tv_returnSignIn;
    ActionBar actionBar;

    // Firebase instance
    FirebaseAuth firebaseAuth;
    // User Model
    boolean isLoggedIn = false;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_email = findViewById(R.id.signUp_et_email);
        et_password = findViewById(R.id.signUp_et_password);
        btn_register = findViewById(R.id.signUp_btn_register);
        tv_returnSignIn = findViewById(R.id.signUp_tv_returnSignIn);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Email SignIn
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
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
            firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUpActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        userModel = new UserModel();
                        userModel.setPersonEmail(emailInput);
                        userModel.setPersonName("User " + ++Constants.USER_INDEX);
                        Log.d("User info", userModel.toString());
                        intent.putExtra(Constants.LOGGED_IN, isLoggedIn);
                        intent.putExtra(Constants.ACCOUNT_INFO, (Parcelable) userModel);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        Log.d("Registration", task.getException().getMessage());
                    }
                }
            });
        }
    }

    public void goToSignIn (View view){
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}