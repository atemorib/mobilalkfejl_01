package mate.simongame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowRecords extends Activity {

    private int[] records;
    private TextView top1, top2, top3, top4;
    private TextView[] recordTextViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_records);
        top1 = findViewById(R.id.top1);
        top2 = findViewById(R.id.top2);
        top3 = findViewById(R.id.top3);
        top4 = findViewById(R.id.top4);
        recordTextViews = new TextView[]{top1, top2, top3, top4};
        records = getSharedPref();
        setRecordText();

    }

    private void setRecordText(){
        for(int i = records.length-1, j=0; i>=0; i--, j++){
            System.out.println(records[i]);
            System.out.println(recordTextViews[j]);
            String con=(j+1)+"."+" score: "+records[i]+" points";
            recordTextViews[j].setText(con);

        }
    }

    public int[] getSharedPref(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String s = pref.getString(Play.SHARED_PREF, "");


        try{
            JSONObject json = new JSONObject(new JSONTokener(s));
            JSONArray jsonArray = json.getJSONArray(Play.SHARED_PREF);
            int[] result = new int[jsonArray.length()];

            for(int i=0; i<jsonArray.length(); i++){
                result[i] = jsonArray.getInt(i);


            }

            return result;

        }catch(JSONException e){

            return null;
        }
    }


}
