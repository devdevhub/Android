package com.example.dell.braintrainer;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class BrainTrainerActivity extends AppCompatActivity {

    int score = 0, turnsPlayed = 0, a, b;
    Button[] buttonArray = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_trainer);
        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i] = (Button)(((GridLayout)(findViewById(R.id.buttonsGridLayout))).getChildAt(i));
        }
        new CountDownTimer(30000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView)(findViewById(R.id.timerTextView))).setText(String.format("%04.1f", (double)(millisUntilFinished/100)/10));
            }
            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("score", score));
            }
        }.start();
        generateSum();
    }

    public void play(View view) {
        turnsPlayed++;
        if (Integer.parseInt(((Button)(view)).getText().toString()) == a+b){
            score++;
        }
        ((TextView)(findViewById(R.id.progressTextView))).setText(String.format("%03d/%03d", score, turnsPlayed));
        generateSum();
    }

    private void generateSum() {
        a = (int)(Math.random()*50)+1;
        b = (int)(Math.random()*50)+1;
        ((TextView)(findViewById(R.id.sumTextView))).setText(a+" + "+b+" =");
        int buttonAnswerIndex = (int)(Math.random()*4);
        int[] optionArray = new int[4];
        for (int i = 0; i < buttonArray.length; i++) {
            if (i == buttonAnswerIndex) {
                buttonArray[i].setText(""+(a+b));
                optionArray[i] = a+b;
            }
            else {
                int option;
                boolean optionTaken = false;
                do {
                    option = (int)(Math.random()*100)+1;
                    for (int item : optionArray) {
                        if (item == option) {
                            optionTaken = true;
                            break;
                        }
                        else {
                            optionTaken = false;
                        }
                    }
                } while (option == a+b || optionTaken);
                buttonArray[i].setText(""+option);
                optionArray[i] = option;
            }
        }
    }

}