package eecs40.super_mario_brothers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity{
    private Dungeon dungeon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        //setContentView(new Level_1(this));
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                int level;
                intent.setClass(MainActivity.this, SecondActivity.class);
                switch (v.getId()) {
                    case R.id.button1:
                        level = 1;
                        break;
                    case R.id.button2:
                        level = 2;
                        break;
                    case R.id.button3:
                        level = 3;
                        break;
                    default:
                        level = 1;
                        break;
                }
                intent.putExtra("eecs40.super_mario_brothers.Level", level);
                startActivity(intent);
                finish();
               }
        });
    }
}
