package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import static java.lang.Math.*;

/**
 * Created by Jun Li on 5/24/2015.
 */
public class Enemy extends Image{

    private final int right = 1;
    private final int velX = 2;
    private Dungeon dungeon;
    private Mario mario;
    private Rect mapRect = new Rect();
    private int dir = right;
    private int tileWidth;
    private int range = 200;
    private int count = 0;
    private boolean active = false;
    private boolean killed = false;

    public Enemy(Bitmap bitmap, Rect rect, Dungeon dungeon, Mario mario) {
        super(bitmap, rect);
        this.dungeon = dungeon;
        this.mario = mario;
        tileWidth = rect.width();
        mapRect.set(rect);
    }


    private void setActive() {
        if (Math.abs(mario.getMapRect().centerX() - rect.centerX())  < dungeon.screenWidth * 3 / 2) {
            active = true;
        }
    }

    public void setKilled(Rect rect) {
        if ((rect.centerX() / tileWidth == this.rect.centerX() / tileWidth) && (Math.abs(rect.bottom - this.rect.top) <= 10)) {
            killed = true;
        }
    }

    public boolean getKilled() {
        return killed;
    }
    public boolean marioDead(Rect rect) {
        if ((rect.centerX() / tileWidth == this.rect.centerX() / tileWidth) && (this.rect.top - rect.bottom < -10)) {
            return true;
        }
        else {
            return false;
        }

    }
    @Override
    public void drawImage(Canvas canvas) {
        setActive();
        int check;
        if (dir == right) {
            check = mapRect.right / tileWidth;
        }
        else {
            check = mapRect.left / tileWidth;
        }


        if (mario.block[rect.centerY() / tileWidth][check] != 0 || Math.abs(count) >= range) {
            dir = -dir;
        }


        int move;
        if (dir == right) {
            move = velX;
            count++;
        }
        else {
            move = -velX;
            count--;
        }

        rect.offset(move, 0);
        mapRect.offset(move, 0);
        if (active && !killed) {
            super.drawImage(canvas);
        }

    }
}
