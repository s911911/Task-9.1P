package com.example.task71p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {
    private static final int PERM_LOCATION_REQUEST_CODE = 200;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private RadioGroup rgType;
    private EditText etName, etPhone, etDesc, etDate;
    private Button btnCurrentLoc, btnSave;
    private DBHelper db;
    private FusedLocationProviderClient locClient;
    private double lat = 0.0, lng = 0.0;
    private String locationText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("lat") && intent.hasExtra("lng")) {
            lat = intent.getDoubleExtra("lat", 0.0);
            lng = intent.getDoubleExtra("lng", 0.0);
            reverseGeocodeAndSetAddress(lat, lng);
        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        locClient = LocationServices.getFusedLocationProviderClient(this);
        db = new DBHelper(this);

        rgType       = findViewById(R.id.rgPostType);
        etName       = findViewById(R.id.etName);
        etPhone      = findViewById(R.id.etPhone);
        etDesc       = findViewById(R.id.etDescription);
        etDate       = findViewById(R.id.etDate);
        btnCurrentLoc= findViewById(R.id.btnCurrentLoc);
        btnSave      = findViewById(R.id.btnSave);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    if (place.getLatLng() != null) {
                        lat = place.getLatLng().latitude;
                        lng = place.getLatLng().longitude;
                        locationText = place.getName();
                        Toast.makeText(CreateAdvertActivity.this,
                                "Identified place: " + locationText, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toast.makeText(CreateAdvertActivity.this,
                            "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            View autocompleteView = autocompleteFragment.getView();
            if (autocompleteView != null) {
                autocompleteView.setBackgroundResource(android.R.drawable.editbox_background_normal);
                EditText et = autocompleteView.findViewById(
                        com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                if (et != null) {
                    et.setHint("Search location");
                    et.setTextSize(16);
                }
            }
        }

        // 重构按钮为调用 getLastLocation()
        btnCurrentLoc.setOnClickListener(v -> getLastLocation());

        btnSave.setOnClickListener(v -> {
            String type     = rgType.getCheckedRadioButtonId() == R.id.rbLost ? "Lost" : "Found";
            String name     = etName.getText().toString().trim();
            String phone    = etPhone.getText().toString().trim();
            String desc     = etDesc.getText().toString().trim();
            String date     = etDate.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || desc.isEmpty() || locationText.isEmpty()) {
                Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            db.insertItem(type, name, phone, desc, date, locationText, lat, lng);
            Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        locClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                reverseGeocodeAndSetAddress(lat, lng);
            } else {
                Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 使用 Geocoder 反向地址解析并更新搜索框
    private void reverseGeocodeAndSetAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                locationText = addresses.get(0).getAddressLine(0);

                AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

                if (autocompleteFragment != null) {
                    View autoView = autocompleteFragment.getView();
                    if (autoView != null) {
                        EditText et = autoView.findViewById(
                                com.google.android.libraries.places.R.id.places_autocomplete_search_input);
                        if (et != null) {
                            et.setText(locationText);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Address resolution failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERM_LOCATION_REQUEST_CODE ||
                requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(); // 权限通过后重新尝试定位
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
