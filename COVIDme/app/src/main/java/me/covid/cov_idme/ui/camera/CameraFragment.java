package me.covid.cov_idme.ui.camera;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.covid.cov_idme.MainActivity;
import me.covid.cov_idme.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CameraFragment extends Fragment {

    private CameraViewModel cameraViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_camera, container, false);
        final ZXingScannerView scannerView = root.findViewById(R.id.zxscan);

        Dexter.withActivity(CameraFragment.this.getActivity()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                    @Override
                    public void handleResult(Result rawResult) {
                        Log.i("test", rawResult.getText());
                        Snackbar.make(root, "Contact tracked. Thank you!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        scannerView.startCamera();
                    }
                });
                scannerView.startCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(CameraFragment.this.getActivity(),
                        "COV-ID.me needs to be able to access your camera to scan QR codes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();

        return root;
    }
}
