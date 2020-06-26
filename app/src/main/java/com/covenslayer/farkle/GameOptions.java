package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class GameOptions extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_options);
        Intent intent = getIntent();

        // This names ArrayList is the one passed from PlayGame screen containing players names
        names = intent.getStringArrayListExtra("extras");
    }

    // This button will take you back to the home screen
    public void goHomeScreen(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // This button takes user to start actual game
    // Gathers the user choices from option radio groups
    // index 0 = user selected option 1 ("5,000", "10,000", "20,000") for end score
    // index 1 = user selected option 2 ("Yes", "No") for 3 farkles rule
    // index 2 = user selected option 3 ("None", "250", "500", "1,000") for break in score
    // index 3 = name of first player
    // index 4 = total score of first player, initially set to 0
    // if 3 farkles is yes then index 5 and index 6 = "NF" for no farkle to keep up with farkles in a row
    //
    // repeat index 3 and 4 (and 5 and 6 if 3 farkles rule is yes) for every other player
    // index last = keeps track of turn number so we know who's turn it is
    // This ArrayList<String> is sent to activity to actually start the game
    public void startTheGame(View view)
    {
        ArrayList<String> allTheStuff = new ArrayList<>();
        RadioGroup radio1 = (RadioGroup) findViewById(R.id.radioGroup1);
        int id1 = radio1.getCheckedRadioButtonId();
        RadioButton radiobtn1 = (RadioButton) findViewById(id1);
        allTheStuff.add(radiobtn1.getText().toString());

        RadioGroup radio2 = (RadioGroup) findViewById(R.id.radioGroup2);
        int id2 = radio2.getCheckedRadioButtonId();
        RadioButton radiobtn2 = (RadioButton) findViewById(id2);
        allTheStuff.add(radiobtn2.getText().toString());

        RadioGroup radio3 = (RadioGroup) findViewById(R.id.radioGroup3);
        int id3 = radio3.getCheckedRadioButtonId();
        RadioButton radiobtn3 = (RadioButton) findViewById(id3);
        allTheStuff.add(radiobtn3.getText().toString());

        for(int i = 0; i < names.size(); i++) {
            allTheStuff.add(names.get(i));
            allTheStuff.add("0");
            if (radiobtn2.getText().toString().equalsIgnoreCase("yes")){
                allTheStuff.add("NF");
                allTheStuff.add("NF");
            }
        }
        allTheStuff.add("T1");

        Intent intent = new Intent(this, StartGame.class);
        intent.putStringArrayListExtra("extras", allTheStuff);
        startActivity(intent);
    }
}
