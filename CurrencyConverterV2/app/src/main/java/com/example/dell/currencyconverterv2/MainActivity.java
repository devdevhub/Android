package com.example.dell.currencyconverterv2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    ListView listView;
    TextView dateTextView;
    final String[] keys = {"EUR", "USD", "GBP", "JPY", "AUD"};
    final String baseURL = "http://api.fixer.io/latest?base=";
    ArrayList<String> currencies;
    ArrayList<Double> rates;
    int currencySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner)(findViewById(R.id.spinner));
        listView = (ListView)(findViewById(R.id.listView));
        dateTextView = (TextView)(findViewById(R.id.dateTextView));
        rates = new ArrayList<Double>();
        currencies = new ArrayList<String>();
        for (String key : keys) {
            currencies.add(getString(getResources().getIdentifier(key, "string", getPackageName())));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        currencySelected = 0;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencySelected = position;
                rates.clear();
                maakListView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        maakListView();
    }

    private void maakListView() {
        ArrayList<String> listItems = new ArrayList<String>();
        for (int i = 0; i < currencies.size(); i++) {
            if (i != currencySelected) {
                String item = currencies.get(i)+": ";
                if (rates.isEmpty()) {
                    item += getString(R.string.msg);
                }
                else {
                    item += rates.get(i);
                }
                listItems.add(item);
            }
        }
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(listViewAdapter);
        String url = baseURL+keys[currencySelected];
        new DownloadTaak().execute(url);
    }

    class DownloadTaak extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String urlString = params[0];
            String jsonString  = "";
            HttpURLConnection connection = null;
            InputStream in = null;
            InputStreamReader reader = null;
            try {
                connection = (HttpURLConnection)(new URL(urlString).openConnection());
                in = connection.getInputStream();
                reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    data = reader.read();
                    char letter = (char)(data);
                    jsonString += letter;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                String date = jsonObject.getString("date");
                dateTextView.setText(date);
                JSONObject rats = jsonObject.getJSONObject("rates");
                rates.clear();
                for (String key : keys) {
                    double d;
                    try {
                        d = rats.getDouble(key);
                    }
                    catch (JSONException e) {
                        d = 0;
                    }
                    rates.add(d);
                }
                maakListView();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}