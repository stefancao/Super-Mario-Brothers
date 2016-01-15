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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Li on 5/15/2015.
 */
public abstract class Dungeon extends SurfaceView implements SurfaceHolder.Callback{

    //defining and initializing variables

    private boolean YouWin = false;
    private boolean Restart = false;

    //Bitmaps
    private Bitmap brick_long;
    private Bitmap brick;
    private Bitmap coin;
    private Bitmap mushroom1up;
    private Bitmap mushroom1life;
    private Bitmap question_block;
    private Bitmap question_block_empty;
    private Bitmap goomba;
    private Bitmap blooper;

    //Images
    protected Image bg;
    protected Image button_left;
    protected Image button_right;
    protected Image button_up;

    protected Mario marioAnim;
    private List<List<Image>> objects = new ArrayList<List<Image>>();
    private ArrayList<Image> obj;

    //ArrayLists
    private ArrayList<Image> brickArray;
    private ArrayList<Image> coinArray;
    private ArrayList<Image> objectbrickArray;
    private ArrayList<Image> mushroomArray;
    private ArrayList<Image> question_blockArray;
    private ArrayList<Image> enemyArray;


    protected Bitmap mushroom = readBitmap(R.drawable.mushroomlife);

    protected final int right = 0;
    protected final int left = 1;


    //Not using anymore (ONLY some so don't delete)
    protected Bitmap tileSet;
    protected int tileSetWidthCount;
    protected int tileSetHeightCount;
    protected int tileWidth;
    protected int tileHeight;
    protected final static int TILESET_WIDTH = 32;
    protected final static int TILESET_HEIGHT = 32;
    protected final static int TILE_WIDTH_COUNT = 150;
    protected final static int TILE_HEIGHT_COUNT = 10;

    protected final static int objectSide = 100;

    protected GameThread gameThread;

    //scores, life, coins
    protected static int score = 0;
    protected static int life = 3;
    protected static int coins = 0;

    private int move = 1;




    protected Dungeon(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //importing the images to varaibles
        BitmapFactory.Options options = new BitmapFactory.Options();
        brick_long = BitmapFactory.decodeResource(this.getResources(), R.drawable.new_brick, options);
        brick = BitmapFactory.decodeResource(this.getResources(), R.drawable.brick, options);
        coin = BitmapFactory.decodeResource(this.getResources(),R.drawable.coin,options);
        mushroom1up = BitmapFactory.decodeResource(this.getResources(), R.drawable.mushroom1up);
        mushroom1life = BitmapFactory.decodeResource(this.getResources(), R.drawable.mushroomlife);
        question_block = BitmapFactory.decodeResource(this.getResources(), R.drawable.question_block);
        question_block_empty = BitmapFactory.decodeResource(this.getResources(), R.drawable.question_block_empty);
        goomba = BitmapFactory.decodeResource(this.getResources(), R.drawable.goomba);
        blooper = BitmapFactory.decodeResource(this.getResources(), R.drawable.blooper);

        int tile = getHeight() / TILE_HEIGHT_COUNT;
        button_left = new Image(readBitmap(R.drawable.left_arrow), new Rect(2 * tile, 8 * tile, 4 *tile, 10 * tile));
        button_right = new Image(readBitmap(R.drawable.right_arrow), new Rect(5 *tile, 8 * tile, 7 * tile, 10 * tile));
        button_up = new Image(readBitmap(R.drawable.up_arrow), new Rect(getWidth() - 4 * tile, 8 * tile, getWidth() - 2 *tile, 10 * tile));
        makeMarioAnim(tile);

        //starting thread
        gameThread = new GameThread(this);
        gameThread.start();

    }

