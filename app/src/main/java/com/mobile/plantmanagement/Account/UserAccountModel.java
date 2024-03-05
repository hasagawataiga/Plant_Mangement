package com.mobile.plantmanagement.Account;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.plantmanagement.Calendar.CalendarEvent;
import com.mobile.plantmanagement.UserModel;

import java.util.Map;

public class UserAccountModel extends AndroidViewModel {

    private final String TAG = "USER_ACCOUNT";
    private String userUID = FirebaseAuth.getInstance().getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference userInfoRef;
    private MutableLiveData<UserAccount> userInfo = new MutableLiveData<>();

    public UserAccountModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<UserAccount> getUserInfo() {
        return userInfo;
    }

    public void retrieveInfo() {
        if (!isLoggedIn(userUID)) {
            return;
        }
        userInfoRef
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Retrieving Events");
                        if (dataSnapshot.exists()) {
                            UserAccount userAccount = dataSnapshot.getValue(UserAccount.class);
                            userInfo.setValue(userAccount);
                            Log.d(TAG, "Retrieve Events Successful");
                        } else {
                            userInfo.setValue(null);
                            Log.d(TAG, "Retrieve Events Failed");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        userInfo.setValue(null);
                        Log.d(TAG, "Error retrieving events", databaseError.toException());
                    }
                });
    }

    public void updateUserInfo(UserAccount userInfo) {
        if (!isLoggedIn(userUID)) {
            return;
        }
        userInfoRef.setValue(userInfo)
                .addOnSuccessListener(aVoid -> showToast("Events saved"))
                .addOnFailureListener(e -> handleDatabaseError(e, "Error saving events"));
    }

    private boolean isLoggedIn(String userUID) {
        if (userUID == null) {
            Log.d(TAG, "User is not found");
            return false;
        }
        userInfoRef = database.getReference("User").child(userUID).child("Info");
        Log.d(TAG, "User is logged");
        return true;
    }

    private void handleDatabaseError(Exception e, String message) {
        showToast("Database Error: " + e.getMessage());
        Log.e(TAG, message, e);
    }

    private void showToast(String message) {
        Toast.makeText(getApplication().getBaseContext(), message, Toast.LENGTH_SHORT).show();
    }
}
