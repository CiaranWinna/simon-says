package com.example.simonsays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Main Menu that extends the AppCompatActivity
 */
public class MainMenu extends AppCompatActivity {
    private TextView welcome_text;
    private Button p1_button;
    private Button p2_button;
    private String p1_name, p2_name;
    private int game_mode;
    private ArrayList<String> game_setting;
    private String diff_array [];

    /**
     * Method called when the class is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * calling the super and setting the view to the menu_main.xml
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);

        /**
         * retrieving the resources from the xml
         */
        welcome_text = findViewById(R.id.welcome_text);
        p1_button = findViewById(R.id.player_vs_simon_btn);
        p2_button = findViewById(R.id.player_vs_player_btn);
        game_setting = new ArrayList<String>();
        diff_array = new String[3];
        diff_array [0] = "Easy";
        diff_array [1] = "Medium";
        diff_array [2] = "Hard";

        // initialise the count to zero
        /**
         * setting the mode of the game
         */
        game_mode = 0;

        /**
         * Setting onCLick listeners to the buttons to send the game mode to the GameActivity via an intent
         */
        // add a listener to the button that will launch the second activity and
        // will attach the count to the intent
        p1_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                game_setting.add("1");
                game_mode = 1;
                add_to_game_setting_p1_name();
            }
        });

        /**
         * Setting onCLick listeners to the buttons to send the game mode to the GameActivity via an intent
         */
        p2_button.setOnClickListener(new View.OnClickListener() {
            // overridden method to handle the button click
            public void onClick(View v) {
                game_setting.add("2");
                game_mode = 2;
                add_to_game_setting_p1_name();
            }
        });

    }

    /**
     * Method called when the intent is returned from the GameActivirty
     * @param request - checking if its a request 10
     * @param result - checking if the result is RESULT_OK
     * @param data - passed the data sent from the GameActivity
     */
    // overridden method that will be called whenever an intent has been returned from
    // an activity that was started by this activity.
    protected void onActivityResult(int request, int result, Intent data) {
        // check the request code for the intent and if the result was ok. if both
        // are good then take a copy of the updated count variable
        if(request == 16 && result == RESULT_OK) {
            game_mode = data.getIntExtra("winner", 0);
            Log.i("MainActivity", "Winner is " + game_mode);
            if (game_mode == 0){
                Toast.makeText(this, "Simon Won!!", Toast.LENGTH_LONG).show();
            }
            else if(game_mode == 1){
                Toast.makeText(this, game_setting.get(1) + " Won!!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, game_setting.get(2) + " won!!", Toast.LENGTH_LONG).show();
            }
        }
        game_setting.clear();
    }

    public void add_to_game_setting_p1_name(){
            // we need a builder to create the dialog for us
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // set the title on this dialog
            if(game_mode == 1){
                builder.setTitle("Player vs Simon:");
            }
            else {
                builder.setTitle("Player vs Player:");
            }

            // it is possible to define your own layouts on a dialog but because we only need
            // a single edit text we
            // will create it and add it here
            final EditText et = new EditText(this);
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            if(game_mode == 1){
                et.setHint("Player name");
            }
            else {
                et.setHint("Player 1's name");
            }
            builder.setView(et);

            // add in the positive button
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    game_setting.add(et.getText().toString());
                    if(game_mode == 1){
                        add_to_game_setting_p1_difficulty();
                    }
                    else{
                        add_to_game_setting_p2_name();
                    }
                }
            });

            // add in the negative button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    game_setting.clear();
                }
            });

            // create the dialog and display it
            AlertDialog dialog = builder.create();
            dialog.show();
    }

    public void add_to_game_setting_p2_name(){
        // we need a builder to create the dialog for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set the title on this dialog
        builder.setTitle("Player vs Player:");

        // it is possible to define your own layouts on a dialog but because we only need
        // a single edit text we
        // will create it and add it here
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT);
        et.setHint("Player 2's name");
        builder.setView(et);

        // add in the positive button
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game_setting.add(et.getText().toString());
                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                intent.putExtra("game_setting", game_setting);
                startActivityForResult(intent, 16);
            }
        });

        // add in the negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game_setting.clear();
            }
        });

        // create the dialog and display it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void add_to_game_setting_p1_difficulty(){
        // we need a builder to create the dialog for us
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set the title and the list of choices
        builder.setTitle("Pick a difficulty:");
        builder.setItems(this.diff_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do a different thing depending on the choice
                if(which == 0)
                    game_setting.add("1500");
                else if(which == 1)
                    game_setting.add("1000");
                else if(which == 2)
                    game_setting.add("500");

                Intent intent = new Intent(MainMenu.this, GameActivity.class);
                intent.putStringArrayListExtra("game_setting", game_setting);
                startActivityForResult(intent, 16);
            }
        });

        // add in the negative button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                game_setting.clear();
            }
        });

        // create the dialog and display it
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
