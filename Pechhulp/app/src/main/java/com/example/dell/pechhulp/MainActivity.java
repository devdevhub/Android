package com.example.dell.pechhulp;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(getResources().getString(R.string.title_activity_main));
        setTaskDescription(new ActivityManager.TaskDescription(null, null, Color.LTGRAY));

        putDrawableOnButton(R.id.button, resize(ContextCompat.getDrawable(getApplicationContext(), R.drawable.main_btn_warning), 80, 70));

        LocationManager lm = (LocationManager)(getSystemService(LOCATION_SERVICE));
        //Display message if there is no internet connection
        if (!isNetworkConnected() && !isFinishing()) {
            noInternetDialog();
        }
        else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            noLocationDialog();
        }
    }

    private void noInternetDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            builder.setMessage(R.string.message_no_internet).setTitle("Geen internetverbinding");
            builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setPositiveButton("Probeer opnieuw", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    noInternetDialog();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void noLocationDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
            builder.setMessage(R.string.message_no_location).setTitle("Locatieservices uitgeschakeld");
            builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.setPositiveButton("Ga naar instellingen", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager)(getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void putDrawableOnButton(int buttonID, Drawable icon) {
        Button button = (Button)(findViewById(buttonID));
        button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private Drawable resize(Drawable drawable, int width, int height) {
        Bitmap bitmap = ((BitmapDrawable)(drawable)).getBitmap();
        return new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.infoButton) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startMapsActivity(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

}
