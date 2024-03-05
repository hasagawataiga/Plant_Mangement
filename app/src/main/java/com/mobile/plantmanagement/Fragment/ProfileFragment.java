package com.mobile.plantmanagement.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.plantmanagement.Account.UserAccount;
import com.mobile.plantmanagement.Account.UserAccountModel;
import com.mobile.plantmanagement.Calendar.CalendarEventModel;
import com.mobile.plantmanagement.R;
import com.mobile.plantmanagement.databinding.SettingProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG = "SETTING_PROFILE";
    private UserAccountModel userAccountModel;
    private UserAccount userInfo;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private SettingProfileBinding settingProfileBinding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        settingProfileBinding = SettingProfileBinding.inflate(inflater, container, false);
        View view = settingProfileBinding.getRoot();

        // Loading Spinner at the beginning
        showLoadingSpinner();

        userInfo = new UserAccount();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        // User Account liveDate declaration
        ViewModelProvider.Factory factory = (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());
        userAccountModel = new ViewModelProvider(this, factory).get(UserAccountModel.class);
        userAccountModel.getUserInfo().observe(getViewLifecycleOwner(), events -> {
            userInfo = userAccountModel.getUserInfo().getValue();
            updateUserInfoView();
        });
        hideLoadingSpinner();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userAccountModel.retrieveInfo();

        settingProfileBinding.ivName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etName.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });
        settingProfileBinding.ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etEmail.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });
        settingProfileBinding.ivStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etStreet.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });
        settingProfileBinding.ivPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etPhoneNumber.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });
        settingProfileBinding.ivApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etApartment.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });
        settingProfileBinding.ivHouseNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingProfileBinding.etHouseNumber.setEnabled(true);
                Toast.makeText(getContext(), "Edit text is enabled", Toast.LENGTH_SHORT).show();
                settingProfileBinding.saveChangesButton.setEnabled(true);
            }
        });

        settingProfileBinding.saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesUserInfo();
                disabledEditText();
                disabledSaveChangesButton();
            }
        });

        settingProfileBinding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateUserInfoView() {
        if (userInfo == null) {
            userInfo = new UserAccount();
        }
        try{
            Picasso.get()
                    .load(getUserIcon(firebaseUser))
                    .into(settingProfileBinding.profileImage);
            Log.d(TAG, "Get user icon successfully.");
        } catch (Exception e){
            settingProfileBinding.profileImage.setImageResource(R.drawable.ic_round_tag_faces_24);
            Log.d(TAG, "Get user icon failed.");
        }
        settingProfileBinding.etName.setText(userInfo.getName());
        settingProfileBinding.etEmail.setText(userInfo.getEmail());
        settingProfileBinding.etPhoneNumber.setText(userInfo.getPhoneNumber());
        settingProfileBinding.etStreet.setText(userInfo.getStreet());
        settingProfileBinding.etApartment.setText(userInfo.getApartment());
        settingProfileBinding.etHouseNumber.setText(userInfo.getHouseNumber());
        hideLoadingSpinner();
    }

    private void saveChangesUserInfo() {
        showLoadingSpinner();
        String name = settingProfileBinding.etName.getText().toString();
        String email = settingProfileBinding.etEmail.getText().toString();
        String phoneNumber = settingProfileBinding.etPhoneNumber.getText().toString();
        String street = settingProfileBinding.etStreet.getText().toString();
        String apartment = settingProfileBinding.etApartment.getText().toString();
        String houseNumber = settingProfileBinding.etHouseNumber.getText().toString();
        userInfo = new UserAccount(name, email, phoneNumber, street, apartment, houseNumber);
        userAccountModel.updateUserInfo(userInfo);
        hideLoadingSpinner();
    }

    private void disabledEditText() {
        settingProfileBinding.etName.setEnabled(false);
        settingProfileBinding.etEmail.setEnabled(false);
        settingProfileBinding.etPhoneNumber.setEnabled(false);
        settingProfileBinding.etStreet.setEnabled(false);
        settingProfileBinding.etApartment.setEnabled(false);
        settingProfileBinding.etHouseNumber.setEnabled(false);
    }

    private void disabledSaveChangesButton() {
        settingProfileBinding.saveChangesButton.setEnabled(false);
    }

    private String getUserIcon(FirebaseUser user) {
        if (user != null) {
            return Objects.requireNonNull(user.getPhotoUrl()).toString();
        }
        return null;
    }

    private void showLoadingSpinner() {
        settingProfileBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoadingSpinner() {
        settingProfileBinding.progressBar.setVisibility(View.GONE);
    }
}