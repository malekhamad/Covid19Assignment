package com.example.covid19.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.covid19.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyPermissions {

    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    public static final int APP_PERMISSIONS_CODE = 1212;

    private static Activity mActivity;
    private static PermissionListener mPerpermissionListener;

    public static void showPermissionsDialogIfNeverAskAgainChecked(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(mActivity.getResources().getString(R.string.permission_denied_title));
        builder.setMessage(mActivity.getResources().getString(R.string.permission_denied_msg));
        builder.setCancelable(true);
        // add a button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Check if version is marshmallow and above.
     * Used in deciding to ask runtime permission
     *
     * @param activity           referance of activity
     * @param permissionListener is describe permission status
     */
    public static boolean checkAndRequestPermission(@NonNull Activity activity, @NonNull PermissionListener permissionListener) {
        mActivity = activity;
        mPerpermissionListener = permissionListener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            List<String> listPermissionsNeeded = getRequiredPermissions();
            List<String> listPermissionsAssign = new ArrayList<>();

            for (String per : listPermissionsNeeded) {
                if (ContextCompat.checkSelfPermission(activity.getApplicationContext(), per) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsAssign.add(per);
                }
            }
            if (!listPermissionsAssign.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsAssign.toArray(new String[listPermissionsAssign.size()]), APP_PERMISSIONS_CODE);
                return false;
            }
        }
        return true;
    }

    public static void checkResult(@NonNull int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case APP_PERMISSIONS_CODE: {
                List<String> listPermissionsNeeded = getRequiredPermissions();
                Map<String, Integer> perms = new HashMap<>();

                for (String permission : listPermissionsNeeded) {
                    perms.put(permission, PackageManager.PERMISSION_GRANTED);
                }
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    boolean isAllGranted = true;
                    for (String permission : listPermissionsNeeded) {
                        if (perms.get(permission) == PackageManager.PERMISSION_DENIED) {
                            isAllGranted = false;
                            break;
                        }
                    }
                    if (isAllGranted) {
                        mPerpermissionListener.onPermissionGranted(getRequiredPermissions());
                    } else {
                        boolean shouldRequest = false;
                        for (String permission : listPermissionsNeeded) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                                shouldRequest = true;
                                break;
                            }
                        }
                        if (shouldRequest) {
                            ifCancelledAndCanRequest(mActivity);
                        } else {
                            //permission is denied (and never ask again is  checked)
                            //shouldShowRequestPermissionRationale will return false
                            ifCancelledAndCannotRequest(mActivity);
                        }
                    }
                }
            }
        }
    }

    /**
     * permission cancel dialog
     *
     * @param activity
     */
    private static void ifCancelledAndCanRequest(final Activity activity) {
        showDialogOK(activity, activity.getResources().getString(R.string.grant_permission), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        checkAndRequestPermission(activity, mPerpermissionListener);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mPerpermissionListener.onPermissionDenied(getRequiredPermissions());
                        // proceed with logic by disabling the related features or quit the app.
                        break;
                }
            }
        });
    }

    /**
     * forcefully stoped all permission dialog
     *
     * @param activity
     */
    private static void ifCancelledAndCannotRequest(final Activity activity) {
        showDialogOK(activity, "You have forcefully denied some of the required permissions \n" + "for this action. Please go to permissions and allow them.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mPerpermissionListener.onPermissionDenied(getRequiredPermissions());
                        // proceed with logic by disabling the related features or quit the app.
                        break;
                }
            }
        });
    }

    private static void showDialogOK(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", okListener).create().show();
    }

    public static List<String> getRequiredPermissions(){
        List<String> permissions = new ArrayList<>();
        permissions.add(FINE_LOCATION);
        permissions.add(COARSE_LOCATION);

        return permissions ;
    }

}