package com.mobile.plantmanagement.Calendar;

import static java.util.Objects.requireNonNull;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
//    private CollectionReference calendarCollection = db.collection("Calendar");
//    private CollectionReference notesCollection = db.collection("Notes");

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference eventsRef = database.getReference("Calendar");
    public CalendarEventModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Map<String, Object>> getSelectedDateEvents() {
        return selectedDateEvents;
    }

    public void retrieveEvents(String date) {
        eventsRef
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Retrieving Events");
                        if (dataSnapshot.exists()) {
                            GenericTypeIndicator<Map<String, Object>> genericType = new GenericTypeIndicator<Map<String, Object>>() {};
                            selectedDateEvents.setValue(dataSnapshot.getValue(genericType));
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
        eventsRef
                .child(date)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Date node does not exist, create a new one
                    eventsRef
                            .child(date)
                            .setValue(events)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplication().getBaseContext(), "Events saved", Toast.LENGTH_SHORT).show();
                                    Log.d("CalendarViewModel", "Events saved for date: " + date);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplication().getBaseContext(), "Error saving events", Toast.LENGTH_SHORT).show();
                                    Log.e("CalendarViewModel", "Error saving events for date: " + date, e);
                                }
                            });
                } else {
                    // Date node already exists, update the events under this date
                    eventsRef
                            .child(date)
                            .updateChildren(events)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplication().getBaseContext(), "Events updated", Toast.LENGTH_SHORT).show();
                                    Log.d("CalendarViewModel", "Events updated for date: " + date);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplication().getBaseContext(), "Error updating events", Toast.LENGTH_SHORT).show();
                                    Log.e("CalendarViewModel", "Error updating events for date: " + date, e);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors
                Log.e("CalendarViewModel", "Database Error: " + databaseError.getMessage());
            }
        });
    }

    public void deleteEvent(String date, CalendarEvent event) {
        eventsRef
                .child(date)
                .child(event.getTitle())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplication().getBaseContext(), "Event removed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Event removed: " + event.getTitle() + " from date: " + date);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplication().getBaseContext(), "Error removing event", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error removing event: " + event.getTitle() + " from date: " + date, e);
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
