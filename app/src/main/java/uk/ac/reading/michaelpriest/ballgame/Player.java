package uk.ac.reading.michaelpriest.ballgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject{

    private Bitmap image;
    private int screenWidth;
    private int screenHeight;
    private boolean leftActive;
    private boolean rightActive;

    public Player(Bitmap res, int w, int h) {
        image = res;
        setWidth(res.getWidth());
        setHeight(res.getHeight());
        this.screenWidth = w;
        this.screenHeight = h;
        setDX(15);

        //set intial x  and y position
        x = (w / 2) - (getWidth() / 4);
        y = h / 4 * 3;
    }

    public void scale() {
        //scale btimap relative to screen width and height
        image = Bitmap.createScaledBitmap(image, (int)(screenWidth / 7), (int)(screenWidth / 7 * 1.9), true);
        setWidth(image.getWidth());
        setHeight(image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        //player holds left
        if (leftActive) {
            x += getDX();
        }
        //player holds right
        if (rightActive) {
            x -= getDX();
        }
    }

    public void setLeftActive(boolean a) {this.leftActive = a;}

    public void setRightActive(boolean a) {this.rightActive = a;}

    public void setImage(Bitmap b) {this.image = b;}

}
