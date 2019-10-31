package com.vson.identify;

import android.view.Surface;

/**
 *
 * @author vson
 */

public class FaceHelper {

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    /**
     * 这里不做成类了 static方便调用了
     */
    public static native void loadModel(String detectModel);


    public static native void startTracking();


    public static native void stopTracking();

    public static native void setSurface(Surface surface, int w, int h);


    public static native void detectorFace(byte[] data, int w, int h, int rotation, int cameraId);

    public static native void destroy();

}
