package pl.pjatk.softdrive.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

public class Motorcycle {

    private int width;
    private int height;
    private int xCoord;
    private int yCoord;

    Display display;
    public int displayWidth;
    public int displayHeight;

    private Rect motor;
    Paint paint = new Paint();

    public Motorcycle(Context context, Canvas canvas, int width, int height) {

        display = new Display(context);
        displayWidth = display.getDisplayWidth();
        displayHeight = display.getDisplayHeight();

        xCoord = displayWidth/2 - width/2;
        yCoord = displayHeight/2 - height/2;

        motor = new Rect(xCoord, yCoord, xCoord + width, yCoord + height);
        paint.setColor(Color.BLUE);

        canvas.drawRect(motor, paint);
    }

    public Rect getMotor() {
        return motor;
    }

    public void setMotor(Rect motor) {
        this.motor = motor;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
