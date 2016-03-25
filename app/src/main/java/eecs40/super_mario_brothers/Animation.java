package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Jun Li on 5/24/2015.
 */
public class Animation {
    private Bitmap[] frameBitmap;
    private Rect rect = new Rect();
    private int playIndex = 0;
    private long lastTime = 0;
    private static final int Anim_Period = 100;

    public Animation(Bitmap[] bitmaps) {
        frameBitmap = bitmaps;
    }

    public void setPos(Rect rect) {
        this.rect.set(rect);
    }

    public void drawFrame(Canvas canvas, int index) {
        Paint paint = new Paint();
        canvas.drawBitmap(frameBitmap[index], null, rect, paint);
    }

    public void drawAnim(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawBitmap(frameBitmap[playIndex], null, rect, paint);
        long time = System.currentTimeMillis();
        if (time - lastTime > Anim_Period) {
            playIndex++;
            lastTime = time;
            if (playIndex >= frameBitmap.length - 1) {
                playIndex = 0;
            }
        }
    }
}
