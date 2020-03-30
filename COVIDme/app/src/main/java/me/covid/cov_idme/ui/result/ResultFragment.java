package me.covid.cov_idme.ui.result;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import me.covid.cov_idme.CovidMeApplication;
import me.covid.cov_idme.R;
import me.covid.cov_idme.model.ResultSubmission;

public class ResultFragment extends Fragment {


    private ResultViewModel resultViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        resultViewModel =
                ViewModelProviders.of(this).get(ResultViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_result, container, false);

        final Switch byDoctor = root.findViewById(R.id.doctor);
        final Switch byTest = root.findViewById(R.id.testResult);

        Button submitButton = root.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultViewModel.setResult(byDoctor.isChecked(), byTest.isChecked());
                if (resultViewModel.isInfected()) {
                    ((CovidMeApplication) getActivity().getApplication()).updateRiskScore(100);

                    ResultSubmission submission = resultViewModel.createSubmission();
                    Log.d("submission", String.valueOf(submission));

                    Snackbar.make(root, "Thank you for helping us track the infection!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_home);
                }
            }
        });

        return root;
    }
}
