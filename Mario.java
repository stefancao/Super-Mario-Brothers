package eecs40.super_mario_brothers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by swear_000 on 5/16/2015.
 */
public class Mario {

    //definign and initilizing variables
    private boolean block_stop = false;
    private boolean block_flag_brick = false;
    private boolean block_flag_question = false;

    private int mario_init_bottom_y;
    private int new_y_init;
    private boolean original_screen = true;

    private int little_super_mario_y_diff;

    private Rect Screen;

    private int MarioX_counter;

    private int Screen_Width;

    private Anim[][] mario;
    private int direction = 0;

    private int init_y;
    private int jump_distance_border;
    private int max_jump = -300;
    private int jump_offset = -10;

    private int posY;
    private int posX;

    private boolean lock = false;
    private boolean up_flag = false;
    private boolean stop_flag = false;

    private boolean check_lock = true;
    private int check_lock_counter = 0;

    private boolean RightLock = false;
    private boolean LeftLock = false;


    private int mario_init_Y;


    private Rect rect;
    private int mode = 0;
    private boolean stand = true;
    private boolean jump = false;
    private int moveX = 0;
    private int moveY = 0;
    private final int velX = 10;
    private final int velY = 1;
    private final int right = 0;
    private final int left = 1;
    private final int little_mario_height = 100;
    private final int height_diff = 100;
    private boolean move = true;

