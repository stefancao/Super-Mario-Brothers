package eecs40.super_mario_brothers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


public class SecondActivity extends Activity {
    private Dungeon dungeon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        int level = getIntent().getIntExtra("eecs40.super_mario_brothers.Level", 1);

        switch (level) {
            case 1:
                dungeon = new Level_1(this);
                break;
            case 2:
                dungeon = new Level_2(this);
                break;
            case 3:
                dungeon = new Level_3(this);
                break;
            default:
                dungeon = new Level_1(this);
                break;
        }
        setContentView(dungeon);
    }
}

