package com.example.simonsays;

/**
 * Imported modules
 */

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class GameActivity extends Activity {

    /**
     * Private attributes for the class
     */
    private TextView header_text;
    private TextView game_level;
    private TextView move;
    private Integer game_mode;
    private String player1_name, player2_name;
    private Integer difficulty;
    private ArrayList<String> game_setting;
    private int current_level;
    private TextView time;

    /**
     * OnCreate method that will initialize the class when it is first loaded
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Have to set the attribute before setting the view layout as the CustomView code will run
         * before the intent can set the game_mode variable if its bellow the super call();
         */


        //Intent intent = getIntent();
        //game_mode = intent.getIntExtra("game_mode", 0 );

        game_setting = new ArrayList<String>();
        game_setting = getIntent().getStringArrayListExtra("game_setting");

        if(game_setting.get(0).equals("1")){
            game_mode = 1;
            player1_name = game_setting.get(1);
            difficulty = Integer.parseInt(game_setting.get(2));
        }
        else{
            game_mode = 2;
            player1_name = game_setting.get(1);
            player2_name = game_setting.get(2);

        }

        /**
         * Setting the layout to the class to the activity_main.xml file
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /**
         * importing the xml views elements to use on the class
         */

        header_text = findViewById(R.id.header_text);
        game_level = findViewById(R.id.game_level);
        move = findViewById(R.id.move);
        time = findViewById(R.id.time);

    }

    /**
     * Will receive a string from the CustomView class in order to update the information section of the class
     * @param m - passed string that will be used to update the header of the class
     */
    public void update_game_activity_header(String m) {
        header_text.setText(m);
    }

    /**
     * Will receive an int that will represent the current game level that is being played
     * @param l - the passed int that will update the current level of the game
     */
    public void update_game_activity_level(int l){
        game_level.setText("Level: " + l);
        current_level = l;
    }

    /**
     * Will be used to update the current move level of the currently active player
     * @param l - will be used to update the move textview in the activity
     */
    public void update_game_activity_move(int l){
        if(l == 1){
            move.setText("Move: " + l + "/" + current_level);
        }
        else{
            move.setText("Moves: " + l + "/" + current_level);
        }
    }

    public void update_game_timer(int t){
        time.setText("Timer: " + t);
    }

    /**
     * method that will be used by the CustomView to get the selected game_mode
     * @return
     */
    public Integer get_game_mode(){
        return game_mode;
    }

    public String get_player1_name(){
        return player1_name;
    }

    public String get_player2_name(){
        return player2_name;
    }

    public Integer get_game_difficulty(){
        return difficulty;
    }

    /**
     * method called by the CustomView to indicate that the current game has ended, this will return the original intent to the MainMenu
     * @param w
     */
    public void finish_game(int w){
        Intent result = new Intent(Intent.ACTION_VIEW);
        result.putExtra("winner", w);
        setResult(RESULT_OK, result);
        finish();
    }

    /**
     * This method is used to inflate the menu with our custom created menu resource.
     * This method is needed as the activity will make use of intents
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is
        // present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This method is needed in order to grab the selected menu option which is presses by the user.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}