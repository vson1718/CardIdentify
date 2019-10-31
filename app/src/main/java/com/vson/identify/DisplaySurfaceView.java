package com.vson.identify;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

/**
 * @author vson  拍照 |视频
 */
public class DisplaySurfaceView extends SurfaceView
        implements SurfaceHolder.Callback, Camera.PreviewCallback, Camera.AutoFocusCallback {

    private final String TAG = "DisplaySurfaceView";

    private Camera camera;
    private boolean isCapturing = true;


    public DisplaySurfaceView(Context context) {
        this(context, null);
    }

    public DisplaySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DisplaySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            if (isCapturing) {
                camera.stopPreview();
                isCapturing = false;
            }
            camera.setPreviewCallback(null);
            camera.release();
        }

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        //获取相机的每一帧

    }

    /**
     * 初始化 camera
     */
    public void initCamera() {
        if (camera == null) {
            camera = Camera.open(CAMERA_FACING_BACK);
        }
    }

    /**
     * 开始显示camera
     */
    public void startPreview() {
        initCamera();
        if (camera != null) {
            try {
                camera.setPreviewDisplay(getHolder());
                camera.setDisplayOrientation(Util.getDegree(getContext()));
                if (isCapturing) {
                    camera.startPreview();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "startPreview: Unable to open camera.");
            }
        }
    }

    /**
     * 设置焦距
     *
     * @param zoom 焦距
     */
    public void setZoom(int zoom) {
        if (camera != null
                && zoom < 100
                && camera.getParameters().isZoomSupported()) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setZoom(zoom);
            camera.setParameters(parameters);
        }

    }


    /**
     * 释放camera资源
     */
    public void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        focusOnTouch((int) event.getX(), (int) event.getY());
        return false;
    }


    private void focusOnTouch(int x, int y) {
        Rect rect = new Rect(x - 100, y - 100, x + 100, y + 100);
        int left = rect.left * 2000 / getWidth() - 1000;
        int top = rect.top * 2000 / getHeight() - 1000;
        int right = rect.right * 2000 / getWidth() - 1000;
        int bottom = rect.bottom * 2000 / getHeight() - 1000;
        // 如果超出了(-1000,1000)到(1000, 1000)的范围，则会导致相机崩溃
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        focusOnRect(new Rect(left, top, right, bottom));
    }


    protected void focusOnRect(Rect rect) {
        if (camera != null) {
            // 先获取当前相机的参数配置对象
            Camera.Parameters parameters = camera.getParameters();
            // 设置聚焦模式
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            Log.d(TAG, "parameters.getMaxNumFocusAreas() : " + parameters.getMaxNumFocusAreas());
            if (parameters.getMaxNumFocusAreas() > 0) {
                //前置摄像头都是0，后置摄像头都是1
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(rect, 1000));
                parameters.setFocusAreas(focusAreas);
                // 先要取消掉进程中所有的聚焦功能
                camera.cancelAutoFocus();
                // 一定要记得把相应参数设置给相机
                camera.setParameters(parameters);
                camera.autoFocus(this);
            }
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        Log.d(TAG, "onAutoFocus: " + success);
    }


    private MediaRecorder mediaRecorder;

    /**
     * 开始录制
     */
    public void startRecord() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        //设置格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置保存路径

        String path = Environment.getExternalStorageDirectory() + "/ShortVideo/" + System.currentTimeMillis() + ".mp4";

        if (new File(path).exists()) {
            Log.d(TAG, "startRecord: 存在删除");
            new File(path).delete();
        }
        Log.d(TAG, "startRecord: " + path);
        mediaRecorder.setOutputFile(path);
        //mediaRecorder和surFace绑定
        mediaRecorder.setPreviewDisplay(getHolder().getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                mediaRecorder = null;
                mediaRecorder = new MediaRecorder();
            }
            mediaRecorder.release();
            mediaRecorder = null;
            Log.d(TAG, "startRecord: 停止录制");
        }
    }


}
