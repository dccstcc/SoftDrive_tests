package pl.pjatk.softdrive.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import pl.pjatk.softdrive.R;

/**
 * Forward car specification
 * @author Dominik Stec
 * @see UiElement
 */
public class ForwardVehicle extends UiElement {

    private int displayWidth;
    private int displayHeight;
    private int xCoord;
    private int yCoord;

    private int height;

    private Context context;

    /**
     * maximum distance read by sensor
     */
    private final int maxDistance = 4000;

    public ForwardVehicle(Context context, UiView view, int color, int soundId, int x, int y, int width, int height, float velocityX, float velocityY) {
        super(view, color, soundId, x, y, width, height, velocityX, velocityY);
        this.height = height;
        this.context = context;
    }

    /**
     * Set and update actual forward car position according to motorcycle position
     * @param restDistance Distance from sensor
     * @param motorcyclePositionY Position of motorcycle
     * @param distanceOffset TODO when motorcycle position changes
     */
    public void updateForwardVehiclePosition(int restDistance, int motorcyclePositionY, int distanceOffset) {
        // update vertical position
        // scale sensor distance value to screen height
        double distance = (restDistance * motorcyclePositionY) / maxDistance;
        distance = Math.floor(distance);
        int revertDistance = (int) (motorcyclePositionY - distance);
        revertDistance -= height;
        update(0, revertDistance);
    }

    /**
     * Draw actual forward car position on screen
     * @param canvas draw object space
     */
    public void draw(Canvas canvas) {

        Drawable carPng = context.getResources().getDrawable(R.drawable.car_top);
        carPng.setBounds(super.getShape());
        carPng.draw(canvas);
    }

    public double getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public double getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public double getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public double getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }
}
