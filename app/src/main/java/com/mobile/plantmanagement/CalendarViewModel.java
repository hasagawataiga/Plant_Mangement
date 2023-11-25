package com.mobile.plantmanagement;

import static java.util.Objects.requireNonNull;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalendarViewModel extends AndroidViewModel {
    private final String TAG = "CALENDAR_VIEW_MODEL";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<Map<String, Object>> selectedDateEvents = new MutableLiveData<>();
    private MutableLiveData<Map<String, Object>> selectedDateNotes = new MutableLiveData<>();
//    private CollectionReference calendarCollection = db.collection("Calendar");
//    private CollectionReference notesCollection = db.collection("Notes");

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventsRef = database.getReference("Calendar");
    private DatabaseReference notesRef = database.getReference("Note");
    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Map<String, Object>> getSelectedDateEvents() {
        return selectedDateEvents;
    }
    public MutableLiveData<Map<String, Object>> getSelectedDateNotes(){
        return selectedDateNotes;
    }

    public void retrieveNotes(String date) {
        notesRef
                .child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, Object>> genericType = new GenericTypeIndicator<Map<String, Object>>() {};
                            selectedDateNotes.setValue(dataSnapshot.getValue(genericType));
                            Log.d(TAG, selectedDateNotes.toString());
                        } else {
                            selectedDateNotes.setValue(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        selectedDateNotes.setValue(null);
                        Log.d(TAG, "Error retrieving notes", databaseError.toException());
                    }
                });
    }

    public void retrieveEvents(String date) {
        eventsRef
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, Object>> genericType = new GenericTypeIndicator<Map<String, Object>>() {};
                            selectedDateEvents.setValue(dataSnapshot.getValue(genericType));
                            Log.d(TAG, selectedDateEvents.toString());
                        } else {
                            selectedDateEvents.setValue(null);
                        }
                        Log.d(TAG, "Retrieving Events Successful");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        selectedDateEvents.setValue(null);
                        Log.d(TAG, "Error retrieving events", databaseError.toException());
                    }
                });
    }

    public void updateNotes(String date, Map<String, Object> notes){
        notesRef
                .child(date)
                .setValue(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication().getBaseContext(), "Notes saved", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Notes saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication().getBaseContext(), "Error saving notes", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Error saving notes", e);
                    }
                });
    }

    public void updateEvents(String date, Map<String, Object> events) {
        eventsRef
                .child(date)
                .setValue(events)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication().getBaseContext(), "Events saved", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Events saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication().getBaseContext(), "Error saving events", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Error saving events", e);
                    }
                });
    }


//    public void retrieveNotes(String date){
//        notesCollection
//                .document(date)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            selectedDateNotes.setValue((Map<String, Object>) documentSnapshot.getData());
//                            Log.d(TAG, selectedDateNotes.toString());
//                        } else {
//                            selectedDateNotes.setValue(null);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        selectedDateNotes.setValue(null);
//                        Log.d(TAG, "Error retrieving notes", e);
//                    }
//                });
//    }
//
//    public void retrieveEvents(String date) {
//        calendarCollection
//                .document(date)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            selectedDateEvents.setValue((Map<String, Object>) documentSnapshot.getData());
//                            Log.d(TAG, selectedDateEvents.toString());
//                        } else {
//                            selectedDateEvents.setValue(null);
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        selectedDateEvents.setValue(null);
//                        Log.d(TAG, "Error retrieving events", e);
//                    }
//                });
//    }
//
//    public void updateNotes(String date, Map<String, Object> notes){
//        notesCollection
//                .document(date)
//                .set(notes)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplication().getBaseContext(), "Notes saved", Toast.LENGTH_SHORT).show();
//                        Log.d("CalendarViewModel", "Notes saved");
//                    }
//                });
//    }
//
//    public void updateEvents(String date, Map<String, Object> events) {
//        calendarCollection
//                .document(date)
//                .set(events)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(getApplication().getBaseContext(), "Event saved", Toast.LENGTH_SHORT).show();
//                        Log.d("CalendarViewModel", "Events saved");
//                    }
//                });
//    }
}
