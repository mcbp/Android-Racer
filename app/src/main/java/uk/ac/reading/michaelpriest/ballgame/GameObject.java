package uk.ac.reading.michaelpriest.ballgame;


import android.graphics.Rect;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected double dx;
    protected double dy;
    protected int width;
    protected int height;

    public void setX(int x) {this.x = x;}
    public int getX() {return this.x;}

    public void setY(int y) {this.y = y;}
    public int getY() {return this.y;}

    public void setDX(double dx) {this.dx = dx;}
    public double getDX() {return this.dx;}

    public void setDY(double dy) {this.dy = dy;}
    public double getDY() {return this.dy;}

    public void setWidth(int width) {this.width = width;}
    public int getWidth() {return this.width;}

    public void setHeight(int height) {this.height = height;}
    public int getHeight() {return this.height;}

    public Rect getRectangle() { return new Rect(x, y, x+width, y+height);}
}
