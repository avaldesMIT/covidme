package me.covid.cov_idme.ui.home;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;

import me.covid.cov_idme.CovidMeApplication;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> uuidText;

    public HomeViewModel() {
        uuidText = new MutableLiveData<>();
        uuidText.setValue(CovidMeApplication.getUniqueID());
    }

    public LiveData<String> getText() {
        return uuidText;
    }
}