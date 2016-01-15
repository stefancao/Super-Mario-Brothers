package eecs40.super_mario_brothers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Created by swear_000 on 5/16/2015.
 */
public class Level_2 extends Dungeon {

    protected Level_2(Context context) {
        super(context);
        tileSet = readBitmap(R.drawable.tileset_2);
        tileSetWidthCount = tileSet.getWidth() / TILESET_WIDTH;
        tileSetHeightCount = tileSet.getHeight() / TILESET_HEIGHT;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);

        Bitmap bitmap = readBitmap(R.drawable.level_2);
        int width = getWidth();
        int height = getHeight();
        bg = new Image(bitmap, new Rect(0, 0, width, height));

        tileWidth = width / TILE_WIDTH_COUNT;
        tileHeight = height / TILE_HEIGHT_COUNT;


        //calling add bricks and objects
        addBrickArray(2);
        addObjectArray(2);


       // bg = new Image(readBitmap(R.drawable.dashtillpuffwallpaper), new Rect(0, 0, getWidth(), getHeight()));

    }

    @Override
    public void tick(Canvas canvas) {
        bg.drawImage(canvas);
        drawObjects(canvas);
        drawScore(canvas);
        super.tick(canvas);
    }
}
