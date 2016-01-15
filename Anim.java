package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Jun Li on 5/15/2015.
 */
public class Anim {

    //defining and initializing varaibles
    private Bitmap[] frameBitmap;
    private Rect rect;
    private int posX;
    private int posY;
    private int playIndex = 0;
    private boolean isLoop;
    private boolean isEnd = false;
    private long lastTime = 0;
    private static final int Anim_Period = 100;
    private int left_center;

    public Anim(Bitmap[] bitmaps, boolean isLoop) {
        frameBitmap = bitmaps;
        this.isLoop = isLoop;
    }

    //setting position function
    public void setPos(Rect rect) {
        this.rect = rect;
        posX = rect.right - rect.width() / 2;
        posY = rect.bottom - rect.height() / 2;
        left_center = (rect.right - rect.left)/2;
    }


    //getting methods
    public Rect getRect() {
        return rect;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }


    //move the animation (rectangle)
    public void animMove(int x, int y) {
        posX += x;
        posY += y;
        rect.offset(x, y);
    }

    //draw to screen
    public void drawFrame(Canvas canvas, int index) {
        canvas.drawBitmap(frameBitmap[index], null, rect, null);
    }

    //draw animation
    public void drawAnim(Canvas canvas) {
        if (!isEnd) {
            canvas.drawBitmap(frameBitmap[playIndex], null, rect, null);
            long time = System.currentTimeMillis();
            if (time - lastTime > Anim_Period) {
                playIndex++;
                lastTime = time;
                if (playIndex >= frameBitmap.length - 1) {
                    isEnd = true;
                    if (isLoop) {
                        isEnd = false;
                        playIndex = 0;
                    }
                }
            }
        }
    }
}
