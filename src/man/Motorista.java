package man;
import java.awt.Color;
import robocode.*;

 public class Motorista extends Robot {
	 

	      public void run() {
	    	     setColors(Color.pink,Color.pink,Color.white); // body / gun / radar

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