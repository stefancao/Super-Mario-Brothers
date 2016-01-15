package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by swear_000 on 5/15/2015.
 */
public class Image {
    private Bitmap image;
    private int posX;
    private int posY;
    private boolean flag = true;
    private Rect rect;
    private boolean life = false;   //false = mushroomup true = mushroomlife

    private int init_x;

    private boolean question_block_flag = false;


    public Image(Bitmap bitmap, Rect rect) {
        image = bitmap;
        this.rect = rect;
        posX = rect.left + rect.width() / 2;
        posY = rect.top + rect.height() / 2;
    }

    public boolean getLife(){
        return life;
    }


    //getting methods
    public boolean getFlag(){
        return flag;
    }

    public boolean getQuestion_block_flag(){
        return question_block_flag;
    }

    public int getInit_x(){
        return init_x;
    }

    public int getPosX() {
        return posX;
    }


    public int getPosY() {
        return posY;
    }

    public Rect getRect(){
        return rect;
    }



    public boolean isClick(int x, int y) {
        return rect.contains(x, y);
    }

    public void moveImage(int x, int y) {
        posX += x;
        posY += y;
        rect.offset(x, y);
    }

    public void changeBitmap(Bitmap image){
        this.image = image;
    }


    //setting methods
    public void setLife(boolean life){
        this.life = life;
    }

    public void setInit_x(int x){
        this.init_x = x;
    }

    public void setQuestion_block_flag(boolean flag){
        this.question_block_flag = flag;
    }

    public void setFlag(boolean flag){
        this.flag = flag;
    }

    public void drawImage(Canvas canvas) {
        canvas.drawBitmap(image, null, rect, null);
    }

}
