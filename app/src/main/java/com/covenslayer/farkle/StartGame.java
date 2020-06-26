package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// This activity is where the games is played
// The ArrayList<String> names which is passed in from last activity is defined as follows
// names[0] = user selected option 1 ("5,000", "10,000", "20,000") for end score
// name[1] =  user selected option 2 ("Yes", "No") for 3 farkles rule
// name[2] = user selected option 3 ("None", "250", "500", "1,000") for break in score
// index 3 = name of first player
// index 4 = total score of first player, initially set to 0
// if 3 farkles is yes then index 5 and index 6 = "NF" for no farkle to keep up with farkles in a row
// repeat index 3 and 4 (and 5 and 6 if 3 farkles rule is yes) for every other player
// index last = keeps track of turn number so we know who's turn it is

public class StartGame extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<>();
    int endScore, breakIn, accumulatedPts, totalPts;
    boolean farkles3, alreadyBrokeIn;
    TextView pName, accPtsText, accPtsNum, totPtsText, totPtsNum;
    String opt1, opt2, opt3;
    ArrayList<String> players = new ArrayList<>();
    int turnNum, rollNum;
    CheckBox cBox1, cBox2, cBox3, cBox4, cBox5, cBox6;
    ImageButton iBtn1, iBtn2, iBtn3, iBtn4, iBtn5, iBtn6;
    boolean[] alreadyChecked = new boolean[6];
    Map<String, Integer> diePosWithRoll = new HashMap<>();
    Button passDice, quitGame, getMyPoints, bank, roll;
    int counter = 0;
    Map<String, Integer> idMap = new HashMap<>();
    Map<Integer, String> winningPairs = new HashMap<>();
    ArrayList<Integer> scores = new ArrayList<>();
    String[] reverseNames;
    private LinearLayout mLayout;
    TextView mTView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        Intent intent= getIntent();
        names = intent.getStringArrayListExtra("extras");

        rollNum = 0;

        bank = (Button) findViewById(R.id.btnBank);
        roll = (Button) findViewById(R.id.btnRoll);
        quitGame  = (Button) findViewById(R.id.btnQuit);
        getMyPoints = (Button) findViewById(R.id.btnScorePeek);
        passDice = (Button) findViewById(R.id.btnPass);
        passDice.setEnabled(false);
        quitGame.setEnabled(true);
        getMyPoints.setEnabled(true);
        bank.setEnabled(true);
        roll.setEnabled(true);
        pName = (TextView) findViewById(R.id.whosTurn);
        accPtsText = (TextView) findViewById(R.id.accPtsText);
        accPtsNum = (TextView) findViewById(R.id.accPtsPlayer);
        totPtsText = (TextView) findViewById(R.id.totPtsText);
        totPtsNum = (TextView) findViewById(R.id.totPtsPlayer);
        cBox1 = (CheckBox) findViewById(R.id.checkBox1);
        cBox2 = (CheckBox) findViewById(R.id.checkBox2);
        cBox3 = (CheckBox) findViewById(R.id.checkBox3);
        cBox4 = (CheckBox) findViewById(R.id.checkBox4);
        cBox5 = (CheckBox) findViewById(R.id.checkBox5);
        cBox6 = (CheckBox) findViewById(R.id.checkBox6);
        cBox1.setEnabled(false);
        cBox2.setEnabled(false);
        cBox3.setEnabled(false);
        cBox4.setEnabled(false);
        cBox5.setEnabled(false);
        cBox6.setEnabled(false);
        iBtn1 = (ImageButton) findViewById(R.id.btnd1);
        iBtn2 = (ImageButton) findViewById(R.id.btnd2);
        iBtn3 = (ImageButton) findViewById(R.id.btnd3);
        iBtn4 = (ImageButton) findViewById(R.id.btnd4);
        iBtn5 = (ImageButton) findViewById(R.id.btnd5);
        iBtn6 = (ImageButton) findViewById(R.id.btnd6);

        opt1 = names.get(0);
        switch(opt1) {
            case "5,000":
                endScore = 5000;
                break;
            case "10,000":
                endScore = 10000;
                break;
            case "20,000":
                endScore = 20000;
                break;
        }

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

        opt3 = names.get(2);
        switch(opt3) {
            case "250":
                breakIn = 250;
                break;
            case "500":
                breakIn = 500;
                break;
            case "1,000":
                breakIn = 1000;
                break;
            default:
                breakIn = 0;
                break;
        }

        turnNum = Integer.parseInt(names.get(names.size() - 1).substring(1));

        // This section ranks players for rankings view if user scrolls down past the buttons
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
        String pTurn = players.get((turnNum - 1) % players.size());
        String pTurn1 = pTurn + ", it is your turn";
        Toast.makeText(getApplicationContext(),pTurn1, Toast.LENGTH_SHORT).show();
        pName.setText(pTurn1);
        accPtsNum.setText("0");
        totPtsNum.setText(names.get(names.indexOf(pTurn) + 1));
        accumulatedPts = 0;
        totalPts = Integer.parseInt(names.get(names.indexOf(pTurn) + 1));

        //This section sorts arrays for names and scores so each array's indices will match up in decreasing order of score
        reverseNames = new String[scores.size()];
        Collections.sort(scores, Collections.<Integer>reverseOrder());
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

        // This section adds texts views of rankings of players
        mLayout = (LinearLayout) findViewById(R.id.rankings);
        mTView = (TextView) findViewById(R.id.currentRankings1);

        for (int i = 0; i < scores.size(); i++){
            counter++;
            mLayout.addView(createNewTextView(mTView.getText().toString()));
        }

        for (int i = 0; i < scores.size(); i++){
            String item = "currentRankings" + (i + 1);
            TextView players = (TextView) findViewById(idMap.get(item));
            String texts = (i + 1) + "   " + reverseNames[i] + "   " + scores.get(i);
            players.setText(texts);
            players.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.winnersFont));
            players.setTextColor(Color.parseColor("#000000"));
        }

        // If player's total points has  equalled or surpassed the endScore value and
        // after everyone else has had one final turn
        // then go to winners end screen
        if(totalPts >= endScore) {
            Intent winner = new Intent(this, Winner.class);
            winner.putStringArrayListExtra("extras", names);
            startActivity(winner);
        }

    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView editView = new TextView(this);
        editView.setLayoutParams(lparams);
        editView.setId(counter);
        String name = "currentRankings" + counter;
        idMap.put(name, counter);
        return editView;
    }

    // Button to send user to home screen
    public void goHomeScreen(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Button for user to bank points and end turn
    public void bankPoints(View view){
        int getLastRole = getMyScore();
        if(totalPts + accumulatedPts + getLastRole>= breakIn) {
            accumulatedPts += getLastRole;
            totalPts += accumulatedPts;
            String pTurn = players.get((turnNum - 1) % players.size());

            // If banked points put user's total points equalling or exceeding endScore,
            // then let everyone know they have one final turn
            if (totalPts >= endScore) {
                Toast.makeText(getApplicationContext(),pTurn + " has reached the end score, everyone else has 1 more turn to win.", Toast.LENGTH_SHORT).show();
            }
            names.set(names.size() - 1, "T" + (turnNum + 1));
            names.set(names.indexOf(pTurn) + 1, String.valueOf(totalPts));
            if(farkles3){
                names.set(names.indexOf(pTurn) +  2, "NF");
                names.set(names.indexOf(pTurn) +  3, "NF");
            }
            Intent intent = new Intent(this, StartGame.class);
            intent.putStringArrayListExtra("extras", names);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),"You need at least " + breakIn + " points to break in.", Toast.LENGTH_SHORT).show();
        }

    }

    // This button indicates user has accepted whatever score is earned from the checked dice
    // and that score will be added to user's accumulated points for this turn
    // and any unchecked and enabled box will be replaced by a new roll
    public void roll(View view) {

        rollNum++;
        if (rollNum > 1) {
            int score = getMyScore();
            accumulatedPts += score;
            accPtsNum.setText(String.valueOf(accumulatedPts));
        }
        diePosWithRoll.clear();
        int[] numOfEach = getHowManyOfEach();
        checkForFarkle(numOfEach);

    }

    // After every roll, this method is called
    // If user farkles, all buttons are disabled except pass dice and quit buttons
    // and no other action can be taken
    // If 3 farkle rule is in effect, then this checks to see if points should be deducted
    public void checkForFarkle(int[] numbers){
        int score = getMyScoreForThisRoll(numbers);
        if (score == 0) {
            if (farkles3) {
                int whichPlayer = ((turnNum - 1) % players.size());
                if(names.get(3 + whichPlayer * 4 + 2).equalsIgnoreCase("F")) {
                    if(names.get(3 + whichPlayer * 4 + 3).equalsIgnoreCase("F")) {
                        if (totalPts > 1000){
                            Toast.makeText(getApplicationContext(),"3 farkles in a row, lose 1000 pts.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"3 farkles in a row, lose " + totalPts + " pts.", Toast.LENGTH_SHORT).show();
                        }
                        totalPts = totalPts < 1000 ? 0 : totalPts - 1000;
                        names.set(3 + whichPlayer * 4 + 3, "NF");
                        names.set(3 + whichPlayer * 4 + 2, "NF");
                        names.set(3 + whichPlayer * 4 + 1, String.valueOf(totalPts));
                    } else {
                        names.set(3 + whichPlayer * 4 + 3, "F");
                    }
                } else {
                    names.set(3 + whichPlayer * 4 + 2, "F");
                }
            }
            cBox1.setChecked(false);
            cBox2.setChecked(false);
            cBox3.setChecked(false);
            cBox4.setChecked(false);
            cBox5.setChecked(false);
            cBox6.setChecked(false);
            Toast.makeText(getApplicationContext(),"You have farkled, please pass the dice.", Toast.LENGTH_SHORT).show();
            passDice.setEnabled(true);
            roll.setEnabled(false);
            bank.setEnabled(false);
            getMyPoints.setEnabled(false);
        }
    }

    // This button is enabled after a farkle and user must pass dice to next player
    public void pass(View view) {
        names.set(names.size() - 1, "T" + (turnNum + 1));
        Intent intent = new Intent(this, StartGame.class);
        intent.putStringArrayListExtra("extras", names);
        startActivity(intent);
    }

    // This method starts the getting score procedure
    // Starts by gathering how many of each number is rolled for each roll
    // Only newly checked boxes count. Those boxes that have previously been checked have already
    // been accounted for in accumulatedPta
    public int getMyScore () {
        int[] howMany = new int[6];
        if(cBox1.isChecked() && !alreadyChecked[0]) {
            howMany[diePosWithRoll.get("d1") - 1] += 1;
        }
        if(cBox2.isChecked() && !alreadyChecked[1]) {
            howMany[diePosWithRoll.get("d2") - 1] += 1;
        }
        if(cBox3.isChecked() && !alreadyChecked[2]) {
            howMany[diePosWithRoll.get("d3") - 1] += 1;
        }
        if(cBox4.isChecked() && !alreadyChecked[3]) {
            howMany[diePosWithRoll.get("d4") - 1] += 1;
        }
        if(cBox5.isChecked() && !alreadyChecked[4]) {
            howMany[diePosWithRoll.get("d5") - 1] += 1;
        }
        if(cBox6.isChecked() && !alreadyChecked[5]) {
            howMany[diePosWithRoll.get("d6") - 1] += 1;
        }
        return getMyScoreForThisRoll(howMany);
    }

    // This is accssed from the check scores button
    // This starts the get scores process for user view before banking or rolling again
    public void whatWouldMyScoreBe(View view) {
        int[] howMany = new int[6];
        if(cBox1.isChecked() && !alreadyChecked[0]) {
            howMany[diePosWithRoll.get("d1") - 1] += 1;
        }
        if(cBox2.isChecked() && !alreadyChecked[1]) {
            howMany[diePosWithRoll.get("d2") - 1] += 1;
        }
        if(cBox3.isChecked() && !alreadyChecked[2]) {
            howMany[diePosWithRoll.get("d3") - 1] += 1;
        }
        if(cBox4.isChecked() && !alreadyChecked[3]) {
            howMany[diePosWithRoll.get("d4") - 1] += 1;
        }
        if(cBox5.isChecked() && !alreadyChecked[4]) {
            howMany[diePosWithRoll.get("d5") - 1] += 1;
        }
        if(cBox6.isChecked() && !alreadyChecked[5]) {
            howMany[diePosWithRoll.get("d6") - 1] += 1;
        }
        int score = getMyScoreForThisRoll(howMany);
        String message = "You would score " + score + " points.";
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
    }

    // Once user hits roll button, then this method is called and rolls all unchecked and enabled dice
    public int[] getHowManyOfEach() {
        int[] howManyOfEach = new int[6];
        if(cBox1.isChecked() && cBox2.isChecked() && cBox3.isChecked() && cBox4.isChecked() && cBox5.isChecked() && cBox6.isChecked()) {
            cBox1.setChecked(false);
            cBox2.setChecked(false);
            cBox3.setChecked(false);
            cBox4.setChecked(false);
            cBox5.setChecked(false);
            cBox6.setChecked(false);
            for(int i = 0; i < 6; i++){
                alreadyChecked[i] = false;
            }
        }
        if (!cBox1.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn1.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox1.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d1", num);
        } else {
            cBox1.setEnabled(false);
            alreadyChecked[0] = true;
            diePosWithRoll.put("d1", 0);
        }
        if (!cBox2.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn2.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox2.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d2", num);
        } else {
            cBox2.setEnabled(false);
            alreadyChecked[1] = true;
            diePosWithRoll.put("d2", 0);
        }
        if (!cBox3.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn3.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox3.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d3", num);
        } else {
            cBox3.setEnabled(false);
            alreadyChecked[2] = true;
            diePosWithRoll.put("d3", 0);
        }
        if (!cBox4.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn4.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox4.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d4", num);
        } else {
            cBox4.setEnabled(false);
            alreadyChecked[3] = true;
            diePosWithRoll.put("d4", 0);
        }
        if (!cBox5.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn5.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox5.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d5", num);
        } else {
            cBox5.setEnabled(false);
            alreadyChecked[4] = true;
            diePosWithRoll.put("d5", 0);
        }
        if (!cBox6.isChecked()) {
            int num = (int) (Math.random() * 6 + 1);
            String img = "dice" + num + "small";
            iBtn6.setImageResource(getResources().getIdentifier(img, "drawable", getPackageName()));
            cBox6.setEnabled(true);
            howManyOfEach[num - 1] += 1;
            diePosWithRoll.put("d6", num);
        } else {
            cBox6.setEnabled(false);
            alreadyChecked[5] = true;
            diePosWithRoll.put("d6", 0);
        }
        return howManyOfEach;
    }

    // This method actually returns the score of the roll of the boxes that are newly checked
    public int getMyScoreForThisRoll(int[] howMany) {
        if (howMany[0]==1 && howMany[1]==1 && howMany[2]==1 && howMany[3]==1 && howMany[4]==1 && howMany[5]==1) {
            return 1500;
        }
        if (howMany[0] == 6) {
            return 4000;
        }
        for(int i = 1; i < 7; i++) {
            if(howMany[i - 1] == 6) {
                return 2500;
            } else if (howMany[i - 1] == 5) {
                if (i == 1){
                    return 3000 + 50 * howMany[4];
                }else if(i == 5){
                    return 1500 + 100 * howMany[0];
                }

                return 300 * i + 100 * howMany[0] + 50 * howMany[4];
            } else if (howMany[i - 1] == 4) {
                if(i == 1){
                    return 2000 + 50 * howMany[4];
                }
                for(int j= i + 1; j < 7; j ++) {
                    if(howMany[j - 1] == 2) {
                        return 1500;
                    }
                }
                if(i == 5){
                    return 1000 + 100 * howMany[0];
                }
                return 200 * i + 100 * howMany[0] + 50 * howMany[4];
            } else if (howMany[i - 1] == 3) {

                for(int j= i + 1; j < 7; j ++) {
                    if (howMany[j - 1] == 3) {
                        return 2500;
                    }
                }
                if (i == 1) {
                    return 1000 + 50 * howMany[4];
                }else if(i == 5){
                    return 500 + 100 * howMany[0];
                }
                return 100 * i + 100 * howMany[0] + 50 * howMany[4];
            } else if (howMany[i - 1] == 2) {
                for(int j= i + 1; j < 7; j ++) {
                    if(howMany[j - 1] == 2) {
                        for(int k= j + 1; k < 7; k ++) {
                            if(howMany[k - 1] == 2) {
                                return 1500;
                            }
                        }
                    }
                }
            }
        }
        return (howMany[0] * 100) + (howMany[4] * 50);

    }

    // These methods toggle the checkboxes checked and unchecked when user clicks on die itself
    public void toggleD1(View view){
        if(!cBox1.isChecked()) {
            cBox1.setChecked(true);
        }else if (cBox1.isChecked() && cBox1.isEnabled()) {
            cBox1.setChecked(false);
        }
    }

    public void toggleD2(View view){
        if(!cBox2.isChecked()) {
            cBox2.setChecked(true);
        }else if (cBox2.isChecked() && cBox2.isEnabled()) {
            cBox2.setChecked(false);
        }
    }

    public void toggleD3(View view){
        if(!cBox3.isChecked()) {
            cBox3.setChecked(true);
        }else if (cBox3.isChecked() && cBox3.isEnabled()) {
            cBox3.setChecked(false);
        }
    }

    public void toggleD4(View view){
        if(!cBox4.isChecked()) {
            cBox4.setChecked(true);
        }else if (cBox4.isChecked() && cBox4.isEnabled()) {
            cBox4.setChecked(false);
        }
    }

    public void toggleD5(View view){
        if(!cBox5.isChecked()) {
            cBox5.setChecked(true);
        }else if (cBox5.isChecked() && cBox5.isEnabled()) {
            cBox5.setChecked(false);
        }
    }

    public void toggleD6(View view){
        if(!cBox6.isChecked()) {
            cBox6.setChecked(true);
        }else if (cBox6.isChecked() && cBox6.isEnabled()) {
            cBox6.setChecked(false);
        }
    }
}
