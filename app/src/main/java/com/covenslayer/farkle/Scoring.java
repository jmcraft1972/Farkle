package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

// This activity educates user on how to score. it is all done in the xml
public class Scoring extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoring);
    }

    // This button starts a new game. Next screen will take user to enter player names.
    public void play(View view)
    {
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }

    // This button takes user back to home screen
    public void goHomeScreen(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
