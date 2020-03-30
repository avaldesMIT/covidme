package me.covid.cov_idme.ui.checkup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import me.covid.cov_idme.CovidMeApplication;
import me.covid.cov_idme.MainActivity;
import me.covid.cov_idme.R;

public class CheckupFragment extends Fragment {

    private CheckupViewModel checkupViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkupViewModel =
                ViewModelProviders.of(this).get(CheckupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_checkup, container, false);

        final Switch fever = root.findViewById(R.id.fever);
        final Switch cough = root.findViewById(R.id.cough);
        final Switch breath = root.findViewById(R.id.breath);
        final Switch fatigue = root.findViewById(R.id.fatigue);
        final Switch bodyAche = root.findViewById(R.id.body_ache);
        final Switch headAche = root.findViewById(R.id.headache);
        final Switch runnyNose = root.findViewById(R.id.runny_nose);
        final Switch soreThroat = root.findViewById(R.id.sore_throat);
        final Switch diarrhea = root.findViewById(R.id.diarrhea);
        final Switch lossOfSmell = root.findViewById(R.id.loss_of_smell);

        Button calculateButton = root.findViewById(R.id.button_calculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer score = checkupViewModel.calculate(
                        fever.isChecked(),
                        cough.isChecked(),
                        breath.isChecked(),
                        fatigue.isChecked(),
                        bodyAche.isChecked(),
                        headAche.isChecked(),
                        runnyNose.isChecked(),
                        soreThroat.isChecked(),
                        diarrhea.isChecked(),
                        lossOfSmell.isChecked());

                ((CovidMeApplication) getActivity().getApplication()).updateRiskScore(score);

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_home);
            }
        });

        return root;
    }
}
