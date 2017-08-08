package com.boger.permissionutils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/6/15.
 */

public class PermissionUtils {

    public static void requestPermissions(Activity context, String[] string, final GetPermissionListener getPermissionListener) {
        List<String> deniedPermissionList = new ArrayList<>();
        if (string.length == 0) {
            return;
        }
        for (int i = 0; i < string.length; i++) {
            if (!checkSelfPermission(context, string[i])) {
                deniedPermissionList.add(string[i]);
            }
        }
        if (!deniedPermissionList.isEmpty()) {
            PermissionPresenter presenter = PermissionPresenter.newInstance(deniedPermissionList);
            presenter.setPermissionListener(new PermissionPresenter.PermissionListener() {
                @Override
                public void onAllPermissionsGranted() {
                    getPermissionListener.onAllPermissionsGranted();
                }

                @Override
                public void onPermissionsDenied(List<String> deniedPermissions) {
                    getPermissionListener.onPermissionsDenied(deniedPermissions);
                }
            });
            context.getFragmentManager().beginTransaction().add(presenter, PermissionPresenter.TAG).commitAllowingStateLoss();
        } else {
            getPermissionListener.onAllPermissionsGranted();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkSelfPermission(Context context, String string) {
        return context.checkSelfPermission(string) == context.getPackageManager().PERMISSION_GRANTED;
    }


    public static void showWarningDialog(Activity context, String string) {

    }

    public interface GetPermissionListener {
        void onAllPermissionsGranted();

        void onPermissionsDenied(List<String> deniedPermissions);
    }
}


