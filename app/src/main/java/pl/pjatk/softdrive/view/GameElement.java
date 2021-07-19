// GameElement.java
// Represents a rectangle-bounded game element
package pl.pjatk.softdrive.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class GameElement {
   protected TrafficView view; // the view that contains this GameElement
   protected Paint paint = new Paint(); // Paint to draw this GameElement
   protected Rect shape; // the GameElement's rectangular bounds
   private float velocityY; // the vertical velocity of this GameElement
   private float velocityX; // the vertical velocity of this GameElement
   private int soundId; // the sound associated with this GameElement
   private int x;
   private int y;
   private int width;
   private int height;



   ///////////////////my attrs
   protected boolean isCollision = false;
   protected double collisionSensitive = 10.0;
   //protected Motorcycle motor;


   // public constructor
   public GameElement(TrafficView view, int color, int soundId, int x,
      int y, int width, int height, float velocityX, float velocityY) {
      this.view = view;
      paint.setColor(color);
      shape = new Rect(x, y, x + width, y + height); // set bounds
      this.soundId = soundId;
      this.velocityX = velocityX;
      this.velocityY = velocityY;
   }

   // update GameElement position and check for wall collisions
   public void update(double intervalX, double intervalY) {
      // update vertical position
      shape.offset((int) (velocityX * intervalX), (int) (velocityY * intervalY));

      // if this GameElement collides with the wall, reverse direction
      if (shape.top < 0 && velocityY < 0 ||
         shape.bottom > view.getScreenHeight() && velocityY > 0)
         velocityY *= -1; // reverse this GameElement's velocity


      //////////////////my collisions

   }

   // draws this GameElement on the given Canvas
   public void draw(Canvas canvas) {
      canvas.drawRect(shape, paint);
   }

   // plays the sound that corresponds to this type of GameElement
   public void playSound() {
      view.playSound(soundId);
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
}

/*********************************************************************************
 * (C) Copyright 1992-2016 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/
