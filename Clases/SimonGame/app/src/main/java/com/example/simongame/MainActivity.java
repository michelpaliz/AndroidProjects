package com.example.simongame;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private AppCompatButton greenButton, redButton, yellowButton, blueButton;
    private Button btnStartGame, btnBestScores;
    private SoundPool soundPool;
    private Game game;
    private List<Integer> playerGuesses;
    private List<Integer> computerGuesses;

    public MainActivity() {
        game = new Game();
        playerGuesses = new ArrayList<>();
        computerGuesses = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cargamos los layout
        greenButton = findViewById(R.id.greenButton);
        greenButton.setOnClickListener(this);
        redButton = findViewById(R.id.redButton);
        redButton.setOnClickListener(this);
        yellowButton = findViewById(R.id.yellowButton);
        yellowButton.setOnClickListener(this);
        blueButton = findViewById(R.id.blueButton);
        blueButton.setOnClickListener(this);
        //Empezar el juego
        btnStartGame = findViewById(R.id.btnStart);
        btnBestScores = findViewById(R.id.btnBestScores);

        btnStartGame.setOnClickListener(v -> {
            btnStartGame.setVisibility(View.GONE);
            btnBestScores.setVisibility(View.GONE);
            game.generateSecuence(4);
            game.startGame(playerGuesses);
            System.out.println(game.getMessage());
            computerChooses();
            System.out.println(computerGuesses);

        });

    }

    public void computerChooses() {
        computerGuesses = game.getSecuenceToGuess();
        for (Integer integer : computerGuesses) {
            if (integer.equals(0)) {
                performAction(greenButton);
            }else if(integer.equals(1)){
                performAction(redButton);
            }else if (integer.equals(2)){
                performAction(yellowButton);
            }else if (integer.equals(3)){
                performAction(blueButton);
            }
        }
    }

    public void performAction(View appCompatActivity) {
        appCompatActivity.performClick();
        appCompatActivity.setPressed(true);
        appCompatActivity.invalidate();
        appCompatActivity.setPressed(false);
        appCompatActivity.invalidate();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.greenButton:
                playerGuesses.add(0);
                break;
            case R.id.redButton:
                playerGuesses.add(1);
                break;
            case R.id.yellowButton:
                playerGuesses.add(2);
                break;
            case R.id.blueButton:
                playerGuesses.add(3);
                break;
        }
    }


}