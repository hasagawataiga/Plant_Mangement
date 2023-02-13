package com.mobile.plantmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    // Firebase instance
    FirebaseAuth firebaseAuth;

    Button btn_resetPassword;
    EditText et_email;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        et_email = findViewById(R.id.resetPassword_et_email);
        btn_resetPassword = findViewById(R.id.resetPassword_btn_reset);
    }

    public void resetPassword (View view){
        email = et_email.getText().toString();
        if(TextUtils.isEmpty(email)){
            et_email.setError("Email is required");
        }else{
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Sent an email to " + email, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                                startActivity(intent);
                                Log.d("Reset email", "Email sent to " + email);
                            }else{
                                Toast.makeText(ResetPasswordActivity.this, "Email is wrong", Toast.LENGTH_SHORT).show();
                                Log.d("Reset email", "Email is wrong");
                            }
                        }
                    });
        }
    }
}