    public void makeMarioAnim(int tile) {

        score = 0;  //initlaize score to 0 whenever making new mario

        Anim [][] mario;
        Rect rect = new Rect(3* tile, 7 * tile, 4 * tile, 8 * tile);
        int littleMario = 0;
        int superMario = 1;

        //little mario
        Bitmap mario1 = readBitmap(R.drawable.little_mario_walk);
        int subWidth_1 = mario1.getWidth() / 8;
        int subHeight_1 = mario1.getHeight() / 2;

        //big mario
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

        //little mario
        mario1 = readBitmap(R.drawable.little_mario_jump);
        subWidth_1 = mario1.getWidth() / 3;
        subHeight_1 = mario1.getHeight();

        //big mario
        mario2 = readBitmap(R.drawable.mario_jump);
        subWidth_2 = mario2.getWidth() / 4;
        subHeight_2 = mario2.getHeight();
        for (int i = 0; i < 2; i++) {
            little_mario[i][8] = Bitmap.createBitmap(mario1, i * subWidth_1, 0, subWidth_1, subHeight_1);
            super_mario[i][8] = Bitmap.createBitmap(mario2, (i + 1) * subWidth_2, 0, subWidth_2, subHeight_2);
        }

        mario = new Anim[2][2];
        mario[littleMario][right] = new Anim(little_mario[right], true);
        mario[littleMario][left] = new Anim(little_mario[left], true);
        mario[superMario][right] = new Anim(super_mario[right], true);
        mario[superMario][left] = new Anim(super_mario[left], true);

        //initlizing some instances
        marioAnim = new Mario(mario, rect);
        marioAnim.setMarioX_counter(marioAnim.getMarioPosX());
        marioAnim.setScreenWidth(getWidth());
        marioAnim.setScreen(new Rect(0, 0, getWidth(), getHeight()));
        marioAnim.setMarioInitY(marioAnim.getRect().centerY());
        marioAnim.setMario_init_bottom_y(marioAnim.getRect().bottom);

    }

    //reading bitmaps
    protected Bitmap readBitmap(int resID) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        InputStream inputStream = getResources().openRawResource(resID);
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public void tick(Canvas canvas) {

        //checking in tick method
        checkCoin();
        checkMushroom();
        checkQuestionBlock();
        checkBrick();
        checkEnemy();

        //drawing arrows
        button_left.drawImage(canvas);
        button_right.drawImage(canvas);
        button_up.drawImage(canvas);
        marioAnim.drawMario(canvas);

        //moving the objects if necessary
        moveEnemy();
        movebrick();
        moveObjects();
    };

