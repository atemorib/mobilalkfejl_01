package mate.simongame;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;




public class Settings extends Activity {

    private RadioGroup difficulty;
    private RadioButton difficultyButton;
    private Button set;
    private final int EASY = 1500;
    private final int MEDIUM = 1000;
    private final int HARD = 500;
    public static int PREFFERED_DIFFICULTY;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        set = findViewById(R.id.submit);
        set.setOnClickListener((View v)->{
            setDifficulty();
        });


    }

    private void setDifficulty(){
        difficulty = (RadioGroup) findViewById(R.id.difficulty);
        int id = difficulty.getCheckedRadioButtonId();
        difficultyButton = (RadioButton) findViewById(id);
        if(difficultyButton!=null){
            Toast.makeText(this,
                    difficultyButton.getText()+" difficulty has been set. You can play now!", Toast.LENGTH_SHORT).show();
            if(difficultyButton.getText().equals("Easy")){
                PREFFERED_DIFFICULTY=EASY;
            }
            if(difficultyButton.getText().equals("Hard")){
                PREFFERED_DIFFICULTY=HARD;
            }
            if(difficultyButton.getText().equals("Medium")){
                PREFFERED_DIFFICULTY=MEDIUM;
            }

        }

    }


}
