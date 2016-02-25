package com.example.dell.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int time;
    boolean running;
    boolean finished;
    TextView timeDisplay;
    Button button;
    SeekBar slider;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        time = 300;
        running = false;
        finished = false;
        timeDisplay = (TextView)(findViewById(R.id.textView));
        button = (Button)(findViewById(R.id.button));
        slider = (SeekBar)(findViewById(R.id.seekBar));

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time = progress;
                timeDisplay.setText(String.format("%02d:%02d.0", time/60, time%60));
                updateTimer();
                if (running) {
                    timer.start();
                    button.setText("PAUSE");
                }
                finished = false;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void startPause(View view) {
        if (finished) {
            time = 300;
            finished = false;
            running = false;
        }
        updateTimer();
        if (running) {
            timer.cancel();
            button.setText("START");
        }
        else {
            timer.start();
            button.setText("PAUSE");
        }
        running = !running;
    }

    private void updateTimer() {
        if (timer != null) {timer.cancel();}
        timer = new CountDownTimer((time+1)*1000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeDisplay.setText(String.format("%02d:%02d.%d", millisUntilFinished/1000/60, millisUntilFinished/1000%60, millisUntilFinished%1000/100));
                slider.setProgress((int)(millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {
                MediaPlayer sound = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                sound.start();
                finished = true;
                button.setText("RESET");
            }
        };
    }

}
