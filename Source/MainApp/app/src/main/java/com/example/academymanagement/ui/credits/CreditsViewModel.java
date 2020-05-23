package com.example.academymanagement.ui.credits;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreditsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreditsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is credits fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
