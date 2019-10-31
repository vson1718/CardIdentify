package com.vson.identify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;

/**
 * @author vson
 */
public class FaceActivity extends AppCompatActivity {


    private FaceSurfaceView faceSurfaceView;

    private String[] permissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        // 动态权限申请
        if (PermissionsUtils.lacksPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
        faceSurfaceView = findViewById(R.id.faceSurfaceView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(Environment.getExternalStorageDirectory(), "face");
                //数据采样标
                File haar = Util.copyAssetsFile(FaceActivity.this, "haarcascade_frontalface_alt.xml", dir);
                //一定使用绝对路径
                FaceHelper.loadModel(haar.getAbsolutePath());
                FaceHelper.startTracking();
            }
        }).start();

        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceSurfaceView.switchCamera();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FaceHelper.destroy();
    }


}
