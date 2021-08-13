// Target.java
// Subclass of GameElement customized for the Target
package pl.pjatk.softdrive.view;

public class SOUND extends GameElement {
   private int hitReward; // the hit reward for this target

   // constructor
   public SOUND(TrafficView view, int color, int hitReward, int x, int y,
                int width, int length, float velocityY) {
      super(view, color, 0, x, y, width, length, 0,
         velocityY);
      this.hitReward = hitReward;
   }

   // returns the hit reward for this Target
   public int getHitReward() {
      return hitReward;
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
