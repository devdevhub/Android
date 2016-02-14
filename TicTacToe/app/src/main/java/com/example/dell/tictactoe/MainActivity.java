package com.example.dell.tictactoe;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int currentPlayer = 1;
    int[] gamePlay = new int[9];
    int[][] winListList = {
        {0, 1, 2},
        {3, 4, 5},
        {6, 7, 8},
        {0, 3, 6},
        {1, 4, 7},
        {2, 5, 8},
        {0, 4, 8},
        {2, 4, 6},
    };
    RelativeLayout gameOverScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameOverScreen = (RelativeLayout)(findViewById(R.id.gameOverScreen));
    }

    public void play(View view) {
        ImageView imageView = (ImageView)(view);
        int index = Integer.parseInt(imageView.getTag().toString());
        if (gamePlay[index] == 0 && !isGameOver()) {
            animate(imageView, index);
            if (isGameOver()) {
                TextView gameOverMesssage = (TextView)(findViewById(R.id.gameOverMessage));
                if (checkWinner() == 0) {
                    gameOverMesssage.setText("It's a tie.");
                }
                else {
                    gameOverMesssage.setText("Player "+checkWinner()+" is victorious!");
                }
                gameOverScreen.setVisibility(View.VISIBLE);
            }
            else {
                if (currentPlayer == 1) {
                    currentPlayer = 2;
                }
                else if (currentPlayer == 2) {
                    currentPlayer = 1;
                }
            }
        }
    }

    private void animate(ImageView imageView, int index) {
        imageView.setTranslationX(-200);
        imageView.setTranslationY(-1000);
        if (currentPlayer == 1) {
            gamePlay[index] = 1;
            imageView.setImageResource(R.drawable.kruisje);
        }
        else if (currentPlayer == 2) {
            gamePlay[index] = 2;
            imageView.setImageResource(R.drawable.rondje);
        }
        imageView.animate().translationX(0).translationY(0).setDuration(150);
    }

    private boolean isGameOver() {
        boolean gameOver = true;
        for (int play : gamePlay) {
            if (play == 0) {
                gameOver = false;
                break;
            }
        }
        if (checkWinner() != 0) {
            gameOver = true;
        }
        return gameOver;
    }

    private int checkWinner() {
        int winner = 0;
        for (int[] winList : winListList) {
            int winCount = 0;
            for (int win : winList) {
                if (gamePlay[win] == currentPlayer) {
                    winCount++;
                }
            }
            if (winCount == 3) {
                winner = currentPlayer;
                break;
            }
        }
        return winner;
    }

    public void reset(View view) {
        currentPlayer = 1;
        gamePlay = new int[9];
        gameOverScreen.setVisibility(View.GONE);
        TableLayout tableLayout = (TableLayout)(findViewById(R.id.tableLayout));
        for (int iRow = 0; iRow < 3; iRow++) {
            TableRow tableRow = (TableRow)(tableLayout.getChildAt(iRow));
            for (int iView = 0; iView < 3; iView++) {
                ImageView imageView = (ImageView)(tableRow.getChildAt(iView));
                imageView.setImageResource(0);
            }
        }
    }

}
