package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// This activity gives final player rankings and buttons to go to home screen or start a new game
public class Winner extends AppCompatActivity {

    private LinearLayout mLayout;
    TextView mTView;
    Map<String, Integer> idMap = new HashMap<>();
    ArrayList<String> names = new ArrayList<>();
    String opt2;
    boolean farkles3;
    Map<Integer, String> winningPairs = new HashMap<>();
    ArrayList<String> playerNames = new ArrayList<>();
    String[] reverseNames;
    ArrayList<Integer> scores = new ArrayList<>();
    int counter = 0;
    ArrayList<String> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        Intent intent= getIntent();
        names = intent.getStringArrayListExtra("extras");

        opt2 = names.get(1);
        switch(opt2){
            case "Yes":
            case "YES":
            case "yes":
                farkles3 = true;
                break;
            default:
                farkles3 = false;
                break;
        }

        Map<String, ArrayList<String>> tieWith = new HashMap<>();
        ArrayList<String> whosTied = new ArrayList<>();
        if (farkles3) {
            for (int i = 3; i < names.size() - 1; i+=4) {
                players.add(names.get(i));
                if(winningPairs.containsKey(Integer.parseInt(names.get(i + 1)))){
                    whosTied.add(names.get(i));
                    tieWith.put(winningPairs.get(Integer.parseInt(names.get(i + 1))), whosTied);
                } else{
                    winningPairs.put(Integer.parseInt(names.get(i + 1)), names.get(i));
                }
                scores.add(Integer.parseInt(names.get(i + 1)));
            }
        } else {
            for (int i = 3; i < names.size() - 1; i+=2) {
                players.add(names.get(i));
                if(winningPairs.containsKey(Integer.parseInt(names.get(i + 1)))){
                    whosTied.add(names.get(i));
                    tieWith.put(winningPairs.get(Integer.parseInt(names.get(i + 1))), whosTied);
                } else{
                    winningPairs.put(Integer.parseInt(names.get(i + 1)), names.get(i));
                }
                scores.add(Integer.parseInt(names.get(i + 1)));
            }
        }
        Collections.sort(scores, Collections.<Integer>reverseOrder());
        reverseNames = new String[scores.size()];
        for (int i = 0; i < scores.size(); i++){
            reverseNames[i] = winningPairs.get(scores.get(i));
            if(tieWith.containsKey(winningPairs.get(scores.get(i)))){
                int j = 0;
                for(String n : tieWith.get(winningPairs.get(scores.get(i)))){
                    j++;
                    reverseNames[i +j] = n;
                }
                i += j;
            }
        }


        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mTView = (TextView) findViewById(R.id.winners1);

        for (int i = 0; i < scores.size(); i++){
            counter++;
            mLayout.addView(createNewTextView(mTView.getText().toString()));
        }

        for (int i = 0; i < scores.size(); i++){
            String item = "winners" + (i + 1);
            TextView players = (TextView) findViewById(idMap.get(item));
            String texts = (i + 1) + "   " + reverseNames[i] + "   " + scores.get(i);
            players.setText(texts);
            players.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.winnersFont));
            players.setTextColor(Color.parseColor("#000000"));
        }
    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView editView = new TextView(this);
        editView.setLayoutParams(lparams);
        editView.setId(counter);
        String name = "winners" + counter;
        idMap.put(name, counter);
        return editView;
    }

    public void goHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void playAgain(View view){
        Intent intent = new Intent(this, PlayGame.class);
        startActivity(intent);
    }
}
