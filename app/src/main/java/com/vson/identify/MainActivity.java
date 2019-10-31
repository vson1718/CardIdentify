package com.vson.identify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

/**
 * @author vson
 */
public class MainActivity extends AppCompatActivity {

    private DisplaySurfaceView surfaceView;
    private SeekBar seekBar;

    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        seekBar = findViewById(R.id.seekBar);
        // 动态权限申请
        if (PermissionsUtils.lacksPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("TAG", "onProgressChanged: " + progress);
                surfaceView.setZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.startRecord();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surfaceView.stopRecord();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.releaseCamera();
    }
}
