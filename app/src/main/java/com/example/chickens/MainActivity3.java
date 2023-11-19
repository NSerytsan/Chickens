package com.example.chickens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.R.layout;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.Manifest;
import android.widget.Toast;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity3 extends AppCompatActivity implements OnMapReadyCallback, RouteListener {

    String endLocationName;

    private LatLng startLocation, endLocation;
    boolean isPermissionGranted;
    GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        String contactAddress = getIntent().getStringExtra("CONTACT_ADDRESS");
        endLocationName = contactAddress;

        Log.i("CONTACT_ADDRESS", contactAddress);

        checkMyPermission();
        if(isPermissionGranted){
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            supportMapFragment.getMapAsync(MainActivity3.this);

        }
    }

    private void geoLocate() {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(endLocationName, 1);
            if(addressList.size()>0){
                Address address = addressList.get(0);
                endLocation = new LatLng(address.getLatitude(), address.getLongitude());
                gotoLocation(address.getLatitude(), address.getLongitude());
                gMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        gMap.moveCamera(cameraUpdate);
    }

    private void checkMyPermission() {
        Dexter.withContext(MainActivity3.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MainActivity3.this, "permission granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        geoLocate();

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                startLocation = latLng;
                googleMap.clear();
                geoLocate();
                googleMap.addMarker(markerOptions);

                getRoute(startLocation, endLocation);
            }
        });
    }

    private void getRoute(LatLng start, LatLng end) {
        RouteDrawing routeDrawing = new RouteDrawing.Builder()
                .context(MainActivity3.this)  // pass your activity or fragment's context
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this).alternativeRoutes(true)
                .waypoints(start, end)
                .build();
        routeDrawing.execute();
    }

    @Override
    public void onRouteFailure(ErrorHandling e) {
        Toast.makeText(this, "Route Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteStart() {
        Toast.makeText(this, "Route Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteSuccess(ArrayList<RouteInfoModel> list, int indexing) {
        Toast.makeText(this, "Route Success", Toast.LENGTH_SHORT).show();

        PolylineOptions polylineOptions = new PolylineOptions();
        ArrayList<Polyline> polylines = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == indexing) {
                Log.e("TAG", "onRoutingSuccess: routeIndexing" + indexing);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(12);
                polylineOptions.addAll(list.get(indexing).getPoints());
                polylineOptions.startCap(new RoundCap());
                polylineOptions.endCap(new RoundCap());
                Polyline polyline = gMap.addPolyline(polylineOptions);
                polylines.add(polyline);
            }
    }
    }

    @Override
    public void onRouteCancelled() {
        Toast.makeText(this, "Route Canceled", Toast.LENGTH_SHORT).show();
    }
}



