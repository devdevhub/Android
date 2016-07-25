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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        styleButton(R.id.button, resize(ContextCompat.getDrawable(getApplicationContext(), R.drawable.main_btn_warning), 80, 70));

        //Display message if there is no internet connection.. *doesn't work, somehow*
        if (!isNetworkConnected()) {
            try {
                if(!isFinishing()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setMessage("message").setTitle("title");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager)(getSystemService(Context.CONNECTIVITY_SERVICE))).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void styleButton(int buttonID, Drawable icon) {
        Button button = (Button)(findViewById(buttonID));
        button.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        button.setTransformationMethod(null);
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
