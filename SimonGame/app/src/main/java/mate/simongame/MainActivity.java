package mate.simongame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] menus={
                "Play",
                "Records",
                "Settings"
        };

        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circlemenu);
        circleMenu.setMainMenu(Color.parseColor("#505050"), R.drawable.ic_menu, R.drawable.ic_close)
                .addSubMenu(Color.parseColor("#FF3021"), R.drawable.ic_play)
                .addSubMenu(Color.parseColor("#33FF33"), R.drawable.ic_record)
                .addSubMenu(Color.parseColor("#6633FF"), R.drawable.ic_settings)
                .setOnMenuSelectedListener((index)->{
                        final int i = index;
                        Runnable r =() -> {
                            switch (i) {
                                case 0:
                                    startActivity(new Intent(getApplicationContext(), Play.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(getApplicationContext(), ShowRecords.class));
                                    break;
                                case 2:
                                    startActivity(new Intent(getApplicationContext(), Settings.class));
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "No such option", Toast.LENGTH_SHORT).show();
                            }};

                        new Handler().postDelayed(r, 1300);

                });
    }
}

