package com.example.billspliter.ui.archieve;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchieveViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ArchieveViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is archieve fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}