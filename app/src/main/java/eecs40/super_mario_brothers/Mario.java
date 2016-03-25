package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Jun Li on 5/24/2015.
 */
public class Mario {
    private Dungeon dungeon;
    private final int velX = 5;
    private final int velY = 5;
    private final int right = 0;

    private Animation [][] mario;
    private Bitmap deathBitmap;
    private Rect rect;
    private Rect mapRect = new Rect();
    private int mode = 0;
    private int dir = right;
    public int [][] block;
    private int marioHeight[] = new int[2];

    private int moveX, moveY;
    private int screenWidth;
    private int bgWidth;
    private int mapX = 0;
    private int tileWidth;
    private int jumpMax;
    private int jumpBorder;
    private int indexX, indexY;

    private boolean jumpFlag = false;
    private boolean standFlag = true;
    private boolean upFlag = false;
    private boolean dead = false;
    private boolean win = false;
    private boolean touch = false;

    public Mario(Dungeon dungeon, Animation[][] mario, Rect rect) {
        this.dungeon = dungeon;
        this.mario = mario;
        this.rect = rect;
        mapRect.set(rect);
        tileWidth = rect.width();
        marioHeight[0] = tileWidth;
        marioHeight[1] = (int) (tileWidth * 1.5);
        jumpMax = tileWidth * 3;
        jumpBorder = rect.top;

    }

    public void setData(int [][] block, int screenWidth, int bgWidth) {
        this.block = block;
        this.screenWidth = screenWidth;
        this.bgWidth = bgWidth;

    }

    public void setDeathBitmap(Bitmap bitmap) {
        deathBitmap = bitmap;
    }
    private void synPos() {
        for (int i = 0; i < mario[mode].length; i++) {
            mario[mode][i].setPos(rect);
        }
    }

    public void setMode(int mode) {
        Rect newRect = new Rect(rect.left, rect.bottom - marioHeight[mode], rect.right, rect.bottom);
        this.mode = mode;
        rect = newRect;
        for (int i = 0; i < mario[mode].length; i++) {
            mario[mode][i].setPos(rect);
        }
    }

    public void setDir(int dir) {
        this.dir = dir;
        if (dir == right) {
            moveX = velX;
        }
        else {
            moveX = -velX;
        }
    }

    public void setStandFlag(boolean flag) {
        standFlag = flag;
        if (flag) {
            moveX = 0;
        }
    }

    public void setJumpFlag(boolean flag) {
        jumpFlag = flag;
        upFlag = flag;
    }

    public boolean getJumpFlag() {
        return jumpFlag;
    }

    public void setJumpBorder() {
        jumpBorder = rect.top - jumpMax;
    }

    public int getMapX() {
        return mapX;
    }

    public boolean getDead() {
        return dead;
    }

    public void setDead() {
        dead = true;
    }


    public boolean getTouch() {
        return touch;
    }

    public void cancelTouch() {
        touch = false;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public int getBlockValue(int x, int y) {
        return block[y][x];
    }

    public Rect getRect() {
        return rect;
    }

    public int getMode() {
        return mode;
    }
    public Rect getMapRect() {
        return mapRect;
    }

    public boolean getWin() {
        if (mapRect.right / tileWidth == 135) {
            win = true;
        }
        return win;
    }



    private void jump() {

        if (jumpFlag && upFlag && rect.top >= jumpBorder) {
            moveY = -velY;
        }
        else if (rect.top < jumpBorder) {
            upFlag = false;
            moveY = velY;
        }

        if (block[rect.bottom / tileWidth][mapRect.centerX() / tileWidth] != 0) {
            if (!upFlag) {
                moveY = 0;
                jumpFlag = false;
            }
        }
        else {
            if (!jumpFlag) {
                moveY = velY;
                jumpFlag = true;
                upFlag = false;
            }

            if (block[rect.top / tileWidth][mapRect.centerX() / tileWidth] != 0) {
                if (jumpFlag) {
                    upFlag = false;
                    moveY = velY;
                    indexX = mapRect.centerX() / tileWidth;
                    indexY = rect.top / tileWidth;
                    if (block[indexY][indexX] == 28 && mode == 1) {
                        block[indexY][indexX] = 0;
                        touch = true;
                    }

                    if (block[indexY][indexX] == 18) {
                        block[indexY][indexX] = 35;
                        touch = true;
                    }

                }
            }
        }
        if (rect.top <= 0) {
            rect.offsetTo(rect.left, 0);
            mapRect.offsetTo(mapRect.left, 0);
            upFlag = false;
            moveY = velY;

        }

        if (rect.bottom / tileWidth >= 9) {
            dead = true;
        }
        rect.offset(0, moveY);


    }

    private void walk() {
        int screenX, checkMapX;
        int rightLine = screenWidth * 3 / 5;
        int leftLine = screenWidth * 2 / 5;

        if (dir == right) {
            screenX = rect.right;
            checkMapX = mapRect.right;
        }
        else {
            screenX = rect.left;
            checkMapX = mapRect.left;
        }

        if (block[rect.centerY() / tileWidth][checkMapX / tileWidth] == 0) {
            mapRect.offset(moveX, 0);
            if (screenX > leftLine && screenX < rightLine) {
                rect.offset(moveX, 0);
            }
            else if (screenX >= rightLine) {
                if (checkMapX >= bgWidth - leftLine && checkMapX <= bgWidth) {
                    rect.offset(moveX, 0);
                }
                else {
                    mapX -= moveX;
                }
            }
            else {
                if (checkMapX <= leftLine) {
                    rect.offset(moveX, 0);
                }
                else {
                    mapX -= moveX;
                }
            }
        }

        if (mapRect.right / tileWidth >= 130 && mapRect.right <= 135) {
            rect.offset(moveX, 0);
            mapRect.offset(moveX, 0);

        }


        if (mapRect.left <= 0) {
            rect.offsetTo(0, rect.top);
            mapRect.offsetTo(0, rect.top);
        }
        else if (mapRect.right >= bgWidth) {
            mapRect.offsetTo(bgWidth - tileWidth, rect.top);
            rect.offset(screenWidth - tileWidth, rect.top);
        }


        if (mapX >= 0) {
            mapX = 0;
        }
        else if (mapX <= -(bgWidth - screenWidth)) {
            mapX = -(bgWidth - screenWidth);
        }
    }


    private void marioMove() {
        jump();
        walk();
        synPos();
    }

    public void drawMario(Canvas canvas) {
        marioMove();

        if (dead) {
            canvas.drawBitmap(deathBitmap, null, rect, null);
        }
        else if (jumpFlag) {
            mario[mode][dir].drawFrame(canvas, 8);
        }
        else if (standFlag) {
            mario[mode][dir].drawFrame(canvas, 7 * dir);
        }
        else {
            mario[mode][dir].drawAnim(canvas);
        }
    }

}
