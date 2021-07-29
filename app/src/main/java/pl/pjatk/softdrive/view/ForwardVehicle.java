package pl.pjatk.softdrive.view;

import android.content.Context;

public class ForwardVehicle extends GameElement{

    int displayWidth;
    int displayHeight;
    int xCoord;
    int yCoord;

    private final int maxDistance = 4000;

    public ForwardVehicle(Context context, TrafficView view, int color, int soundId, int x, int y, int width, int height, float velocityX, float velocityY) {
        super(view, color, soundId, x, y, width, height, velocityX, velocityY);
    }

    ///////////////////////////////////my update method
    // update GameElement position and check for wall collisions
    public void updateForwardVehiclePosition(int distance, int motorcyclePositionY) {
        // update vertical position
        // scale sensor distance value to screen height
        distance = (distance * motorcyclePositionY) / maxDistance;
        shape.offset(0, distance);

//        // if this GameElement collides with the wall, reverse direction
//        if (shape.top < 0 && velocityY < 0 ||
//                shape.bottom > view.getScreenHeight() && velocityY > 0)
//            velocityY *= -1; // reverse this GameElement's velocity


        //////////////////my collisions

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
