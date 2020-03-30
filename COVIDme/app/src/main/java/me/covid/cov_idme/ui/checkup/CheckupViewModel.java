package me.covid.cov_idme.ui.checkup;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CheckupViewModel extends ViewModel {

    public CheckupViewModel() {

    }

    public Integer calculate(boolean fever,
                          boolean cough,
                          boolean breath,
                          boolean fatigue,
                          boolean bodyAche,
                          boolean headache,
                          boolean runnyNose,
                          boolean soreThroat,
                          boolean diarrhea,
                          boolean lossOfSmell) {

        //TODO: Replace with remote call to calculate score
        Integer score;
        if (fever && breath) {
            score = 95;
        } else if (fever && cough) {
            score = 90;
        } else if (fever && headache) {
            score = 85;
        } else if (fever && (fatigue || bodyAche || soreThroat)) {
            score = 85;
        } else if ((cough && breath && (fatigue || bodyAche || headache || soreThroat))) {
            score = 80;
        } else if (fever && (runnyNose || diarrhea)) {
            score = 50;
        } else if (lossOfSmell) {
            score = 30;
        } else {
            score = 20;
        }
        return score;
    }
}