    public Mario(Anim[][] mario, Rect rect) {
        this.mario = mario;
        this.rect = rect;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                mario[i][j].setPos(rect);
            }
        }
    }


    //getting methods
    public int getMode(){
        return mode;
    }

    public boolean getUpFlag(){
        return up_flag;
    }

    public boolean getBlock_flag_brick(){
        return block_flag_brick;
    }

    public boolean getBlock_flag_question(){
        return block_flag_question;
    }

    public int getNew_y_init(){
        return new_y_init;
    }

    public int getLittle_super_mario_y_diff(){
        return little_super_mario_y_diff;
    }

    public Rect getScreen(){
        return Screen;
    }

    public boolean getOriginalScreen(){
        return original_screen;
    }

    public int getMario_init_bottom_y(){
        return mario_init_bottom_y;
    }

    public boolean getRightLock(){
        return RightLock;
    }

    public boolean getLeftLock(){
        return LeftLock;
    }

    public int getMarioX_counter(){
        return MarioX_counter;
    }

    public int getMarioLeft(){
        return rect.left;
    }

    public int getMarioRight(){
        return rect.right;
    }

    public int getMarioPosX() {
        return rect.centerX();
    }

    public int getDirection(){
        return direction;
    }

    public int getVelX(){
        return velX;
    }

    public int getMarioPosY(){
        return rect.centerY();
    }

    public boolean getLock(){
        return this.lock;
    }

    public Rect getRect() {
        return rect;
    }


    //setting methods
    public void setBlock_flag_brick(boolean flag){
        this.block_flag_brick = flag;
    }

    public void setBlock_flag_question(boolean flag){
        this.block_flag_question = flag;
    }

    public void setBlock_stop(boolean stop){
        this.block_stop = stop;
    }

    public void setMario_init_bottom_y(int y){
        this.mario_init_bottom_y = y;
    }

    public void setNew_y_init(int new_y_init){
        this.new_y_init = new_y_init;
    }

    public void setLittle_super_mario_y_diff(int diff){
        this.little_super_mario_y_diff = diff;
    }

    public void setStop_flag(boolean stop){
        this.stop_flag = stop;
    }

    public void setMarioInitY(int y){
        this.mario_init_Y = y;
    }

    public void setScreen(Rect screen){
        this.Screen = screen;
    }

    public void setOriginal_screen(boolean original_screen){
        this.original_screen = original_screen;
    }

    public void setRightLock(boolean rightLock){
        this.RightLock = rightLock;
    }

    public void setLeftLock(boolean leftLock){
        this.LeftLock = leftLock;
    }

    public void setScreenWidth(int width){
        this.Screen_Width = width;
    }

    public void setMarioX_counter(int x){
        this.MarioX_counter = x;
    }

    public void setStand(boolean stand) {
        this.stand = stand;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setInitY(int init_y) {
        this.init_y = init_y;
    }

    public void setUpFlag(boolean up_flag) {
        this.up_flag = up_flag;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void setPosX(Rect rect) {
        this.posX = rect.centerX();
    }
    public void setPosY(Rect rect) {
        this.posY = rect.centerY();
    }

    public void setMoveX(int direction) {
        if (direction == -1) {
            moveX = 0;
        }
        else {
            moveX = (-2 * direction + 1) * velX;
        }
    }


    //setting mode if little or big mario
    public void setMode(int mode) {
        this.mode = mode;
        Rect newRect = new Rect(rect.left, rect.bottom - little_mario_height - mode * height_diff, rect.right, rect.bottom);
        rect = newRect;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                mario[i][j].setPos(rect);
            }
        }
    }

    public void moveScreen(int x) {
        Screen.offset(x, 0);
    }

    //jumping
    public void jump(){

        if (jump == true){
            jump_distance_border = init_y + max_jump;   //certain distance jumping up

            if((getRect().centerY() > jump_distance_border) && (up_flag == true) ) {
                rect.offset(0, jump_offset);
            }
            else if(up_flag == false){  //falling down again

               if (getMarioPosY() >= mario_init_Y && mode == 0){
                    rect.offsetTo(getMarioPosX(), mario_init_Y);
                    lock = false;
                    jump = false;
               }
               else if(getMarioPosY() >= mario_init_Y-50 && mode == 1){
                    rect.offsetTo(getMarioPosX(), mario_init_Y-150);
                    lock = false;
                    jump = false;
                }
               else if(stop_flag){
                    lock = false;
                    jump = false;
                    stop_flag = false;
               }
               else{

                    rect.offset(0, -jump_offset);
               }
                if(getMarioPosY() >= init_y){
                    lock = false;
                    jump = false;

                }
            }

            else {
                up_flag = false;
            }

        }

        else if (block_flag_question){  //if there is a question block or brick
            if (rect.bottom < new_y_init && block_stop){
                rect.offset(0, -jump_offset);
            }
            else{
                block_stop = false;
            }

        }

        else if (block_flag_brick){
            if (rect.bottom < new_y_init && block_stop){
                rect.offset(0, -jump_offset);
            }
            else{
                block_stop = false;
            }
        }

        else if (rect.bottom < mario_init_bottom_y){
            rect.offset(0, -jump_offset);
        }
    }

    public void synPos() {
        rect = mario[mode][direction].getRect();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (i != mode && j != direction) {
                    mario[i][j].setPos(rect);
                }
            }
        }
    }

    public void drawMario(Canvas canvas) {

        jump(); //call jump

        if (Screen.left == 0){
            setOriginal_screen(true);
        }
        else{
            setOriginal_screen(false);
        }


        //checking all these conditions if have to move the screen then stop mario etc
        if (check_lock_counter == 2){

            check_lock = true;
        }

        if (check_lock && MarioX_counter < 0) {
            checkMario();
        }

        if (check_lock && (getMarioPosX() > Screen_Width/5 * 4) && RightLock){
            checkMario();
            MarioX_counter += (-2 * direction + 1) * velX;
            moveScreen((-2 * direction + 1) * velX);
        }

        if (check_lock && (getMarioPosX() < Screen_Width/5) && LeftLock && (!original_screen)){
            checkMario();
            MarioX_counter += (-2 * direction + 1) * velX;
            moveScreen((-2 * direction + 1) * velX);
        }

        if (jump) {
            mario[mode][direction].animMove(moveX, moveY);
            if(moveX > 0){
                MarioX_counter += moveX;

            }
            else if(moveX < 0){
                MarioX_counter += moveX;
            }
            mario[mode][direction].drawFrame(canvas, 8);

        }
        else if (stand) {
            mario[mode][direction].drawFrame(canvas, 7 * direction);
        }
        else {
            mario[mode][direction].animMove(moveX, moveY);
            if(moveX > 0){
                MarioX_counter += moveX;

            }
            else if(moveX < 0){
                MarioX_counter += moveX;
            }
            mario[mode][direction].drawAnim(canvas);
        }
        //System.out.println("X counter = "+MarioX_counter);

        check_lock_counter ++;


    }

    public void checkMario(){
        setMoveX(-1);
        check_lock = false;
        check_lock_counter = 0;

    }

}



