package com.example.simonsays;

// imports
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class that extends the view, this is the custom view that will hold the canvas for the game and is linked to the activity_game xml
 */
// class definition
public class CustomView extends View {

    // private fields that are necessary for rendering the view
    // the colours of our squares
    private Paint red, green, blue, yellow;
    private Paint d_red, d_green, d_blue, d_yellow;
    private Rect square; // the square itself
    private int first; // the first touch to be rendered
    private float firstX;
    private float firstY;
    private boolean touch = false; // do we have at least on touch
    private int square_measure;
    private ArrayList<Integer> array;
    private boolean simon = true;
    private boolean vs_simon = true;
    private boolean player1 = false;
    private boolean player2 = false;
    private boolean record_touch = false;
    private boolean finished = false;
    private CountDownTimer timer;
    private Toast message;
    private Boolean timer_is_running = false;
    private int level = 0;
    private int current_turn_pointer = 0;
    private GameActivity refToGameActivity;
    private Integer game_mode;
    private String player_1_name, player_2_name;
    private Integer diffi;
    private CountDownTimer turn_timer;
    private int counter = 10;

    // default constructor for the class that takes in a context
    public CustomView(Context c) {
        super(c);
        init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public CustomView(Context c, AttributeSet as) {
        super(c, as);
        init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certian way
    public CustomView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    /**
     * refactored init method as most of this code is shared by all the
     * constructors
     */
    private void init() {
        // create the paint objects for rendering our rectangles
        red = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        d_red = new Paint(Paint.ANTI_ALIAS_FLAG);
        d_green = new Paint(Paint.ANTI_ALIAS_FLAG);
        d_blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        d_yellow = new Paint(Paint.ANTI_ALIAS_FLAG);
        // setting the colors for the Paint objects
        red.setColor(0xFFFF0000);
        green.setColor(0xFF00FF00);
        blue.setColor(0xFF0000FF);
        yellow.setColor(0xFFFFFF00);
        d_red.setColor(0xFFcc2900);
        d_green.setColor(0xFF00cc00);
        d_blue.setColor(0xFF0052cc);
        d_yellow.setColor(0xFFe6e600);

        // initialise the rectangle
        square = new Rect(-360, -360, 360, 360);

        // initializing the array that will hold the sequence for the game
        array = new ArrayList<Integer>();

        // initializing a toast for any game notifications
        message = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);

        /* initializing the timer that will be used per turn*/
        timer = new CountDownTimer(10000,1000) {

            public void onTick(long millisUntilFinished) {
                set_game_timer(counter);
                counter--;
            }

            public void onFinish() {
                if((player1 == true) && (simon == true)){
                    finish_game(0);
                }
                else if((player1 == true) && (simon == false)){
                    finish_game(2);
                }
                else if(player2 == true){
                    finish_game(1);
                }
            }
        };

        // getting the context of the game_mode, player1 name, player2 name and difficulty from the GameActivity
        refToGameActivity = (GameActivity) getContext();
        game_mode = refToGameActivity.get_game_mode();



        // based on the returned value, it will either be a SvP game or a PvP game
        if(game_mode.equals(1)){
            player_1_name = refToGameActivity.get_player1_name();
            diffi = refToGameActivity.get_game_difficulty();
            simon = true;
            player1 = false;
            record_touch = false;
        }
        else if(game_mode.equals(2)){
            player_1_name = refToGameActivity.get_player1_name();
            player_2_name = refToGameActivity.get_player2_name();
            simon = false;
            player1 = true;
            record_touch = true;
        }

    }

    // public method that needs to be overridden to draw the contents of this
    // widget
    public void onDraw(Canvas canvas) {
        // call the superclass method
        super.onDraw(canvas);

        // if there is no touches then just draw a single blue square in the last place
        if (!touch) {
            //top left square
            canvas.save();
            canvas.translate(square_measure, square_measure);
            canvas.drawRect(square, blue);
            canvas.restore();

            //top right square
            canvas.save();
            canvas.translate(square_measure * 3, square_measure);
            canvas.drawRect(square, red);
            canvas.restore();

            //bottom left square
            canvas.save();
            canvas.translate(square_measure, square_measure * 3);
            canvas.drawRect(square, green);
            canvas.restore();


            // bottom right square
            canvas.save();
            canvas.translate(square_measure * 3, square_measure * 3);
            canvas.drawRect(square, yellow);
            canvas.restore();
        } else if (touch) {
            if ((firstX <= square_measure * 2) && (firstY <= square_measure * 2)) {
                //top left square
                canvas.save();
                canvas.translate(square_measure, square_measure);
                canvas.drawRect(square, d_blue);
                canvas.restore();

                //top right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure);
                canvas.drawRect(square, red);
                canvas.restore();

                //bottom left square
                canvas.save();
                canvas.translate(square_measure, square_measure * 3);
                canvas.drawRect(square, green);
                canvas.restore();


                // bottom right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure * 3);
                canvas.drawRect(square, yellow);
                canvas.restore();
            } else if ((firstX >= square_measure * 2) && (firstY <= square_measure * 2)) {
                //top left square
                canvas.save();
                canvas.translate(square_measure, square_measure);
                canvas.drawRect(square, blue);
                canvas.restore();

                //top right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure);
                canvas.drawRect(square, d_red);
                canvas.restore();

                //bottom left square
                canvas.save();
                canvas.translate(square_measure, square_measure * 3);
                canvas.drawRect(square, green);
                canvas.restore();


                // bottom right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure * 3);
                canvas.drawRect(square, yellow);
                canvas.restore();
            } else if ((firstX <= square_measure * 2) && (firstY >= square_measure * 2)) {
                //top left square
                canvas.save();
                canvas.translate(square_measure, square_measure);
                canvas.drawRect(square, blue);
                canvas.restore();

                //top right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure);
                canvas.drawRect(square, red);
                canvas.restore();

                //bottom left square
                canvas.save();
                canvas.translate(square_measure, square_measure * 3);
                canvas.drawRect(square, d_green);
                canvas.restore();


                // bottom right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure * 3);
                canvas.drawRect(square, yellow);
                canvas.restore();
            } else {
                //top left square
                canvas.save();
                canvas.translate(square_measure, square_measure);
                canvas.drawRect(square, blue);
                canvas.restore();

                //top right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure);
                canvas.drawRect(square, red);
                canvas.restore();

                //bottom left square
                canvas.save();
                canvas.translate(square_measure, square_measure * 3);
                canvas.drawRect(square, green);
                canvas.restore();

                // bottom right square
                canvas.save();
                canvas.translate(square_measure * 3, square_measure * 3);
                canvas.drawRect(square, d_yellow);
                canvas.restore();
            }
        }
    }

    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {

        if (record_touch == true) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // this indicates that the user has placed the first finger on the
                // screen what we will do here is enable the pointer, track its location
                // and indicate that the user is touching the screen right now
                // we also take a copy of the pointer id as the initial pointer for this
                // touch
                int pointer_id = event.getPointerId(event.getActionIndex());
                touch = true;
                first = pointer_id;
                firstX = event.getX(pointer_id);
                firstY = event.getY(pointer_id);

                // if the player1 variable is set then call player_1
                if (player1 == true) {
                    player_1(firstX, firstY);
                }
                // if the player2 variable is set then call player_2
                else if (player2 == true) {
                    player_2(firstX, firstY);
                }
                // force a redraw
                invalidate();
                return true;

            }
            // if the player removes the touch
            else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                // this indicates that the user has removed the last finger from the
                // screen and has ended all touch events. here we just disable the
                // last touch.
                int pointer_id = event.getPointerId(event.getActionIndex());
                first = pointer_id;
                firstX = 0;
                firstX = 0;
                touch = false;
                invalidate();
                return true;
            }

        } else {
            // if touch is not allowed, call simon();
            simon();
            // notify that its the users turn
            Toast.makeText(this.getContext(), "Tap to start " + player_1_name +" turn", Toast.LENGTH_SHORT).show();
            // force a redraw
            invalidate();

        }

        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead
        return super.onTouchEvent(event);

    }

    /**
     * Method that  is used to draw the canvas in a sqaure, the square_measure will hold a quarter of the size, this will be used for
     * calculations in other methods
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) {
            size = height;
        } else {
            size = width;
        }

        square_measure = size / 4;

        // setting the canvas size to a sqaure
        setMeasuredDimension(size, size);
    }

    /**
     * timer used to light the panels of the game
     */
    private void startTimer() {

        timer_is_running = true;

        timer = new CountDownTimer((int) diffi,1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                timer_is_running = false;
                touch = false;
                invalidate();
            }
        }.start();
    }

    /**
     * method used to set the header of the GameActivity, it will take in a string.
     * The change will occur due to generating a context of the GameActivity anf passing the value t the appropriate method
     * @param s
     */
    public void set_game_activity_header(String s) {
        GameActivity md = (GameActivity) this.getContext(); //make a variable to hold a reference to the MainActivity which holds this CustomView
        String message = s;
        md.update_game_activity_header(message); //call the updateMainActivity() which you will have to write in the MainActivity
    }

    /**
     * method used to set the level of the GameActivity, it will take in an int.
     * The change will occur due to generating a context of the GameActivity anf passing the value t the appropriate method
     * @param l - current level of the game that passed
     */
    public void set_game_activity_level(int l) {
        GameActivity md = (GameActivity) this.getContext(); //make a variable to hold a reference to the MainActivity which holds this CustomView
        int message = l;
        md.update_game_activity_level(message); //call the updateMainActivity() which you will have to write in the MainActivity
    }

    /**
     * method used to set the move total of the GameActivity, it will take in an int.
     * The change will occur due to generating a context of the GameActivity anf passing the value t the appropriate method
     * @param l
     */
    public void set_game_activity_move(int l) {
        GameActivity md = (GameActivity) this.getContext(); //make a variable to hold a reference to the MainActivity which holds this CustomView
        int message = l;
        md.update_game_activity_move(message); //call the updateMainActivity() which you will have to write in the MainActivity
    }

    public void set_game_timer(int t){
        GameActivity md = (GameActivity) this.getContext();
        int message = t;
        md.update_game_timer(message);
    }

    /**
     * Method called to check current input of the user with the current point in the array sequence. It will take in an int that will be the user current input,
     * that input will be tested against the array sequence position
     * @param q - passed current input by the user
     * @return
     */
    public Boolean check_array(int q) {
        Integer quad = (Integer) q;
        if (quad.equals(array.get(current_turn_pointer))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that is called when it is simon's turn in the Player vs Simon mode
     */
    public void simon() {
        //timer.cancel();
        //counter =10;
        // set the header
        set_game_activity_header("Simon is currently playing");
        // increase the level
        level++;
        // set the new level
        set_game_activity_level(level);
        // force a redraw
        invalidate();
        // generate a random number
        int temp = new Random().nextInt((4 - 1) + 1) + 1;
        // add to the current array
        array.add(temp);
        // loop through the array sequence and light up the panels
        for (Integer num : array) {
            touch = true;
            if (num.equals(1)) {
                firstX = square_measure;
                firstY = square_measure;
            } else if (num.equals(2)) {
                firstX = square_measure * 3;
                firstY = square_measure;
            } else if (num.equals(3)) {
                firstX = square_measure;
                firstY = square_measure * 3;
            } else {
                firstX = square_measure * 3;
                firstY = square_measure * 3;
            }
            invalidate();
            startTimer();
        }
        /*-------------------------------------------------------------------------------------------------------------------*/
        /* I tried to add the thread to run through the current array and light up the panels sequentially*/
        /*final Handler handler = new Handler();  // create a handler to link to our runnable
        handler.post(new Runnable() {           // create the runnable
            @Override
            public void run() {
                for(Integer num : array){
                    if (num.equals(1)) {
                        firstX = square_measure;
                        firstY = square_measure;
                    } else if (num.equals(2)) {
                        firstX = square_measure * 3;
                        firstY = square_measure;
                    } else if (num.equals(3)) {
                        firstX = square_measure;
                        firstY = square_measure * 3;
                    } else {
                        firstX = square_measure * 3;
                        firstY = square_measure * 3;
                    }
                    touch = true;
                    handler.postDelayed(this, 5000); // execute this runnable again
                    invalidate();
                    touch = false;
                    handler.postDelayed(this, 500); // execute this runnable again
                    invalidate();
                }
            }
        });*/
        /*-------------------------------------------------------------------------------------------------------------------*/
        // set player1 to true to trigger the player turn
        player1 = true;
        // allow for touch
        record_touch = true;
        // force redraw
        invalidate();
        //timer.start();
    }

    /**
     * method called when its the player 1's turn. The methed will check if the array is empty, then add user touch.
     * If !empty, then check passed touch and increment pointer. If pointer is equal to level then add to array if Pvp.
     * @param x - player's x coordinate
     * @param y - player's y coordinate
     */
    public void player_1(float x, float y) {
        // set the header
        if(simon == true){
            set_game_activity_header( player_1_name + "'s turn");
        }
        // if user has not reached turn limit
        if ((current_turn_pointer < level) && array.size() > 0) {
            set_game_activity_move((current_turn_pointer+1));
            // variable to check if current user input is correct
            boolean is_correct = true;
            // 1st quadrant
            if ((x < (square_measure * 2)) && (y < (square_measure * 2))) {
                is_correct = check_array(1);
            }
            // 2nd quadrant
            else if ((x > (square_measure * 2)) && (y < (square_measure * 2))) {
                is_correct = check_array(2);
            }
            // 3rd quadrant
            else if ((x < (square_measure * 2)) && (y > (square_measure * 2))) {
                is_correct = check_array(3);
            }
            // 4th quadrant
            else {
                is_correct = check_array(4);
            }

            // if !correct and Player vs Simon game, player lost to simon
            if ((is_correct == false) && (simon == true)) {
                finish_game(0);
            }
            // player 1 lost to player 2
            else if ((is_correct == false) && (simon == false)) {
                finish_game(2);
            }

            // if player has fully matched Simon's sequence, give turn back to simon
            if ((current_turn_pointer == (level - 1)) && (simon == true)) {
                current_turn_pointer = 0;
                record_touch = false;
                invalidate();
            }
            // else increment the current pointer
            else {
                current_turn_pointer++;
            }

        }

        // if its player 1's turn when playing player vs player and needs to add the last input to the array
        else if ((current_turn_pointer == level) && (simon == false) && (array.size() > 0)) {
            players_add_to_array(x, y);

        }
        // if its a player vs player, player 1 only needs to add to the array on the first move
        else{
            players_add_to_array(x, y);
        }
    }

    /**
     * Method called when its player 2 turn in the PLayer vs player. This method will check if the player input is equal to the sequence. If player 2 reaches the
     * end of the sequence then the player will add to the array and change over to player 1
     * @param x - player 2's current x coordinate
     * @param y - player 2's current y coordinate
     */
    public void player_2 ( float x, float y){
            // if player 2 has not reached the end of the current sequence
            if (current_turn_pointer < level) {
                set_game_activity_move((current_turn_pointer+1));
                // variable to check if the current input is correct
                boolean is_correct = true;
                // 1st quadrant
                if ((x < (square_measure * 2)) && (y < (square_measure * 2))) {
                    is_correct = check_array(1);
                }
                // 2nd quadrant
                else if ((x > (square_measure * 2)) && (y < (square_measure * 2))) {
                    is_correct = check_array(2);
                }
                // 3rd quadrant
                else if ((x < (square_measure * 2)) && (y > (square_measure * 2))) {
                    is_correct = check_array(3);
                }
                // 4th quadrant
                else {
                    is_correct = check_array(4);
                }

                // if the current input is incorrect, then player 1 wins
                if((is_correct == false) && (simon == false)) {
                    finish_game(1);
                }
                // if true then increment the counter
                else if((is_correct == true) && (simon == false)) {
                    current_turn_pointer++;
                }

            }
            // if its player 1's turn when playing player vs player and needs to add the last input to the array
            else if (current_turn_pointer == level) {
                players_add_to_array(x, y);
            }
    }

    /**
     * method called when the game has eneded, it will take in an int to indicate which player won the game(Simon, Player 1, Player 2)
     * @param w
     */
    public void finish_game (int w){
        //clear the array
        array.clear();
        // reset pointer
        current_turn_pointer = 0;
        // send the winner's number to the GameActivity to the finish_game method in the GameActivity
        this.refToGameActivity.finish_game(w);
    }

    /**
     * <ethod called when either player 1 or player 2 needs to add to the game sequence
      * @param x - current x coordinate of player
     * @param  y - current y coordinate of player
     */
    public void players_add_to_array ( float x, float y){
            // 1st quadrant
            if ((x < (square_measure * 2)) && (y < (square_measure * 2))) {
                array.add(1);
            }
            // 2nd quadrant
            else if ((x > (square_measure * 2)) && (y < (square_measure * 2))) {
                array.add(2);
            }
            // 3rd quadrant
            else if ((x < (square_measure * 2)) && (y > (square_measure * 2))) {
                array.add(3);
            }
            // 4th quadrant
            else {
                array.add(4);
            }

            // if its currently player 1, then switch to player 2
            if(player1 == true){
                // set the header
                set_game_activity_header(player_2_name + "'s turn");
                timer.cancel();
                counter =10;
                invalidate();
                current_turn_pointer = 0;
                player2 = true;
                player1 = false;
                level++;
                set_game_activity_level(level);
                Toast.makeText(this.getContext(), "Tap to start " + player_2_name + "'s turn", Toast.LENGTH_SHORT).show();
                timer.start();
                invalidate();
            }
            // if its currently player 2, then switch to player 1
            else if (player2 == true){
                // set the header
                set_game_activity_header(player_1_name + "'s turn");
                timer.cancel();
                counter =10;
                invalidate();
                current_turn_pointer = 0;
                player2 = false;
                player1 = true;
                level++;
                set_game_activity_level(level);
                Toast.makeText(this.getContext(), "Tap to start " + player_1_name + "'s turn", Toast.LENGTH_SHORT).show();
                timer.start();
                invalidate();
            }

            // update the move view to the current array size
            //set_game_activity_move(array.size());
            // force a redraw
            invalidate();
    }

}
