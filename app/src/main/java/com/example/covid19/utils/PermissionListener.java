package com.example.covid19.utils;

import java.util.List;

public interface PermissionListener {

    void onPermissionGranted(List<String> mCustomPermission);

    void onPermissionDenied(List<String> mCustomPermission);
}