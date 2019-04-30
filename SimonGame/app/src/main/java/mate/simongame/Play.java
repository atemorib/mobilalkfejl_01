package mate.simongame;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Play extends AppCompatActivity implements View.OnClickListener {


    private final Random random = new Random();
    private ImageButton green;
    private ImageButton red;
    private ImageButton blue;
    private ImageButton yellow;
    private TextView score, levelProgress;
    private ProgressBar progressBar;
    private final List<Integer> sequence = new ArrayList<>();
    public static final List<Integer> topFourScore = new ArrayList<>(4);
    private int currentIndex = 0;
    private int guessed;
    private final Handler handler = new Handler();
    private SoundPool soundPool;
    private int greenSound, redSound, blueSound, yellowSound, gameOverSound;

    private String s;

    public static final String SHARED_PREF ="sharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Progressbar, textfield and imagebuttons
        setContentView(R.layout.activity_play);
        this.levelProgress = (TextView) findViewById(R.id.progress_text);
        s = (currentIndex)+"/"+(sequence.size()+1);
        levelProgress.setText(s);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.score = (TextView) findViewById(R.id.score);
        this.score.setText(String.valueOf(sequence.size()));
        this.green = (ImageButton) findViewById(R.id.green);
        this.red = (ImageButton) findViewById(R.id.red);
        this.blue = (ImageButton) findViewById(R.id.blue);
        this.yellow = (ImageButton) findViewById(R.id.yellow);

        //Build sound
        buildSound();
        greenSound = soundPool.load(this, R.raw.doo, 1);
        redSound = soundPool.load(this, R.raw.re, 1);
        blueSound = soundPool.load(this, R.raw.mi, 1);
        yellowSound = soundPool.load(this, R.raw.fa, 1);
        gameOverSound = soundPool.load(this, R.raw.lata, 1);


        //Imagebuttons func
        green.setOnClickListener(this);
        red.setOnClickListener(this);
        blue.setOnClickListener(this);
        yellow.setOnClickListener(this);
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);
        blue.setEnabled(false);

        Runnable r = ()->{
            startGame();
        };
        handler.postDelayed(r, 2000);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.green: guessed=0;
                //Toast.makeText(this, "GREEN", Toast.LENGTH_SHORT).show();
                animation(green);
                playSound(green);
             break;
            case R.id.red: guessed=1;
                //Toast.makeText(this, "RED", Toast.LENGTH_SHORT).show();
                animation(red);
                playSound(red);
                break;
            case R.id.blue: guessed=2;
                //Toast.makeText(this, "BlUE", Toast.LENGTH_SHORT).show();
                animation(blue);
                playSound(blue);
                break;
            case R.id.yellow: guessed=3;
                //Toast.makeText(this, "YELLOW", Toast.LENGTH_SHORT).show();
                animation(yellow);
                playSound(yellow);
                break;

        }

        if(sequence.get(currentIndex) != guessed){
            gameOver();
            return;
        }
        //animation
        System.out.println("SUCCESS");
        currentIndex++;
        progressBar.setMax(sequence.size());
        progressBar.setProgress(currentIndex);
        s = (currentIndex)+"/"+(sequence.size());
        levelProgress.setText(s);

        if(currentIndex == sequence.size()){
            currentIndex=0;
            System.out.println("DISABLED!!!");
            disableButtons();

            this.score.setText(String.valueOf(sequence.size()));
            //record update maybe
            final Runnable r= ()->{
                nextRound();
            };
            handler.postDelayed(r, 2000);

        }




    }

    private void buildSound(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build();
        }
        else{
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
    }


    private void playSound(View v){
        switch (v.getId()){
            case R.id.green: soundPool.play(greenSound, 1,1,0,0,1); break;
            case R.id.red: soundPool.play(redSound, 1,1,0,0,1); break;
            case R.id.blue: soundPool.play(blueSound, 1,1,0,0,1); break;
            case R.id.yellow: soundPool.play(yellowSound, 1,1,0,0,1); break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    private void animation(ImageButton imageButton) {
        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_alpha);
        imageButton.startAnimation(alpha);
    }

    private void disableButtons(){
        green.setEnabled(false);
        red.setEnabled(false);
        yellow.setEnabled(false);
        blue.setEnabled(false);
    }



    private void startGame(){
        currentIndex=0;
        sequence.clear();
        nextRound();
        //displayImlp();
    }

    private void nextRound(){
        //TODO:highscore lekezelése

        sequence.add(random.nextInt(4));
        System.out.println("SEUQNCEEEEEEEEEEEEEEEEEEEEEEEEEEE: "+sequence);
        displaySequence(currentIndex);

    }

    private void displaySequence(int index){
        if(index >= sequence.size()){
            progressBar.setMax(sequence.size());
            progressBar.setProgress(currentIndex);
            s = (currentIndex)+"/"+(sequence.size());
            levelProgress.setText(s);
            playerTurn();
        }
        else {

            Runnable r = () -> {
                displayImlp(index);
            };
            new Handler().postDelayed(r, Settings.PREFFERED_DIFFICULTY==0?1500:Settings.PREFFERED_DIFFICULTY);
        }
    }


    private void displayImlp(int index) {

        System.out.println("DONE");
        switch (sequence.get(index)) {
            case 0:

                animation(green);
                playSound(green);
                break;
            case 1:

                animation(red);
                playSound(red);
                break;
            case 2:

                animation(blue);
                playSound(blue);
                break;
            case 3:
                animation(yellow);
                playSound(yellow);
                break;
            default:
                throw new Error();
        }
      displaySequence(++index);


    }
    public int randomInt(){
        return random.nextInt(10);
    }

    private void playerTurn(){

        green.setEnabled(true);
        red.setEnabled(true);
        yellow.setEnabled(true);
        blue.setEnabled(true);


    }

    private void playAgain(){
        progressBar.setMax(1);
        progressBar.setProgress(0);
        levelProgress.setText("0/1");
        score.setText("0");

        startGame();
    }

    private void gameOver(){
        soundPool.play(gameOverSound, 1,1,1, 0,1);
        System.out.println("GAME OVER!");

        //STORE HIGH SCORES
        setSharedPref(sequence.size()-1);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Play.this);
        alertDialogBuilder.setMessage("Game over!\nYou reached level: " + (sequence.size() -1) + "\nDo you want to play agian?");
        alertDialogBuilder.setPositiveButton("Yes",
                (DialogInterface arg0, int arg1)->{
                    playAgain();

                });

        alertDialogBuilder.setNegativeButton("No",
                (DialogInterface dialog, int which)->{

                finish();

        });
        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private List<Integer> setHighScoreArray(int score){
        if(topFourScore.size()==4){
            if(score > Collections.min(topFourScore)){
                if(!topFourScore.contains(score)) {
                    topFourScore.remove(Collections.min(topFourScore));
                    topFourScore.add(score);
                }
            }
        }
        if(topFourScore.size()<4){
            if(!topFourScore.contains(score)) {
                topFourScore.add(score);
            }
        }
        Collections.sort(topFourScore);
        return topFourScore;
    }

    private void setSharedPref(int score){
        SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        String s;
        List<Integer> scores = setHighScoreArray(score);
        System.out.println(scores);
        try{
            JSONArray jsonArray = new JSONArray();
            for(int i: scores)
                jsonArray.put(i);
            JSONObject json = new JSONObject();
            json.put(SHARED_PREF, jsonArray);
            s= json.toString();
        }catch (JSONException e){
            System.out.println("HIBA");
            s="";
        }
        prefEditor.putString(SHARED_PREF, s);
        prefEditor.apply();
    }

    //Neccesary. Othervise: if we press back button while the display animation is going on, the program would crash
    //due to NullPointer exception, because the animation would go on but wouldnt find the SoundPool or the ImageButton.
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


}
