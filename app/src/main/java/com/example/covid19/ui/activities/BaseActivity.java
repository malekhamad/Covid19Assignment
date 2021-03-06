package com.example.covid19.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid19.R;

public abstract class BaseActivity extends AppCompatActivity {

    private Activity mActivity ;
    ProgressDialog dialog ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = currentActivity() ;
    }



    public abstract Activity currentActivity();

    public void onFailure(){
        Toast.makeText(mActivity.getApplicationContext(), R.string.failure, Toast.LENGTH_SHORT).show();
        hideProgressDialog();
    };


    public void showProgressDialog() {
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Processing...");
        dialog.setCancelable(false);
        dialog.show();
    }

    public void hideProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
