package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// This is welcome activity of app consisting on a logo and 2 buttons
// First button will take user to a screen where user can find out how to score the game
// Second button will initiate game play sequence
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Button to take user to game scoring info
    public void scoringScreen(View view)
    {
        Intent intent = new Intent(this, Scoring.class);
        startActivity(intent);
    }

    // Button to start a new game. Next screen will enter player names
    public void play(View view)
    {
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }
}
