package com.vson.identify;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;


/**
 * @author Vson
 */

public class PermissionsUtils {

    /**
     * 判断权限集合
     *
     * @param permissions 权限集合
     * @return true 没有权限|false 有权限
     */
    public static boolean lacksPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //低于Android6.0 不能动态分配权限
            return false;
        }
        for (String permission : permissions) {
            if (lacksPermission(context, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     *
     * @param permission 权限
     * @return boolean
     */
    private static boolean lacksPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED;
    }


}
