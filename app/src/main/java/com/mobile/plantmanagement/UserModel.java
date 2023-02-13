package com.mobile.plantmanagement;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable {
    private String personName;
    private String personGivenName;
    private String personFamilyName;
    private String personEmail;
    private String personId;
    private Uri personPhoto;
    public UserModel(String personName, String personGivenName, String personFamilyName, String personEmail, String personId, Uri personPhoto) {
        this.personName = personName;
        this.personGivenName = personGivenName;
        this.personFamilyName = personFamilyName;
        this.personEmail = personEmail;
        this.personId = personId;
        this.personPhoto = personPhoto;
    }

    public UserModel() {
        personName = "GUEST";
    }

    protected UserModel(Parcel in) {
        personName = in.readString();
        personGivenName = in.readString();
        personFamilyName = in.readString();
        personEmail = in.readString();
        personId = in.readString();
        personPhoto = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public String toString() {
        return "UserModel{" +
                "personName='" + personName + '\'' +
                ", personGivenName='" + personGivenName + '\'' +
                ", personFamilyName='" + personFamilyName + '\'' +
                ", personEmail='" + personEmail + '\'' +
                ", personId='" + personId + '\'' +
                ", personPhoto=" + personPhoto +
                '}';
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonGivenName() {
        return personGivenName;
    }

    public void setPersonGivenName(String personGivenName) {
        this.personGivenName = personGivenName;
    }

    public String getPersonFamilyName() {
        return personFamilyName;
    }

    public void setPersonFamilyName(String personFamilyName) {
        this.personFamilyName = personFamilyName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Uri getPersonPhoto() {
        return personPhoto;
    }

    public void setPersonPhoto(Uri personPhoto) {
        this.personPhoto = personPhoto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(personName);
        parcel.writeString(personGivenName);
        parcel.writeString(personFamilyName);
        parcel.writeString(personEmail);
        parcel.writeString(personId);
        parcel.writeParcelable(personPhoto, i);
    }
}
