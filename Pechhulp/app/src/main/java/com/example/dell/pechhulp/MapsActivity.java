package com.example.dell.pechhulp;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        this.setTitle(getResources().getString(R.string.title_activity_maps));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTaskDescription(new ActivityManager.TaskDescription(null, null, Color.LTGRAY));

        if (!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, 1337);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        styleButton(R.id.button, resize(ContextCompat.getDrawable(getApplicationContext(), R.drawable.main_btn_tel), 70, 70));
        styleButton(R.id.callButton, resize(ContextCompat.getDrawable(getApplicationContext(), R.drawable.main_btn_phone), 70, 70));
        styleButton(R.id.cancelButton, resize(ContextCompat.getDrawable(getApplicationContext(), R.drawable.main_btn_close), 30, 30));
    }

    private void styleButton(int buttonID, Drawable icon) {
        Button button = (Button) (findViewById(buttonID));
        button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        button.setTransformationMethod(null);
    }

    private Drawable resize(Drawable drawable, int width, int height) {
        Bitmap bitmap = ((BitmapDrawable) (drawable)).getBitmap();
        return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    private boolean canAccessLocation() {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (!canAccessLocation()) {
            requestPermissions(LOCATION_PERMS, 1337);
        } else {
            mMap = googleMap;

            LocationManager service = (LocationManager) (getSystemService(LOCATION_SERVICE));
            String provider = service.getBestProvider(new Criteria(), false);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            LatLng sydney = new LatLng(-34, 151);
            Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_mini)));
            Location location = service.getLastKnownLocation(provider);
            try {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                marker.setPosition(userLocation);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

            // Setting a custom info window adapter for the google map
            GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
                View infoWindow = getLayoutInflater().inflate(R.layout.marker, null);

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    infoWindow = getLayoutInflater().inflate(R.layout.marker, null);
                    TextView addressView = (TextView)(infoWindow.findViewById(R.id.address));

                    LocationManager service = (LocationManager) (getSystemService(LOCATION_SERVICE));
                    String provider = service.getBestProvider(new Criteria(), false);
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return null;
                    }
                    Location location = service.getLastKnownLocation(provider);

                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String country = addresses.get(0).getCountryName();
                        addressView.setText(address+", "+country);
                    } catch (IOException|NullPointerException e) {
                        e.printStackTrace();
                    }
                    return infoWindow;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {
                    return null;
                }
            };
            mMap.setInfoWindowAdapter(infoWindowAdapter);
            marker.showInfoWindow();



        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void notifyPhoneCosts(View view) {
        findViewById(R.id.notification).setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        findViewById(R.id.filter).setVisibility(View.VISIBLE);
    }

    public void cancelCall(View view) {
        findViewById(R.id.notification).setVisibility(View.GONE);
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        findViewById(R.id.filter).setVisibility(View.GONE);
    }

    public void call(View view) {
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:+319007788990")));
    }

}
