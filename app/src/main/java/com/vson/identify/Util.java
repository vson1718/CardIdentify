package com.vson.identify;

import android.app.Activity;
import android.content.Context;
import android.view.Surface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author vson
 * @date 2019-10-31.
 * Company:上海动博士企业发展有限公司
 * E-mail :13013099535@163.com
 * 项目描述:
 */
public class Util {

    public static File copyAssetsFile(Context context, String name, File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name);
        if (!file.exists()) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = context.getAssets().open(name);
                fos = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[2048];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    /**
     * 获取显示方向
     *
     * @return degree 调整方法
     */
    public static int getDegree(Context context) {
        int rotating = ((Activity) context).getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
            default:
                break;

        }
        return degree;
    }
}
