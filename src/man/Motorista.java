package man;
import java.awt.Color;
import robocode.*;

 public class Motorista extends Robot {
	 
     public void run() {

         setColors(Color.green,Color.green,Color.white); // body / gun / radar

         // Robot Main Loop
         while (true) {
             
             ahead(100);
             turnRight(45);
             turnGunRight(45);
             ahead(100);
             turnRight(45);
             turnGunRight(45);
             
         }
     }

     public void onScannedRobot(ScannedRobotEvent event) {
         
         double distance = event.getDistance();
         double bearing = event.getBearing();
         if (distance < 60) {
             fire(3);
             if(!(bearing > -90 && bearing < 90 )) {
                 turnRight(180);
             }
         }else if(distance > 250 ) {
             ahead(150);
             if(!(bearing > -90 && bearing < 90 )) {
                 turnRight(180);
             }
         }
         else {
             fire(1);
         }
         
     }
     //acertar tiro 
     public void onHitRobot(HitRobotEvent event){
         if(event.getBearing() > -90 && event.getBearing() <90 ) {
             back(100);
         }
         else {
             ahead(100);
         }
     }

 }