    //restarting the game if killed by enemy
    private void restartGame(Canvas canvas){

        int tmp_coins = coins;  //coins will stay the same
        int tmp_life = life - 1; //subtract 1

        //if you win, print out
        if (YouWin){
            Paint paint4 = new Paint();
            paint4.setColor(Color.WHITE);
            paint4.setTextSize(250);
            canvas.drawText("You Win", getWidth() / 2 - 600, getHeight() / 2 - 50, paint4);
        }

        //if no life left then game over
        if (tmp_life == -1){
            Paint paint4 = new Paint();
            paint4.setColor(Color.WHITE);
            paint4.setTextSize(250);

            if (YouWin){

            }
            else{
                canvas.drawText("Game Over", getWidth() / 2 - 600, getHeight() / 2 - 50, paint4);
            }

            gameThread.interrupt(); //interrupting thread

            tmp_life++; //to make it stop
            Restart = true; //to make it stop

        }
        //else restart the game over again
        else {

            objects = null;

            objects = new ArrayList<List<Image>>();
            this.surfaceCreated(getHolder());

            gameThread = null;
            gameThread = new GameThread(this);
            gameThread.start();
            Restart = false;
        }

        //rememering the coins and life from last game session
        coins = tmp_coins;
        life = tmp_life;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();

                //left click
                if (button_left.isClick(x, y)) {
                    marioAnim.setStand(false);  //these are for animations
                    marioAnim.synPos();
                    marioAnim.setDirection(1);
                    marioAnim.setMoveX(1);
                    marioAnim.setLeftLock(true);
                }
                //right click
                else if (button_right.isClick(x, y)) {
                    marioAnim.setStand(false);  //these are for animations
                    marioAnim.synPos();
                    marioAnim.setDirection(0);
                    marioAnim.setMoveX(0);
                    marioAnim.setRightLock(true);
                }
                //up click
                else if (button_up.isClick(x, y)) {
                    marioAnim.setMoveX(-1);

                    //for jumping
                    if (marioAnim.getLock() == false){
                        marioAnim.setPosY(marioAnim.getRect());
                        marioAnim.setInitY(marioAnim.getRect().centerY());
                        marioAnim.setJump(true);
                        marioAnim.setLock(true);
                        marioAnim.setUpFlag(true);

                    }
                }
                break;


            case MotionEvent.ACTION_UP:
                marioAnim.setStand(true);   //for animation
                marioAnim.setRightLock(false);
                marioAnim.setLeftLock(false);
                //marioAnim.setJump(false);
                break;

            //for multitouch
            case MotionEvent.ACTION_POINTER_DOWN:
                x = (int) event.getX(event.getActionIndex());
                y = (int) event.getY(event.getActionIndex());

                if (button_left.isClick(x, y)) {
                    marioAnim.setStand(false);  //for animation
                    marioAnim.synPos();
                    marioAnim.setDirection(1);
                    marioAnim.setMoveX(1);
                    marioAnim.setLeftLock(true);

                }
                else if (button_right.isClick(x, y)) {
                    marioAnim.setStand(false);  //for animation
                    marioAnim.synPos();
                    marioAnim.setDirection(0);
                    marioAnim.setMoveX(0);
                    marioAnim.setRightLock(true);
                    marioAnim.setRightLock(true);
                }
                else if (button_up.isClick(x, y)) {

                    //for jumping
                    if (marioAnim.getLock() == false){
                        marioAnim.setPosY(marioAnim.getRect());
                        marioAnim.setInitY(marioAnim.getRect().centerY());
                        marioAnim.setJump(true);
                        marioAnim.setLock(true);
                        marioAnim.setUpFlag(true);

                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:

                x = (int) event.getX(event.getActionIndex());
                y = (int) event.getY(event.getActionIndex());

                marioAnim.setRightLock(false);
                marioAnim.setLeftLock(false);

                if (button_left.isClick(x, y) || button_right.isClick(x, y)) {
                    marioAnim.setStand(true);
                    marioAnim.setMoveX(-1);
                }
                else if (button_up.isClick(x, y)) {

                }
                break;
            default:
                break;
        }
        return true;
    }

    //Not really using anymore
    protected Bitmap getTileSetBitmap(int index) {
        index--;
        int x = index % tileSetWidthCount;
        int y = index / tileSetWidthCount;
        return Bitmap.createBitmap(tileSet, x * TILESET_WIDTH, y * TILESET_HEIGHT, TILESET_WIDTH, TILESET_HEIGHT);
    }

    //not really using anymore
    protected void getObjectArray(int [][] dataArray) {
        obj = new ArrayList<>();
        for (int i = 0; i < TILE_HEIGHT_COUNT; i++) {
            for (int j = 0; j < TILE_WIDTH_COUNT; j++) {
                if (dataArray[i][j] > 0) {
                    obj.add(new Image(getTileSetBitmap(dataArray[i][j]), new Rect(j * tileWidth, i * tileHeight, (j + 1) * tileWidth, (i + 1) * tileHeight)));
                }
            }
        }
        objects.add(obj);
    }

    //adding objects to arrays when loading up a level
    protected void addObjectArray(int level){

        int x1, y1;
        int space_width = 50;

        switch (level){
            case 1: //case level 1
                x1 = 200;
                y1 = 500;
                space_width = 50;

                //coin
                coinArray = new ArrayList<>();
                for (int i = 0; i < 4; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += space_width + objectSide;
                }
                x1 = 500;
                y1 = 600 - objectSide;
                for (int i = 0; i < 80; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 700;
                y1 = 500 - 4*objectSide;
                for (int i = 0; i < 3; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                objects.add(coinArray);

                //brick
                objectbrickArray = new ArrayList<>();
                objectbrickArray.add(new Image(brick, new Rect(500, 500, 500+objectSide, 500+objectSide)));

                x1 = 700;
                y1 = 500;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 700;
                y1 = 500 - 3*objectSide;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 1900;
                y1 = 700;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 2400;
                y1 = 500;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 2700;
                y1 = 600;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3200;
                y1 = 500;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3600;
                y1 = 300;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 4200;
                y1 = 700;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 5000;
                y1 = 400;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 6000;
                y1 = 500;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 7500;
                y1 = 300;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                objects.add(objectbrickArray);  //putting all in a big objects list


                //mushroom
                mushroomArray = new ArrayList<>();
                mushroomArray.add(new Image(mushroom1up, new Rect(600, 500 - objectSide, 600 + objectSide, 500)));
                mushroomArray.get(0).setFlag(false);
                mushroomArray.get(0).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(2000, 500 - objectSide, 2000 + objectSide, 500)));
                mushroomArray.get(1).setFlag(false);
                mushroomArray.get(1).setLife(true);     //mushroomlife
                mushroomArray.add(new Image(mushroom1up, new Rect(4000, 400 - objectSide, 4000 + objectSide, 400)));
                mushroomArray.get(2).setFlag(false);
                mushroomArray.get(2).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(7000, 400 - objectSide, 7000 + objectSide, 400)));
                mushroomArray.get(3).setFlag(false);
                mushroomArray.get(3).setLife(true);

                objects.add(mushroomArray); //putting all in a big objects list

                //question_block
                question_blockArray = new ArrayList<>();
                question_blockArray.add(new Image(question_block, new Rect(600, 500, 600 + objectSide, 500 + objectSide)));
                question_blockArray.get(0).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(2000, 500, 2000 + objectSide, 500 + objectSide)));
                question_blockArray.get(1).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(4000, 400, 4000 + objectSide, 400 + objectSide)));
                question_blockArray.get(2).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(7000, 400, 7000 + objectSide, 400 + objectSide)));
                question_blockArray.get(3).setQuestion_block_flag(true);

                objects.add(question_blockArray);   //putting all in a big objects list

                //enemy

                enemyArray = new ArrayList<>();
                //goomba
                enemyArray.add(new Image(goomba, new Rect(800, 700, 800 + objectSide, 700 + objectSide)));
                enemyArray.get(0).setInit_x(enemyArray.get(0).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(5000, 700, 5000 + objectSide, 700 + objectSide)));
                enemyArray.get(1).setInit_x(enemyArray.get(1).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(4000, 700, 4000 + objectSide, 700 + objectSide)));
                enemyArray.get(2).setInit_x(enemyArray.get(2).getRect().centerX());

                //blooper
                enemyArray.add(new Image(blooper, new Rect(2500, 700, 2500 + objectSide, 700 + objectSide)));
                enemyArray.get(3).setInit_x(enemyArray.get(3).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3000, 700, 3000 + objectSide, 700 + objectSide)));
                enemyArray.get(4).setInit_x(enemyArray.get(4).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3500, 700, 3500 + objectSide, 700 + objectSide)));
                enemyArray.get(5).setInit_x(enemyArray.get(5).getRect().centerX());

                objects.add(enemyArray);    //putting all in a big objects list

                break;
            case 2: //level 2

                x1 = 100;
                y1 = 500;
                space_width = 50;

                //coin
                coinArray = new ArrayList<>();
                for (int i = 0; i < 2; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += space_width + objectSide;
                }
                x1 = 600;
                y1 = 600 - objectSide;
                for (int i = 0; i < 30; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 700;
                y1 = 500 - 4*objectSide;
                for (int i = 0; i < 3; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                objects.add(coinArray);

                //brick
                objectbrickArray = new ArrayList<>();
                objectbrickArray.add(new Image(brick, new Rect(800, 500, 800+objectSide, 500+objectSide)));

                x1 = 900;
                y1 = 500;
                for(int i = 0; i < 4; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 980;
                y1 = 500 - 3*objectSide;
                for(int i = 0; i < 7; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 2100;
                y1 = 700;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 2600;
                y1 = 500;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 2900;
                y1 = 600;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3300;
                y1 = 500;
                for(int i = 0; i < 4; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3600;
                y1 = 300;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 4000;
                y1 = 700;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 5100;
                y1 = 400;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 6100;
                y1 = 500;
                for(int i = 0; i < 3; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 7200;
                y1 = 300;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                objects.add(objectbrickArray);  //putting all in a big objects list

                //mushroom
                mushroomArray = new ArrayList<>();
                mushroomArray.add(new Image(mushroom1up, new Rect(650, 500 - objectSide, 650 + objectSide, 500)));
                mushroomArray.get(0).setFlag(false);
                mushroomArray.get(0).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(2200, 500 - objectSide, 2200 + objectSide, 500)));
                mushroomArray.get(1).setFlag(false);
                mushroomArray.get(1).setLife(true);     //mushroomlife
                mushroomArray.add(new Image(mushroom1up, new Rect(4200, 400 - objectSide, 4200 + objectSide, 400)));
                mushroomArray.get(2).setFlag(false);
                mushroomArray.get(2).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(6900, 400 - objectSide, 6900 + objectSide, 400)));
                mushroomArray.get(3).setFlag(false);
                mushroomArray.get(3).setLife(true);

                objects.add(mushroomArray);     //putting all in a big objects list

                //question_block
                question_blockArray = new ArrayList<>();
                question_blockArray.add(new Image(question_block, new Rect(650, 500, 650 + objectSide, 500 + objectSide)));
                question_blockArray.get(0).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(2200, 500, 2200 + objectSide, 500 + objectSide)));
                question_blockArray.get(1).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(4200, 400, 4200 + objectSide, 400 + objectSide)));
                question_blockArray.get(2).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(6900, 400, 6900 + objectSide, 400 + objectSide)));
                question_blockArray.get(3).setQuestion_block_flag(true);

                objects.add(question_blockArray);       //putting all in a big objects list

                //enemy
                enemyArray = new ArrayList<>();
                //goomba
                enemyArray.add(new Image(goomba, new Rect(820, 700, 820 + objectSide, 700 + objectSide)));
                enemyArray.get(0).setInit_x(enemyArray.get(0).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(5300, 700, 5300 + objectSide, 700 + objectSide)));
                enemyArray.get(1).setInit_x(enemyArray.get(1).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(3900, 700, 3900 + objectSide, 700 + objectSide)));
                enemyArray.get(2).setInit_x(enemyArray.get(2).getRect().centerX());

                //blooper
                enemyArray.add(new Image(blooper, new Rect(2200, 700, 2200 + objectSide, 700 + objectSide)));
                enemyArray.get(3).setInit_x(enemyArray.get(3).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3500, 700, 3500 + objectSide, 700 + objectSide)));
                enemyArray.get(4).setInit_x(enemyArray.get(4).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3800, 700, 3800 + objectSide, 700 + objectSide)));
                enemyArray.get(5).setInit_x(enemyArray.get(5).getRect().centerX());

                objects.add(enemyArray);    //putting all in a big objects list

                break;

            case 3: //level 3

                x1 = 400;
                y1 = 500;
                space_width = 50;

                //coin
                coinArray = new ArrayList<>();
                for (int i = 0; i < 2; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += space_width + objectSide;
                }
                x1 = 500;
                y1 = 600 - objectSide;
                for (int i = 0; i < 35; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 600;
                y1 = 500 - 4*objectSide;
                for (int i = 0; i < 3; i++){
                    coinArray.add(new Image(coin, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                objects.add(coinArray);

                //brick
                objectbrickArray = new ArrayList<>();
                objectbrickArray.add(new Image(brick, new Rect(500, 500, 500+objectSide, 500+objectSide)));

                x1 = 800;
                y1 = 500;
                for(int i = 0; i < 5; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 1000;
                y1 = 500 - 3*objectSide;
                for(int i = 0; i < 4; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 2200;
                y1 = 700;
                for(int i = 0; i < 6; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 2600;
                y1 = 500;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 2700;
                y1 = 600;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3100;
                y1 = 500;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 3600;
                y1 = 300;
                for(int i = 0; i < 6; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                x1 = 4400;
                y1 = 700;
                objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));

                x1 = 5500;
                y1 = 400;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 6200;
                y1 = 500;
                for(int i = 0; i < 4; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }
                x1 = 7200;
                y1 = 300;
                for(int i = 0; i < 2; i++){
                    objectbrickArray.add(new Image(brick, new Rect(x1, y1, x1+objectSide, y1+objectSide)));
                    x1 += objectSide;
                }

                objects.add(objectbrickArray);      //putting all in a big objects list

                //mushroom
                mushroomArray = new ArrayList<>();
                mushroomArray.add(new Image(mushroom1up, new Rect(640, 500 - objectSide, 640 + objectSide, 500)));
                mushroomArray.get(0).setFlag(false);
                mushroomArray.get(0).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(2500, 500 - objectSide, 2500 + objectSide, 500)));
                mushroomArray.get(1).setFlag(false);
                mushroomArray.get(1).setLife(true);     //mushroomlife
                mushroomArray.add(new Image(mushroom1up, new Rect(4200, 400 - objectSide, 4200 + objectSide, 400)));
                mushroomArray.get(2).setFlag(false);
                mushroomArray.get(2).setLife(false);    //mushroomup
                mushroomArray.add(new Image(mushroom1life, new Rect(6700, 400 - objectSide, 6700 + objectSide, 400)));
                mushroomArray.get(3).setFlag(false);
                mushroomArray.get(3).setLife(true);


                objects.add(mushroomArray); //putting all in a big objects list

                //question_block
                question_blockArray = new ArrayList<>();
                question_blockArray.add(new Image(question_block, new Rect(640, 500, 640 + objectSide, 500 + objectSide)));
                question_blockArray.get(0).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(2500, 500, 2500 + objectSide, 500 + objectSide)));
                question_blockArray.get(1).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(4200, 400, 4200 + objectSide, 400 + objectSide)));
                question_blockArray.get(2).setQuestion_block_flag(true);
                question_blockArray.add(new Image(question_block, new Rect(6700, 400, 6700 + objectSide, 400 + objectSide)));
                question_blockArray.get(3).setQuestion_block_flag(true);

                objects.add(question_blockArray);       //putting all in a big objects list


                //enemy
                enemyArray = new ArrayList<>();
                //goomba
                enemyArray.add(new Image(goomba, new Rect(820, 700, 820 + objectSide, 700 + objectSide)));
                enemyArray.get(0).setInit_x(enemyArray.get(0).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(5100, 700, 5100 + objectSide, 700 + objectSide)));
                enemyArray.get(1).setInit_x(enemyArray.get(1).getRect().centerX());
                enemyArray.add(new Image(goomba, new Rect(4100, 700, 4100 + objectSide, 700 + objectSide)));
                enemyArray.get(2).setInit_x(enemyArray.get(2).getRect().centerX());


                //blooper
                enemyArray.add(new Image(blooper, new Rect(2400, 700, 2400 + objectSide, 700 + objectSide)));
                enemyArray.get(3).setInit_x(enemyArray.get(3).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3100, 700, 3100 + objectSide, 700 + objectSide)));
                enemyArray.get(4).setInit_x(enemyArray.get(4).getRect().centerX());
                enemyArray.add(new Image(blooper, new Rect(3400, 700, 3400 + objectSide, 700 + objectSide)));
                enemyArray.get(5).setInit_x(enemyArray.get(5).getRect().centerX());

                objects.add(enemyArray);        //putting all in a big objects list


                break;
            default:
                break;

        }

    }

    //adding bricks
    protected void addBrickArray(int level){

        brickArray = new ArrayList<>();
        int brick_side = getHeight() - marioAnim.getRect().bottom;

        for (int i = 0; i < 5; i++){
            brickArray.add(new Image(brick_long,new Rect(getWidth()*i, getHeight() - brick_side, getWidth()*(i+1), getHeight())));
        }

        objects.add(brickArray);        //adding brcisk to bigger list

    }

    //checking if mario hits any coins
    protected void checkCoin(){
        int index = objects.indexOf(coinArray);
        for (int i = 0; i < objects.get(index).size(); i++ ) {
            if(objects.get(index).get(i).getRect().intersect(marioAnim.getRect())){
                objects.get(index).remove(i);
                coins++;        //increment coins
                score++;        //increment score
            }
        }
    }

    //checking if mario hits any mushroom
    protected void checkMushroom(){
        int index = objects.indexOf(mushroomArray);
        for (int i = 0; i < objects.get(index).size(); i++ ) {

            //mushroomup1
            if(objects.get(index).get(i).getRect().intersect(marioAnim.getRect()) && (objects.get(index).get(i).getFlag()) && (!objects.get(index).get(i).getLife())){
                objects.get(index).remove(i);
                score += 10;
                marioAnim.setMode(1);       //big mario
            }
            //mushroomlife
            else if (objects.get(index).get(i).getRect().intersect(marioAnim.getRect()) && (objects.get(index).get(i).getFlag()) && (objects.get(index).get(i).getLife())){
                objects.get(index).remove(i);
                score += 10;
                life += 1;      //increment life
            }
        }
    }

    //check if hitting any bricks
    protected void checkBrick(){
        int index = objects.indexOf(objectbrickArray);
        for (int i = 0; i < objects.get(index).size(); i++ ) {

            Rect temp = new Rect(objects.get(index).get(i).getRect());  //copy because doesn't work otherwise...

            if(temp.intersect(marioAnim.getRect())){

                //jumping up TO block
                if(marioAnim.getRect().top <= objects.get(index).get(i).getRect().bottom && (marioAnim.getRect().centerY() > objects.get(index).get(i).getRect().centerY()) && marioAnim.getRect().centerY() < getWidth() ){
                    marioAnim.setUpFlag(false);     //make sure cannot jump up anymore make mario go down
                    marioAnim.setBlock_flag_brick(false);

                    if (marioAnim.getMode() == 1){  //if big mario then can break brick
                        objects.get(index).remove(i);
                        score++;
                    }

                }
                //down TO block
                if(marioAnim.getRect().bottom >= objects.get(index).get(i).getRect().top && (marioAnim.getRect().centerY() > 0 && marioAnim.getRect().centerY() < objects.get(index).get(i).getRect().centerY()) ) {
                    marioAnim.setStop_flag(true);
                    marioAnim.setNew_y_init(objects.get(index).get(i).getRect().top);
                    marioAnim.setBlock_flag_brick(true);
                    marioAnim.setBlock_stop(true);

                }

                else if ((marioAnim.getRect().right >= objects.get(index).get(i).getRect().left) ||(marioAnim.getRect().left >= objects.get(index).get(i).getRect().right)){
                    marioAnim.setMoveX(-1);
                }

            }
            else{
                marioAnim.setBlock_flag_brick(false);       //used for jumping purposes
            }
        }
    }

    //check if mario hits question blocks
    protected void checkQuestionBlock(){
        int index = objects.indexOf(question_blockArray);
        for (int i = 0; i < objects.get(index).size(); i++ ) {

            Rect temp = new Rect(objects.get(index).get(i).getRect());      //makign copy doesn't work otherwise...

            if(temp.intersect(marioAnim.getRect())){

                //jumping up TO block
                if(marioAnim.getRect().top <= objects.get(index).get(i).getRect().bottom && (marioAnim.getRect().centerY() > objects.get(index).get(i).getRect().centerY()) && marioAnim.getRect().centerY() < getWidth() ){
                    marioAnim.setUpFlag(false);
                    marioAnim.setBlock_flag_question(false);

                    if (objects.get(index).get(i).getQuestion_block_flag()){
                        objects.get(index).get(i).changeBitmap(question_block_empty);   //changing to an empty question block

                        int index_mushroom = objects.indexOf(mushroomArray);     //making the mushroom to appear
                        objects.get(index_mushroom).get(i).setFlag(true);
                        objects.get(index).get(i).setQuestion_block_flag(false);
                    }

                }
                //down TO block
                if(marioAnim.getRect().bottom >= objects.get(index).get(i).getRect().top && (marioAnim.getRect().centerY() > 0 && marioAnim.getRect().centerY() < objects.get(index).get(i).getRect().centerY()) ) {
                    marioAnim.setStop_flag(true);
                    marioAnim.setNew_y_init(objects.get(index).get(i).getRect().top);
                    marioAnim.setBlock_flag_question(true);
                    marioAnim.setBlock_stop(true);

                }

                else if ((marioAnim.getRect().right >= objects.get(index).get(i).getRect().left) ||(marioAnim.getRect().left >= objects.get(index).get(i).getRect().right)){
                    marioAnim.setMoveX(-1);
                }

            }
            else{
                marioAnim.setBlock_flag_question(false);
            }

        }
    }

    //cheking if mario hits any enemies
    protected void checkEnemy(){

        int index = objects.indexOf(enemyArray);
        for (int i = 0; i < objects.get(index).size(); i++ ) {

            Rect temp = new Rect(objects.get(index).get(i).getRect());

            if(temp.intersect(marioAnim.getRect())){

                //jumping up TO block
                if(marioAnim.getRect().top <= objects.get(index).get(i).getRect().bottom && (marioAnim.getRect().centerY() > objects.get(index).get(i).getRect().centerY()) && marioAnim.getRect().centerY() < getWidth() ){

                }

                //down TO block
                if(marioAnim.getRect().bottom >= objects.get(index).get(i).getRect().top && (marioAnim.getRect().centerY() > 0 && marioAnim.getRect().centerY() < objects.get(index).get(i).getRect().centerY()) ) {
                    objects.get(index).remove(i);
                    score++;

                }

                if ((marioAnim.getRect().right >= objects.get(index).get(i).getRect().left) ||(marioAnim.getRect().left >= objects.get(index).get(i).getRect().right)){
                    if (marioAnim.getMode() == 1){      //if big mario shrink to little mario
                        marioAnim.setMode(0);
                    }
                    else {
                        Restart = true;     //else you are killed by the enemy
                    }

                }

            }

        }
    }

    //moving the enemy
    protected void moveEnemy(){
        int index = objects.indexOf(enemyArray);
        for(int i = 0; i <objects.get(index).size(); i++) {
            objects.get(index).get(i).moveImage(-5*move, 0);
            if (objects.get(index).get(i).getRect().centerX() < objects.get(index).get(i).getInit_x() - 300) {      //moving left and right within a certain coordinate
                move = -move;
            }
            if ((objects.get(index).get(i).getRect().centerX() > objects.get(index).get(i).getInit_x() + 300)){     //moving left and right within a certain coordinate
                move = -move;
            }
        }
    }

    //moving the objects
    protected void moveObjects(){
        if ((marioAnim.getMarioPosX() > (getWidth()/5 * 4) && marioAnim.getRightLock())
                ||(marioAnim.getMarioPosX() < (getWidth()/5) && marioAnim.getLeftLock() && (!marioAnim.getOriginalScreen())) ){

            int move = (-2 * marioAnim.getDirection() + 1) * marioAnim.getVelX();

            //coins
            int index = objects.indexOf(coinArray);
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);      //move along with the screen
            }

            //brick
            index =objects.indexOf(objectbrickArray);
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);      //move along with the screen
            }

            //mushroom1up
            index = objects.indexOf(mushroomArray);
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);      //move along with the screen
            }

            //question_block
            index = objects.indexOf(question_blockArray);
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);      //move along with the screen
            }

            //enemy
            index = objects.indexOf(enemyArray);
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);      //move along with the screen
            }
        }

    }

    //move the bottom bricks
    protected void movebrick(){

        if (marioAnim.getMarioPosX() > (getWidth()/5 * 4) && marioAnim.getRightLock()){
            int index = objects.indexOf(brickArray);
            int move = (-2 * marioAnim.getDirection() + 1) * marioAnim.getVelX();
            for(int i = 0; i <objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);
            }
        }

        else if (marioAnim.getMarioPosX() < (getWidth()/5) && marioAnim.getLeftLock() && (!marioAnim.getOriginalScreen())){
            int index = objects.indexOf(brickArray);

            int move = (-2 * marioAnim.getDirection() + 1) * marioAnim.getVelX();
            for (int i = 0; i < objects.get(index).size(); i++) {
                objects.get(index).get(i).moveImage(-move, 0);
            }
        }
    }

    //draw the objects
    protected void drawObjects(Canvas canvas) {
        if (marioAnim.getMarioX_counter() >= 5000){     //winning if hitting a certain point of the game
            YouWin = true;
            restartGame(canvas);
        }

        if(coins >= 100){       //getting a new life if having more than 100 coins
            life++;
            coins = 0;
        }

        if(Restart){        //restarting the game (if killed by enemy)
            restartGame(canvas);
        }


        for (int i = 0; i < objects.size(); i++) {
            for (int j = 0; j < objects.get(i).size(); j++) {
                if(!objects.get(i).get(j).getFlag()){       //some mushrooms are hidden don't draw them if they are hidden

                }
                else {
                    objects.get(i).get(j).drawImage(canvas);
                }
            }
        }
    }


    //draw score, life, coins
    protected void drawScore(Canvas c){
        Paint paint2 = new Paint();
        paint2.setColor(Color.WHITE);
        paint2.setTextSize(100);
        c.drawText("Score:" + Integer.toString(score) + "\t \t \t Life: x" + Integer.toString(life) + "\t \t \t Coin: x" + Integer.toString(coins), 50, 100, paint2);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.interrupt();
    }
}
