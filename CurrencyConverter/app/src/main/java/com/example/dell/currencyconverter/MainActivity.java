package com.example.dell.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText input;
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) (findViewById(R.id.input));
        output = (TextView) (findViewById(R.id.output));
    }

    public void convEURtoUSD(View view) {
        try {
            double usd = Double.parseDouble(input.getText().toString()) * 1.108625;
            if ((double)((int)(usd*100))/100 == (int)(usd)) {
                output.setText("$ "+(int)(usd)+",-");
            } else {
                output.setText("$ "+String.format("%.2f", usd));
            }
        }
        catch (NumberFormatException e) {}
    }
    public void convUSDtoEUR(View view) {
        try {
            double eur = Double.parseDouble(input.getText().toString())/1.108625;
            if ((double)((int)(eur*100))/100 == (int)(eur)) {
                output.setText("€ "+(int)(eur)+",-");
            }
            else {
                output.setText("€ "+String.format("%.2f", eur));
            }
        }
        catch (NumberFormatException e) {}
    }

}
