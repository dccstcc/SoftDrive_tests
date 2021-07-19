package pl.pjatk.softdrive.view;

import android.content.Context;

public class ForwardVehicle extends GameElement{

    Display display;
    int displayWidth;
    int displayHeight;
    int xCoord;
    int yCoord;

    public ForwardVehicle(Context context, TrafficView view, int color, int soundId, int x, int y, int width, int height, float velocityX, float velocityY) {
        super(view, color, soundId, x, y, width, height, velocityX, velocityY);

//        display = new Display(context);
//        displayWidth = display.getDisplayWidth();
//        displayHeight = display.getDisplayHeight();

//        xCoord = displayWidth/2 - width/2;
        //yCoord = displayHeight/2 - height/2;

//        super.setX(xCoord);
//        super.setY(0);

//        setxCoord(xCoord);
//        setyCoord(yCoord);

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
