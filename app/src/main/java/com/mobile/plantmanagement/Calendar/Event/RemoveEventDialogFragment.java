package com.mobile.plantmanagement.Calendar.Event;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mobile.plantmanagement.R;

public class RemoveEventDialogFragment extends DialogFragment {

    ImageView checkButton;
    ImageView cancelButton;

    private OnRemoveEventListener listener;

    public interface OnRemoveEventListener {
        void onRemoveConfirmed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRemoveEventListener) {
            listener = (OnRemoveEventListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnRemoveEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_remove_button_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkButton = view.findViewById(R.id.positiveButton);
        cancelButton = view.findViewById(R.id.negativeButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyRemoveConfirmed();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void notifyRemoveConfirmed() {
        if (listener != null) {
            listener.onRemoveConfirmed();
        }
    }

    public void setOnRemoveEventListener(OnRemoveEventListener listener) {
        this.listener = listener;
    }
}

