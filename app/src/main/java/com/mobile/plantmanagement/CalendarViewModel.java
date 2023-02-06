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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private CollectionReference calendarCollection = db.collection("Calendar");
    private CollectionReference notesCollection = db.collection("Notes");
    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Map<String, Object>> getSelectedDateEvents() {
        return selectedDateEvents;
    }
    public MutableLiveData<Map<String, Object>> getSelectedDateNotes(){
        return selectedDateNotes;
    }

    public void retrieveNotes(String date){
        notesCollection
                .document(date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            selectedDateNotes.setValue((Map<String, Object>) documentSnapshot.getData());
                            Log.d(TAG, selectedDateNotes.toString());
                        } else {
                            selectedDateNotes.setValue(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        selectedDateNotes.setValue(null);
                        Log.d(TAG, "Error retrieving notes", e);
                    }
                });
    }

    public void retrieveEvents(String date) {
        calendarCollection
                .document(date)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            selectedDateEvents.setValue((Map<String, Object>) documentSnapshot.getData());
                            Log.d(TAG, selectedDateEvents.toString());
                        } else {
                            selectedDateEvents.setValue(null);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        selectedDateEvents.setValue(null);
                        Log.d(TAG, "Error retrieving events", e);
                    }
                });
    }

    public void updateNotes(String date, Map<String, Object> notes){
        notesCollection
                .document(date)
                .set(notes)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication().getBaseContext(), "Notes saved", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Notes saved");
                    }
                });
    }

    public void updateEvents(String date, Map<String, Object> events) {
        calendarCollection
                .document(date)
                .set(events)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication().getBaseContext(), "Event saved", Toast.LENGTH_SHORT).show();
                        Log.d("CalendarViewModel", "Events saved");
                    }
                });
    }
}
