package com.mobile.plantmanagement.Calendar;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class CalendarEventModel extends AndroidViewModel {
    private final String TAG = "CALENDAR_VIEW_MODEL";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Map<String, Object>> selectedDateEvents = new MutableLiveData<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String userUID = FirebaseAuth.getInstance().getUid();
    private DatabaseReference eventsRef;
    public CalendarEventModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Map<String, Object>> getSelectedDateEvents() {
        return selectedDateEvents;
    }

    public void retrieveEvents(String date) {
        if (!isLoggedIn(userUID)) {
            return;
        }
        eventsRef
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Retrieving Events");
                        if (dataSnapshot.exists()) {
                            selectedDateEvents.setValue(dataSnapshot.getValue(new GenericTypeIndicator<Map<String, Object>>() {}));
                            Log.d(TAG, "Retrieve Events Successful");
                        } else {
                            selectedDateEvents.setValue(null);
                            Log.d(TAG, "Retrieve Events Failed");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        selectedDateEvents.setValue(null);
                        Log.d(TAG, "Error retrieving events", databaseError.toException());
                    }
                });
    }

    public void updateEvents(String date, Map<String, Object> events) {
        if (!isLoggedIn(userUID)) {
            return;
        }
        eventsRef.child(date).updateChildren(events)
                .addOnSuccessListener(aVoid -> showToast("Events saved"))
                .addOnFailureListener(e -> handleDatabaseError(e, "Error saving events"));
    }

    public void deleteEvent(String date, CalendarEvent event) {
        if (!isLoggedIn(userUID)) {
            return;
        }
        eventsRef.child(date).child(event.getTitle()).removeValue()
                .addOnSuccessListener(aVoid -> showToast("Event removed: " + event.getTitle() + " from date: " + date))
                .addOnFailureListener(e -> handleDatabaseError(e, "Error removing event: " + event.getTitle() + " from date: " + date));
    }

    private boolean isLoggedIn(String userUID) {
        if (userUID == null) {
            return false;
        }
        eventsRef = database.getReference("User").child(userUID).child("Event");
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
