package eecs40.super_mario_brothers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Li on 5/24/2015.
 */
public  class Dungeon extends SurfaceView implements SurfaceHolder.Callback {

    protected final int right = 0;
    protected final int left = 1;
    protected SurfaceHolder surfaceHolder;
    protected GameThread thread;
    public int screenWidth, screenHeight;
    protected int bgWidth;
    protected int tileWidth;
    protected Image bgImage;
    protected Bitmap tileSet;
    protected Image leftButton, rightButton, upButton;
    protected Mario mario;

    ArrayList<Image> object = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    public Dungeon(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        tileWidth = screenHeight / 10;
    }


    protected void initButton() {
        leftButton = new Image(readBitmap(R.drawable.left_arrow), new Rect(2 * tileWidth, 8 * tileWidth, 4 *tileWidth, 10 * tileWidth));
        rightButton = new Image(readBitmap(R.drawable.right_arrow), new Rect(5 *tileWidth, 8 * tileWidth, 7 * tileWidth, 10 * tileWidth));
        upButton = new Image(readBitmap(R.drawable.up_arrow), new Rect(getWidth() - 4 * tileWidth, 8 * tileWidth, screenWidth - 2 * tileWidth, 10 * tileWidth));
    }

    protected void initMario() {
        Animation [][] marioAnim;
        Rect rect = new Rect(3* tileWidth, 7 * tileWidth, 4 * tileWidth, 8 * tileWidth);
        int littleMario = 0;
        int superMario = 1;

        Bitmap mario1 = readBitmap(R.drawable.little_mario_walk);
        int subWidth_1 = mario1.getWidth() / 8;
        int subHeight_1 = mario1.getHeight() / 2;

        Bitmap mario2 = readBitmap(R.drawable.mario_walk);
        int subWidth_2 = mario2.getWidth() / 8;
        int subHeight_2 = mario2.getHeight() / 2;
        Bitmap [][] little_mario = new Bitmap[2][9];
        Bitmap [][] super_mario = new Bitmap[2][9];

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                little_mario[i][j] = Bitmap.createBitmap(mario1, j * subWidth_1, i * subHeight_1, subWidth_1, subHeight_1);
                super_mario[i][j] = Bitmap.createBitmap(mario2, j * subWidth_2, i * subHeight_2, subWidth_2, subHeight_2);
            }
        }

        mario1 = readBitmap(R.drawable.little_mario_jump);
        subWidth_1 = mario1.getWidth() / 3;
        subHeight_1 = mario1.getHeight();

        mario2 = readBitmap(R.drawable.mario_jump);
        subWidth_2 = mario2.getWidth() / 4;
        subHeight_2 = mario2.getHeight();
        for (int i = 0; i < 2; i++) {
            little_mario[i][8] = Bitmap.createBitmap(mario1, i * subWidth_1, 0, subWidth_1, subHeight_1);
            super_mario[i][8] = Bitmap.createBitmap(mario2, (i + 1) * subWidth_2, 0, subWidth_2, subHeight_2);
        }

        marioAnim = new Animation[2][2];
        marioAnim[littleMario][right] = new Animation(little_mario[right]);
        marioAnim[littleMario][left] = new Animation(little_mario[left]);
        marioAnim[superMario][right] = new Animation(super_mario[right]);
        marioAnim[superMario][left] = new Animation(super_mario[left]);

        mario = new Mario(this, marioAnim, rect);

        Bitmap death =  Bitmap.createBitmap(mario1, 2 * subWidth_1, 0, subWidth_1, subHeight_1);
        mario.setDeathBitmap(death);
    }

    protected void initEnemy(int [][] dataArray) {
        Enemy enemy;

        Bitmap bitmap = null;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 150; j++) {
                if (dataArray[i][j] == 16 || dataArray[i][j] == 17) {
                    if (dataArray[i][j] == 16) {
                        bitmap = readBitmap(R.drawable.goomba);
                    }
                    else if (dataArray[i][j] == 17) {
                        bitmap = readBitmap(R.drawable.boo);
                    }
                    enemy = new Enemy(bitmap,new Rect(j * tileWidth, i * tileWidth, (j + 1) * tileWidth, (i + 1) * tileWidth), this, mario);
                    enemy.setIndex(j, i);
                    enemies.add(enemy);
                }
            }
        }

    }


    protected void drawMap(Canvas canvas) {
        int mapX = mario.getMapX();
        bgImage.setScreen(mapX);
        bgImage.drawImage(canvas);
    }


    protected void drawButton(Canvas canvas) {
        leftButton.drawImage(canvas);
        rightButton.drawImage(canvas);
        upButton.drawImage(canvas);
    }

    protected Bitmap getTileSetBitmap(int index) {
        index--;
        int x = index % 6;
        int y = index / 6;
        return Bitmap.createBitmap(tileSet, x * 32, y * 32, 32, 32);
    }

    protected void getObjectArray(int [][] dataArray) {
        Image obj;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 150; j++) {
                if (dataArray[i][j] > 0 && dataArray[i][j] != 16 && dataArray[i][j] != 17) {
                    obj = new Image(getTileSetBitmap(dataArray[i][j]), new Rect(j * tileWidth, i * tileWidth, (j + 1) * tileWidth, (i + 1) * tileWidth));
                    obj.setIndex(j, i);
                    object.add(obj);
                }
            }
        }
    }

    protected void removeObject() {
        if (mario.getTouch()) {
            for (int i = 0; i < object.size(); i++) {
                if (mario.getIndexX() == object.get(i).getObjX() && mario.getIndexY() == object.get(i).getObjY()) {
                    int value = mario.getBlockValue(mario.getIndexX(), mario.getIndexY());
                    if (value == 0) {
                        object.remove(i);
                    }
                    else {
                        object.get(i).setBitmap(getTileSetBitmap(value));
                    }
                    mario.cancelTouch();
                }
            }
        }
    }

    public void drawObject(Canvas canvas){
        removeObject();
        for (int i = 0; i < object.size(); i++) {
            int mapX = mario.getMapX();
            object.get(i).setScreen(mapX);
            object.get(i).drawImage(canvas);
        }
    }

    public void drawEnemy(Canvas canvas) {
        Rect tmp = mario.getRect();

        for (int i = 0; i < enemies.size(); i++) {
            if (enemies.get(i).getKilled()) {
                enemies.remove(i);
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            int mapX = mario.getMapX();
            enemies.get(i).setScreen(mapX);
            enemies.get(i).setKilled(tmp);
            if (mario.getMode() == 0 && enemies.get(i).marioDead(tmp) && !enemies.get(i).getKilled()) {
                mario.setDead();
            }
            else if (enemies.get(i).marioDead(tmp)) {
                mario.setMode(0);
            }
            enemies.get(i).drawImage(canvas);
        }
    }


    public void draw(Canvas canvas) {
        drawMap(canvas);
        drawButton(canvas);
        drawObject(canvas);
        drawEnemy(canvas);
        mario.drawMario(canvas);

        if (mario.getWin()) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(250);
            canvas.drawText("You Win", screenWidth / 2 - 600, screenHeight / 2 - 50, paint);
            thread.interrupt();
        }
        else if (mario.getDead()) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(250);
            canvas.drawText("Game Over", screenWidth / 2 - 600, screenHeight / 2 - 50, paint);
            thread.interrupt();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y;
        x = (int) event.getX(event.getActionIndex());
        y = (int) event.getY(event.getActionIndex());

        if (mario.getMapRect().right / tileWidth >= 130) {
            return false;
        }
        else {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if (leftButton.isClick(x, y)) {
                        mario.setStandFlag(false);
                        mario.setDir(left);
                    }
                    else if (rightButton.isClick(x, y)) {
                        mario.setStandFlag(false);
                        mario.setDir(right);
                    }
                    else if (upButton.isClick(x, y) && !mario.getJumpFlag()) {
                        mario.setJumpFlag(true);
                        mario.setJumpBorder();
                    }
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    if (leftButton.isClick(x, y)) {
                        mario.setStandFlag(false);
                        mario.setDir(left);
                    }
                    else if (rightButton.isClick(x, y)) {
                        mario.setStandFlag(false);
                        mario.setDir(right);
                    }
                    else if (upButton.isClick(x, y) && !mario.getJumpFlag()) {
                        mario.setJumpBorder();
                        mario.setJumpFlag(true);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    mario.setStandFlag(true);
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    if (leftButton.isClick(x, y) || rightButton.isClick(x, y)) {
                        mario.setStandFlag(true);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    public Bitmap readBitmap(int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream inputStream = getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.interrupt();
    }
}
