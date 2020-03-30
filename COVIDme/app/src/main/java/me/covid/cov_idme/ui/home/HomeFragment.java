package me.covid.cov_idme.ui.home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import me.covid.cov_idme.CovidMeApplication;
import me.covid.cov_idme.R;

public class HomeFragment extends Fragment {

    private static final int GREEN = Color.rgb(0, 128, 0);

    private static final int YELLOW = Color.YELLOW;

    private static final int RED = Color.RED;

    private static final int YELLOW_THRESHOLD = 25;

    private static final int RED_THRESHOLD = 90;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.homeText);
        final ImageView qrImageView = root.findViewById(R.id.qrImageView);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String text) {
                Integer score = CovidMeApplication.getRiskScore();

                int color = GREEN;
                if (score != null && score >= RED_THRESHOLD) {
                    color = RED;
                } else if (score != null && score >= YELLOW_THRESHOLD) {
                    color = YELLOW;
                }

                textView.setText(text);

                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);

                    for (int x = 0; x < bitmap.getWidth(); x++) {
                        for (int y = 0; y < bitmap.getHeight(); y++) {
                            if (Color.valueOf(Color.WHITE).equals(bitmap.getColor(x, y))) {
                                bitmap.setPixel(x, y, color);
                            }
                        }
                    }

                    qrImageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    // Ignored
                }
            }
        });
        return root;
    }
}
