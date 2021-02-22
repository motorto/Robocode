package man;
import java.awt.Color;

import robocode.*;

 public class Motorista extends Robot {
     public void run() {
    	 
    	 setColors(Color.pink,Color.pink,Color.gray); // body / gun / radar
    	 
    	 // Robot Main Loop
         while (true) {
             ahead(100);
             turnGunRight(360);
             back(100);
             turnGunRight(360);
         }
     }

     public void onScannedRobot(ScannedRobotEvent e) {
         fire(1);
     }
 }