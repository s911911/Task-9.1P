package com.example.task71p;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private final int FINE_PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dbHelper = new DBHelper(this);

        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_PERMISSION_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        currentLocation = location;
                        if (myMap != null) {
                            showCurrentLocation();
                            showAllItemsOnMap();
                        }
                    } else {
                        Toast.makeText(this, "无法获取当前位置", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showCurrentLocation() {
        if (currentLocation != null && myMap != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            myMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("You here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        }
    }

    private void showAllItemsOnMap() {
        List<Item> itemList = dbHelper.getAllItems();
        if (itemList != null && !itemList.isEmpty()) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            for (Item item : itemList) {
                LatLng position = new LatLng(item.getLatitude(), item.getLongitude());

                myMap.addMarker(new MarkerOptions()
                        .position(position)
                        .title(item.getPostType() + ": " + item.getPersonName())
                        .snippet(item.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(
                                item.getPostType().equalsIgnoreCase("Lost")
                                        ? BitmapDescriptorFactory.HUE_RED
                                        : BitmapDescriptorFactory.HUE_GREEN)));

                boundsBuilder.include(position);
            }

            // 自动缩放地图以展示所有标记
            myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120));
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            myMap.setMyLocationEnabled(true);
        }

        if (currentLocation != null) {
            showCurrentLocation();
            showAllItemsOnMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "位置权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


