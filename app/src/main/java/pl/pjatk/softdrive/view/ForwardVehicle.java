package pl.pjatk.softdrive.view;

import android.content.Context;

public class ForwardVehicle extends UiElement {

    int displayWidth;
    int displayHeight;
    int xCoord;
    int yCoord;

    int height;

    // maximum distance read by senor
    private final int maxDistance = 4000;

    public ForwardVehicle(Context context, UiView view, int color, int soundId, int x, int y, int width, int height, float velocityX, float velocityY) {
        super(view, color, soundId, x, y, width, height, velocityX, velocityY);
        this.height = height;
    }

    public void updateForwardVehiclePosition(int restDistance, int motorcyclePositionY, int distanceOffset) {
        // update vertical position
        // scale sensor distance value to screen height
        double distance = (restDistance * motorcyclePositionY) / maxDistance;
        distance = Math.floor(distance);
        int revertDistance = (int) (motorcyclePositionY - distance);
        revertDistance -= height;
        update(0, revertDistance);
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
