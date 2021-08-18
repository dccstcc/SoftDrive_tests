package pl.pjatk.softdrive.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Base for object to draw on screen
 * @author Dominik Stec
 */
public class UiElement {
   protected UiView view; // the view that contains this UiElement
   protected Paint paint = new Paint(); // Paint to draw this UiElement
   protected Rect shape; // the UiElement's rectangular bounds
   private float velocityY; // the vertical velocity of this UiElement
   private float velocityX; // the vertical velocity of this UiElement
   private int soundId; // the sound associated with this UiElement
   private int x;
   private int y;
   private int width;
   private int height;

   /**
    * Object to draw on screen configuration
    * @param view context of view controller
    * @param color TODO object color
    * @param soundId TODO object sounds
    * @param x x coordinate position
    * @param y y coordinate position
    * @param width object width
    * @param height object height
    * @param velocityX TODO object move velocity horizontal
    * @param velocityY TODO object move valocity vertical
    */
   public UiElement(UiView view, int color, int soundId, int x,
      int y, int width, int height, float velocityX, float velocityY) {
      this.view = view;
      paint.setColor(color);
      shape = new Rect(x, y, x + width, y + height); // set bounds
      this.soundId = soundId;
      this.velocityX = velocityX;
      this.velocityY = velocityY;
   }

   /**
    * Set actual position
    * @param coordX x coordinate position
    * @param coordY y coordinate position
    */
   public void update(int coordX, int coordY) {
      shape.offset(coordX, coordY);
   }

   /**
    * draw rectangle with color on canvas
    * @param canvas draw object space
    */
   public void draw(Canvas canvas) {
      canvas.drawRect(shape, paint);
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
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

   public Rect getShape() {return shape;}

   public void setShape(Rect shape) {this.shape = shape;}
}
