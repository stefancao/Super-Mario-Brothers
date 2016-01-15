package eecs40.super_mario_brothers;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by swear_000 on 5/17/2015.
 */


//Not using this class
public class Enemy {
    private Anim[] anims;
    private boolean active = false;
    private boolean killed = false;
    private Rect rect;
    private int direction = 0;
    private int moveX = 0;
    private int moveY = 0;

    public Enemy(Anim[] anims, Rect rect) {
        this.anims = anims;
        this.rect = rect;
    }


    public void drawEnemy(Canvas canvas) {
        if (killed) {
            anims[direction].drawFrame(canvas, 0);
        }
        if (active) {
            anims[direction].animMove(moveX, moveY);
            anims[direction].drawAnim(canvas);
        }
    }
}
