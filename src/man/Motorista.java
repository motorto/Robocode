package man;

import java.awt.Color;
import robocode.*;

public class Motorista extends Robot {
	
	boolean lockedEnemy = false;

	public void run() {

		setColors(Color.black, Color.black, Color.white); // body / gun / radar

		// Robot Main Loop
		while (true) {
			move();
		}

	}

	public void move() {
		ahead(200);
		turnRight(45);
		if (!lockedEnemy) {
			turnGunRight(360);
		}
	}

	// Quando encontra o adversario
	public void onScannedRobot(ScannedRobotEvent event) {

		double maxPower = Rules.MAX_BULLET_POWER;
		double distance = event.getDistance();
		double bearing = event.getBearing();

		// Rodar para o adversario
		if (bearing < 180) 
			turnRight(bearing);
		else 
			turnLeft(bearing);

		if (distance < 100) {
			lockedEnemy=true;
			fire(maxPower);
		} else if(distance < 300){
			fire(5);
			ahead(distance/3);
		}
		else { 
			lockedEnemy=false;
			fire(1);
		}
		
	}

	public void onHittWall(HitWallEvent event) {
		turnLeft(180);
	}

	/*
	 * // Quando acerta o tiro 
	 * public void onHitRobot(HitRobotEvent event){ }
	 */

	public void onHitByBulletEvent(HitByBulletEvent event) {
			turnRight(90);
			ahead(150);
	}

}
