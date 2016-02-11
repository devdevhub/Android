package com.example.dell.animations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	
	boolean isBart;
    SeekBar seekBar;
    TextView speedText;
    ImageView bartImg, homerImg;
    RadioGroup radioGroup;
    int time, degrees;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isBart = true;
	    seekBar = (SeekBar)(findViewById(R.id.seekBar));
        speedText = (TextView)(findViewById(R.id.speedTextView));
		bartImg = (ImageView)(findViewById(R.id.bartImg));
		homerImg = (ImageView)(findViewById(R.id.homerImg));
        radioGroup = (RadioGroup)(findViewById(R.id.radioGroup));
        time = seekBar.getMax()-seekBar.getProgress();
        degrees = 4*360;

        homerImg.animate().alpha(0).setDuration(0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.fadeRadioButton) {
                    if (isBart) {
                        homerImg.animate().alpha(0).setDuration(0);
                        homerImg.animate().translationX(0).setDuration(0);
                        homerImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(0);
                    } else {
                        bartImg.animate().alpha(0).setDuration(0);
                        bartImg.animate().translationX(0).setDuration(0);
                        bartImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(0);
                    }
                }
                if (checkedId == R.id.moveRadioButton) {
                    if (isBart) {
                        homerImg.animate().alpha(1).setDuration(0);
                        homerImg.animate().translationX(-1000).setDuration(0);
                        homerImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(0);
                    } else {
                        bartImg.animate().alpha(1).setDuration(0);
                        bartImg.animate().translationX(1000).setDuration(0);
                        bartImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(0);
                    }
                }
                if (checkedId == R.id.rotateRadioButton) {
                    if (isBart) {
                        homerImg.animate().alpha(1).setDuration(0);
                        homerImg.animate().translationX(0).setDuration(0);
                        homerImg.animate().rotation(degrees).scaleX(0).scaleY(0).setDuration(0);
                    } else {
                        bartImg.animate().alpha(1).setDuration(0);
                        bartImg.animate().translationX(0).setDuration(0);
                        bartImg.animate().rotation(degrees).scaleX(0).scaleY(0).setDuration(0);
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                time = seekBar.getMax()-seekBar.getProgress();
                speedText.setText("Duration: "+String.format("%.2f sec", (double)(time)/1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speedText.setText("");
            }
        });
	}
	
	public void animate(View view) {
        if (radioGroup.getCheckedRadioButtonId() == R.id.fadeRadioButton) {
            fade();
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.moveRadioButton) {
            move();
        }
        if (radioGroup.getCheckedRadioButtonId() == R.id.rotateRadioButton) {
            rotate();
        }
        isBart = !isBart;
	}

    private void fade() {
        if (isBart) {
            bartImg.animate().alpha(0).setDuration(time);
            homerImg.animate().alpha(1).setDuration(time);
        }
        else {
            bartImg.animate().alpha(1).setDuration(time);
            homerImg.animate().alpha(0).setDuration(time);
        }
    }

    private void move() {
        if (isBart) {
            bartImg.animate().translationX(1000).setDuration(time);
            homerImg.animate().translationX(0).setDuration(time);
        }
        else {
            bartImg.animate().translationX(0).setDuration(time);
            homerImg.animate().translationX(-1000).setDuration(time);
        }
    }

    private void rotate() {
        if (isBart) {
            bartImg.animate().rotation(degrees).scaleX(0).scaleY(0).setDuration(time);
            homerImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(time);
        }
        else {
            bartImg.animate().rotation(0).scaleX(1).scaleY(1).setDuration(time);
            homerImg.animate().rotation(degrees).scaleX(0).scaleY(0).setDuration(time);
        }
    }

}