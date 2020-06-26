package com.covenslayer.farkle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// This activity allows users to enter players names by clicking add player button and typing in the name
// in the newly created editText.
public class PlayGame extends AppCompatActivity {

    private LinearLayout mLayout;
    private EditText mEditText;
    private Button mButton;
    private int counter;
    Map<String, Integer> idMap = new HashMap<>();
    ArrayList<String> extras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        counter = 0;
        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mEditText = (EditText) findViewById(R.id.name1);
        mButton = (Button) findViewById(R.id.btnAddPlayer);
        TextView textView = new TextView(this);
        textView.setText("New text");
    }

    // User clicked add player button so create new EditText for user to enter name
    public void onClick(View v) {
        counter++;
        mLayout.addView(createNewTextView(mEditText.getText().toString()));
    }

    private EditText createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText editView = new EditText(this);
        editView.setLayoutParams(lparams);
        editView.setHint("Name");
        editView.setId(counter);
        editView.setFocusableInTouchMode(true);
        editView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editView, InputMethodManager.SHOW_IMPLICIT);
        String name = "name" + counter;
        idMap.put(name, counter);
        return editView;
    }

    // User clicked options button so get user names from EditTexts and start options activity,
    // sending it the ArrayList<String> of player names
    public void options(View view)
    {
        for (int i = 0; i < counter; i++) {
            String item = "name" + (i + 1);
            EditText names = (EditText) findViewById(idMap.get(item));
            names.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.winnersFont));
            names.setTextColor(Color.parseColor("#000000"));
            extras.add(names.getText().toString());
        }
        Intent intent = new Intent(this, GameOptions.class);
        intent.putStringArrayListExtra("extras", extras);
        startActivity(intent);
    }

    // User skips the options screen and takes the defsult options
    // index 0 = "10,000" which is the default for ending score option
    // index 1 = "No" which is default for 3 farkles option
    // index 2 = "250" which is default score for break in value option
    // index 3 = name of first player
    // index 4 = total score of first player, initially set to 0
    // repeat index 3 and 4 for every other player
    // index last = keeps track of turn number so we know who's turn it is
    // This ArrayList<String> is sent to activity to actually start the game
    public void start(View view)
    {
        extras.add("10,000");
        extras.add("No");
        extras.add("250");
        for (int i = 0; i < counter; i++) {
            String item = "name" + (i + 1);
            EditText names = (EditText) findViewById(idMap.get(item));
            extras.add(names.getText().toString());
            extras.add("0");
        }
        extras.add("T1");
        Intent intent = new Intent(this, StartGame.class);
        intent.putStringArrayListExtra("extras", extras);
        startActivity(intent);
    }
}
