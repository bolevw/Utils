package com.boger.permissionutils;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubo on 2017/6/15.
 */

public class PermissionPresenter extends Fragment {
    public static final String TAG = "PermissionPresenter";
    private static final String KEY = "permissionList";
    private static final int REQUEST_CODE = 0x001;

    private PermissionListener permissionListener;
    private List<String> permissionList = new ArrayList<>();

    public static PermissionPresenter newInstance(List<String> list) {

        Bundle args = new Bundle();
        args.putStringArrayList(KEY, (ArrayList<String>) list);
        PermissionPresenter fragment = new PermissionPresenter();
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        permissionList = bundle.getStringArrayList(KEY);

        String[] a = new String[permissionList.size()];
        requestPermissions(permissionList.toArray(a), REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handlePermissionResult(requestCode, permissions, grantResults);
    }

    private void handlePermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            boolean allGet = true;
            List<String> deniedPermission = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allGet = false;
                    deniedPermission.add(permissions[i]);
                }
            }

            if (allGet) {
                permissionListener.onAllPermissionsGranted();
            } else {
                permissionListener.onPermissionsDenied(deniedPermission);
            }

        }
    }

    public void setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }

    public interface PermissionListener {
        void onAllPermissionsGranted();

        void onPermissionsDenied(List<String> deniedPermissions);
    }
}
