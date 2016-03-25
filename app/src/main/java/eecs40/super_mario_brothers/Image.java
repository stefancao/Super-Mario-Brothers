package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Jun Li on 5/24/2015.
 */
public class Image {
    protected Bitmap bitmap;
    protected Rect rect = new Rect();
    protected Paint paint = new Paint();
    protected int objX, objY;
    protected int mapX = 0;

    public Image(Bitmap bitmap, Rect rect) {
        this.bitmap = bitmap;
        this.rect.set(rect);
    }

    public void setPos(Rect rect) {
        this.rect.set(rect);
    }

    public void setIndex(int x, int y) {
        objX = x;
        objY = y;
    }

    public int getObjX() {
        return objX;
    }

    public int getObjY() {
        return objY;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setScreen(int mapX) {
        rect.offset(-this.mapX + mapX, 0);
        this.mapX = mapX;
    }

    public Rect getRect() {
        return rect;
    }

    public boolean isClick(int x, int y) {
        return rect.contains(x, y);
    }

    public void drawImage(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, rect, paint);
    }
}
