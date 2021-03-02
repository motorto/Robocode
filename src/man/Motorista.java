package man;

import java.awt.*;
import robocode.*;

public class Motorista extends AdvancedRobot {
	boolean locked = false;
	public void run() {

		setColors(Color.black, Color.black, Color.white); // body / gun / radar

		// Robot Main Loop
		while (true) {
			move();
		}

	}

	public void move() {
		if(locked == false) turnGunRight(720);
		ahead(100);
		turnLeft(90);
	}

	// Enemy found
	public void onScannedRobot(ScannedRobotEvent event) {
		boolean lowEnergy = false;
		double maxPower = Rules.MAX_BULLET_POWER;
		double distance = event.getDistance();
		double bearing = event.getBearing();
		
		setTurnGunRight(getHeading() - getRadarHeading() + bearing);
		locked = true;
		if (getEnergy() < 30) {
			lowEnergy = true;
		} else lowEnergy = false;

		if (distance < 100 && lowEnergy == false) {
			fire(maxPower);
		} 
		else if(distance < 300 && lowEnergy == false){
			fire(2);
		}
		else { 
			locked = false;
			fire(1);
		}
	}

	public void onHittWall(HitWallEvent event) {
		double tmp = getHeading() + 135; 
		if (tmp >= 360) tmp-=360;
		turnRight(tmp);
		ahead(50);
		fire(1); 
	}

	
//	  // Quando acerta o tiro 
//	  public void onHitRobot(HitRobotEvent event){ }

	public void onHitByBulletEvent(HitByBulletEvent event) {
		back(100);
	}
	
	public void onRobotDeath(RobotDeathEvent event) { 
		setTurnRadarRight(720); 
	}

}
