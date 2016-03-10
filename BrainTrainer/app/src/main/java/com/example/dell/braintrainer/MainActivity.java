package com.example.dell.braintrainer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int highScore = getPreferences(MODE_PRIVATE).getInt("highScore", 0);
        int recentScore = getIntent().getIntExtra("score", getPreferences(MODE_PRIVATE).getInt("recentScore", 0));
        getPreferences(MODE_PRIVATE).edit().putInt("recentScore", recentScore).commit();
        String highScoreUnlocked = "";
        if (highScore < recentScore) {
            highScore = recentScore;
            getPreferences(MODE_PRIVATE).edit().putInt("highScore", highScore).commit();
            highScoreUnlocked = "Congratulations, you just beat the old High Score!";
        }
        ((TextView)(findViewById(R.id.scoreTextView))).setText(
            "Recent Score: "+recentScore+"\n"+
            "High Score: "+highScore+"\n\n"+
            highScoreUnlocked
        );
    }

    public void start(View view) {
        startActivity(new Intent(this, BrainTrainerActivity.class));
    }

}
