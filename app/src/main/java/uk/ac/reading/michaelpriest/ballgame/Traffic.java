package uk.ac.reading.michaelpriest.ballgame;


import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Traffic extends GameObject {

    private Bitmap image;
    private int screenWidth;

    public Traffic(Bitmap res, int w, int h, int xpos) {
        image = res;
        setWidth(res.getWidth());
        setHeight(res.getHeight());
        this.screenWidth = w;
        scale();
        y = -500;
        x = xpos;
    }

    public void scale() {
        image = Bitmap.createScaledBitmap(image, (int)(screenWidth / 7), (int)(screenWidth / 7 * 1.9), true);
        setWidth(image.getWidth());
        setHeight(image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update() {
        //increase in y direction
        y+=dy;
    }

    public void setVector(double dy) {this.setDY(dy);}

}
