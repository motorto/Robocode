package man;

import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {

	public void run() {

		setColors(Color.black, Color.black, Color.white); // body / gun / radar

		// Robot Main Loop
		while (true) {
			move();
		}

	}

	public void move() {
		turnGunRight(720);
//		ahead(200);
	//	turnRight(45);
	}

	// Quando encontra o adversario
	public void onScannedRobot(ScannedRobotEvent event) {

		double maxPower = Rules.MAX_BULLET_POWER;
		double distance = event.getDistance();
		double bearing = event.getBearing();
		
		setTurnGunRight(getHeading() - getRadarHeading() + bearing);
		//dar lock no rival
		if (distance < 100 ) {
			fire(maxPower);
		} 
		else if(distance < 300){
			fire(2);
			ahead(distance/3);
		}
		else { 
			ahead(distance/3);
			//fire(1);
		}
	}

	public void onHittWall(HitWallEvent event) {
		turnLeft(180);
		fire(1);
	}

	
//	  // Quando acerta o tiro 
//	  public void onHitRobot(HitRobotEvent event){ }

	public void onHitByBulletEvent(HitByBulletEvent event) {
			turnRight(90);
			ahead(150);
	}
	
	public void onRobotDeath(RobotDeathEvent event) { 
		setTurnRadarRight(720); 
	}

}
