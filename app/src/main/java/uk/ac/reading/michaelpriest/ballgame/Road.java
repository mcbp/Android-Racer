package uk.ac.reading.michaelpriest.ballgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Road extends GameObject{

    private Bitmap image;
    private int screenWidth;
    private int screenHieght;

    public Road(Bitmap res, int w, int h) {
        image = res;
        screenWidth = w;
        screenHieght = h;
    }

    public void scale(int width, int height) {
        image = Bitmap.createScaledBitmap(image, screenWidth - 200, screenHieght, true);
        setWidth(image.getWidth());
        setHeight(image.getHeight());
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 100, y, null);
        //if image has moved down create another image directly above it
        if(y > 0) {canvas.drawBitmap(image, 100, y - screenHieght, null);}
    }

    public void update() {
        y+=dy;
        //if image is completely off screen set it back to top
        if(y > screenHieght) {y=0;}
    }

    public void setVector(double dy) {this.setDY(dy);}
}
