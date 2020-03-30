package me.covid.cov_idme.ui.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.xml.transform.Result;

import me.covid.cov_idme.CovidMeApplication;
import me.covid.cov_idme.model.ResultSubmission;

public class ResultViewModel extends ViewModel {

    private boolean byDoctor;

    private boolean byTest;

    public ResultViewModel() {
    }

    /**
     * Sets a new diagnosis for this user
     *
     * @param byDoctor - whether the user was diagnosed by a physician
     * @param byTest - whether the user was diagnosed as a result of a positive test result for SARS-Cov-2
     */
    public void setResult(boolean byDoctor, boolean byTest) {
        this.byDoctor = byDoctor;
        this.byTest = byTest;
    }

    /**
     * Determines whether or not the user has been diagnosed with COVID-19
     *
     * @return true if and only if the user has been diagnosed with COVID-19
     */
    public boolean isInfected() {
        return byDoctor || byTest;
    }

    /**
     * Creates a new test result for submission
     *
     * @return a submission for the test results
     */
    public ResultSubmission createSubmission() {
        return new ResultSubmission(byDoctor, byTest);
    }

}