package eecs40.super_mario_brothers;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Jun Li on 5/24/2015.
 */
public class GameThread extends Thread{
    private Dungeon dungeon;
    private static final int FRAME_Period = 5; //In ms

    public GameThread(Dungeon dungeon){
        this.dungeon = dungeon;
    }

    public void run(){
        SurfaceHolder sh = dungeon.getHolder();

        //Main game loop
        while(!Thread.interrupted()){
            Canvas c = sh.lockCanvas(null);
            try{
                synchronized (sh){
                    dungeon.draw(c);
                }
            } catch(Exception e){

            } finally{
                if (c != null){
                    sh.unlockCanvasAndPost(c);
                }
            }
            try{
                Thread.sleep(FRAME_Period);
            } catch(InterruptedException e){
                return;
            }
        }

    }
}
