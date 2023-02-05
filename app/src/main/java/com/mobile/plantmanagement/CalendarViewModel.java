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
        db.collection("Notes")
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
        db.collection("Calendar")
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
        db.collection("Notes")
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
        db.collection("Calendar")
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

//    public List<CalendarEvent> getEventsList(String date){
//        db.collection("Calendar").document(date).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        Map<String, Object> data = documentSnapshot.getData();
//                        if(documentSnapshot.exists()){
//                            for(Map.Entry<String, Object> entry : data.entrySet()){
//                                CalendarEvent event = new CalendarEvent(entry.getKey(), (String) entry.getValue());
//                                eventList.add(event);
//                            }
//                            Log.d(TAG, "CalendarEvent list: " + eventList.toString());
//                        }else{
//                            Log.d(TAG, "Document is not existed.");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error getting document: ", e);
//                    }
//                });
//        return eventList;
//    }

//    public LiveData<List<CalendarEvent>> getEvents(String date){
//        db.collection("Calendar").document(date).get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if(documentSnapshot.exists()){
//                    List<CalendarEvent> eventList = new ArrayList<>();
//                    eventList = (List<CalendarEvent>) documentSnapshot.toObject(CalendarEvent.class);
//                    eventsLiveData.setValue(eventList);
//                }else{
//                    Log.d(TAG, "Document is not existed.");
//                }
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error getting document: ", e);
//                    }
//                });
//        return eventsLiveData;
//    }
}
