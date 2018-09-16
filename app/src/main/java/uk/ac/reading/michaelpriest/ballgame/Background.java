package uk.ac.reading.michaelpriest.ballgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Background {

    private Bitmap image;
    private int x;
    private int y;
    private double dy;
    private int width;
    private int height;

    public Background(Bitmap res) {
        image = res;
    }

    public void scale(int width, int height) {
        image = Bitmap.createScaledBitmap(image, width, height, true);
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if(y > 0) {canvas.drawBitmap(image, x, y - height, null);}
    }

    public void update() {
        y+=dy;
        if(y > height) {y=0;}
    }

    public void setVector(double dy) {
        this.dy = dy;
    }
}
