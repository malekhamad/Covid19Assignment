package com.example.covid19.ui.activities;


import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.covid19.enumertion.CasesCount;
import com.example.covid19.R;
import com.example.covid19.databinding.ActivityMainBinding;
import com.example.covid19.databinding.FragmentCountryDialogBinding;
import com.example.covid19.model.country.CountryData;
import com.example.covid19.model.tracking.CountryDetail;
import com.example.covid19.model.tracking.CountryStatus;
import com.example.covid19.model.tracking.CountryTracking;
import com.example.covid19.model.tracking.CountryRootData;
import com.example.covid19.ui.adapters.RecyclerCountryStatusAdapter;
import com.example.covid19.utils.EasyPermissions;
import com.example.covid19.utils.PermissionListener;
import com.example.covid19.view_model.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    // region Variables
    private ActivityMainBinding mBinding;
    private MainViewModel mMainViewModel;

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;

    // endregion

    // region Listeners
    private Observer<CountryRootData> trackingAllDataObserver = new Observer<CountryRootData>() {
        @Override
        public void onChanged(CountryRootData countryRootData) {

            if(countryRootData.isHasError()){
                onFailure();
                return;
            }

            for (int i = 0 ; i< countryRootData.getCountryStatusList().size() ; i++) {

                // get LatLng from countryData by passing country name
                CountryStatus mCountryStatus = countryRootData.getCountryStatusList().get(i) ;
                if (mMainViewModel.getCountryData().get(mCountryStatus.getCountryName()) != null) {
                    double latitude = mMainViewModel.getCountryData().get(mCountryStatus.getCountryName()).getLatlng().get(0);
                    double longitude = mMainViewModel.getCountryData().get(mCountryStatus.getCountryName()).getLatlng().get(1);

                    chooseMarkerBasedOnSorting(i, latitude, longitude);
                }
            }

            String casesTitle = getString(R.string.cases);
            String deathsTitle = getString(R.string.deaths);
            String confirmedTitle = getString(R.string.confirmed);
            String recoveredTitle = getString(R.string.recovered);

            mBinding.textViewTodayCases.setText(casesTitle + " " + countryRootData.getWorldCases());
            mBinding.textViewTodayDeaths.setText(deathsTitle + " " + countryRootData.getWorldDeath());
            mBinding.textViewTodayConfirmed.setText(confirmedTitle + " " + countryRootData.getWorldConfirmed());
            mBinding.textViewTodayRecovered.setText(recoveredTitle + " " + countryRootData.getWorldRecovered());

            showWorldStatus();

            hideProgressDialog();
        }
    };


    private Observer<String> countryNameObserver = new Observer<String>() {
        @Override
        public void onChanged(String countryName) {
            if(countryName == null){
              onFailure();
              return;
            }
            mMainViewModel.getTrackingDataByCountry(countryName, mMainViewModel.getSelectedStartDateWithFormat()
                    , mMainViewModel.getSelectedEndDateWithFormat()).observe(MainActivity.this, showBottomSheetObserver);

        }
    };

    private Observer<Boolean> showBottomSheetObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean showDialog) {
            if(showDialog){
                hideProgressDialog();
                hideWorldStatus();
                showBottomSheetDialog();
            }else {
                onFailure();
            }

        }
    };

    private OnMapReadyCallback lOnMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mGoogleMap = googleMap;

            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            MainActivity.this, R.raw.map_style));

            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(48.864716
                    ,2.349014) , 6.0f) );


            googleMap.setOnMapClickListener(onMapClickListener);

            googleMap.setOnMarkerClickListener(onMarkerClickListener);
        }
    };

    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            // check if the user fill start date and end date

            if (!mMainViewModel.isDateFieldFilled()) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.required_date_field_msg), Toast.LENGTH_SHORT).show();
                return;
            }

            getSpecificInformationWithAnimate(latLng);

        }
    };

    private GoogleMap.OnMarkerClickListener onMarkerClickListener = marker -> {
        getSpecificInformationWithAnimate(marker.getPosition());
        return true;
    };

    private PermissionListener lPermissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted(List<String> mCustomPermission) {
            init();
        }

        @Override
        public void onPermissionDenied(List<String> mCustomPermission) {
            EasyPermissions.showPermissionsDialogIfNeverAskAgainChecked(MainActivity.this);
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetStartListener = (datePicker, i, i1, i2) -> {
        try {

            mMainViewModel.setSelectedStartCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            mMainViewModel.setSelectedStartCalendarShown(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            String startDate = mMainViewModel.getSelectedStartDateWithFormat();

            mBinding.editDateFrom.setText("From : " + startDate);
            mBinding.editDateTo.setEnabled(true);

            mBinding.editDateTo.performClick();

        } catch (Exception e) {
            Log.i(TAG, "onError: "+e.getMessage());
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetEndListener = (datePicker, i, i1, i2) -> {
        try {

            // remove all markers
            mGoogleMap.clear();

            showProgressDialog();

            mMainViewModel.setSelectedEndCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            String endDate = mMainViewModel.getSelectedEndDateWithFormat();

            mBinding.editDateTo.setText("To : " + endDate);/*
            mMainViewModel.getTrackingData(mMainViewModel.getSelectedStartDateWithFormat(), mMainViewModel.getSelectedEndDateWithFormat())
                    .observe(MainActivity.this, mTrackingDataObserver);*/

            mMainViewModel.getTrackingAllData(mMainViewModel.getSelectedStartDateWithFormat(), mMainViewModel.getSelectedEndDateWithFormat())
                    .observe(MainActivity.this, trackingAllDataObserver);


        } catch (Exception e) {
            Log.i(TAG, "onError: "+e.getMessage());
        }
    };

    private View.OnClickListener onClickDateFrom = view -> {

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                dateSetStartListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        datePickerDialog.show();
    };

    private View.OnClickListener onClickDateTo = view -> {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                dateSetEndListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


        datePickerDialog.getDatePicker().setMinDate(mMainViewModel.getSelectedStartCalendarShown());
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();

    };

    private View.OnClickListener nextBtnClickListener = view -> {

        CountryDetail mTracking = mMainViewModel.getCountryTracking().getCountryDetailList().get(0);

        CountryData mCountryData = mMainViewModel.getCountryData().get(mTracking.getCountryName());

        Intent intent = new Intent(MainActivity.this,NewsActivity.class);
        intent.putExtra(NewsActivity.COUNTRY_DATA_KEY,mCountryData);
        startActivity(intent);
    };

    // endregion

    // region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);


        if (EasyPermissions.checkAndRequestPermission(this, lPermissionListener)) {
            init();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.checkResult(requestCode, permissions, grantResults);

    }

    @Override
    public Activity currentActivity() {
        return this;
    }


    // endregion

    // region Methods
    private void init() {

        try {

            mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mSupportMapFragment.getMapAsync(lOnMapReadyCallback);

            mBinding.editDateFrom.setOnClickListener(onClickDateFrom);
            mBinding.editDateTo.setOnClickListener(onClickDateTo);

            Glide.with(this)
                    .asGif()
                    .load(R.drawable.earth_anim)
                    .into(mBinding.imageEarth);

            mMainViewModel.getAllCountryData();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void showWorldStatus() {
        Animation slideUp = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up);

        if (mBinding.constraintStatusRoot.getVisibility() == View.GONE) {
            mBinding.constraintStatusRoot.setVisibility(View.VISIBLE);
            mBinding.constraintStatusRoot.startAnimation(slideUp);
        }
        mBinding.constraintStatusRoot.setVisibility(View.VISIBLE);
    }

    private void hideWorldStatus() {
        Animation slideDown = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);

        if (mBinding.constraintStatusRoot.getVisibility() == View.VISIBLE) {
            mBinding.constraintStatusRoot.setVisibility(View.GONE);
            mBinding.constraintStatusRoot.startAnimation(slideDown);
        }
        mBinding.constraintStatusRoot.setVisibility(View.GONE);
    }

    private void showBottomSheetDialog() {
        // initialize dataBinding on BottomSheet

        CountryTracking mTracking = mMainViewModel.getCountryTracking();

        FragmentCountryDialogBinding mDialogBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_country_dialog, null, false);

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(mDialogBinding.getRoot());

        if (mTracking.isHasData()) {
            mDialogBinding.textViewTitle.setText(mTracking.getCountryDetailList().get(0).getCountryName() + " Covid19 Status");
            mDialogBinding.countryRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
            mDialogBinding.countryRecyclerView.setAdapter(new RecyclerCountryStatusAdapter(MainActivity.this, mTracking.getCountryDetailList()));
        } else {
            mDialogBinding.textviewNoDataFound.setVisibility(View.VISIBLE);
        }

            mDialogBinding.textButton.setOnClickListener(nextBtnClickListener);
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showWorldStatus();
            }
        });
        bottomSheetDialog.show();
    }


    protected Marker createMarker(double latitude, double longitude , @IdRes int resId) {

        return mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(resId)));
    }

    private void getSpecificInformationWithAnimate(@NonNull LatLng latLng) {
        float zoomLevel = 6.0f;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel), new GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {
                showProgressDialog();

                mMainViewModel.getCountryNameFromLocation(latLng).observe(MainActivity.this, countryNameObserver);

            }
        });
    }


    @SuppressLint("ResourceType")
    private void chooseMarkerBasedOnSorting(int i, double latitude, double longitude) {
        if (i < CasesCount.LOW.getValue()){
            createMarker(latitude, longitude,R.drawable.ic_corona_white);
        }else if(i < CasesCount.MEDIUM.getValue()){
            createMarker(latitude, longitude,R.drawable.ic_corona_yellow);
        }else {
            createMarker(latitude, longitude,R.drawable.ic_corona_red);
        }
    }

    // endregion


}