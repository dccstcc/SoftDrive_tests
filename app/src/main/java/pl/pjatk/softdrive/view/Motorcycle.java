package pl.pjatk.softdrive.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import pl.pjatk.softdrive.R;

/**
 * Motorcycle object representation
 * @author Dominik Stec
 */
public class Motorcycle {

    private int width;
    private int height;
    private int xCoord;
    private int yCoord;

    private Display display;
    private int displayWidth;
    private int displayHeight;

    private Rect motor;

    /**
     * Draw motorcycle on given position with given size
     * @param context this Android application Context object
     * @param canvas draw object space
     * @param width motorcycle object width
     * @param height motorcycle object height
     */
    public Motorcycle(Context context, Canvas canvas, int width, int height) {

        display = new Display(context);
        displayWidth = display.getDisplayWidth();
        displayHeight = display.getDisplayHeight();

        xCoord = (int) (displayWidth/2 - width/2);
        yCoord = (int) (displayHeight/2 - height/2);

        // motorcycle size and position
        motor = new Rect(xCoord, yCoord, xCoord + width, yCoord + height);

        // draw motorcycle image
        Drawable motorPng = context.getResources().getDrawable(R.drawable.motorcycle_top);
        motorPng.setBounds(motor);
        motorPng.draw(canvas);
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
