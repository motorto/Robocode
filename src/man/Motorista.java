package man;

import java.awt.*;
import robocode.*;

public class Motorista extends AdvancedRobot {
	byte dir = 1;
	public void run() {
		setColors(Color.black, Color.black, Color.white); // body / gun / radar
		setBulletColor(Color.green);
		
		setAdjustGunForRobotTurn(true);
		
		// Robot Main Loop
		while (true) {
			turnGunLeft(360);
			ahead(50);
			back(50);
		}
	}

	// Enemy found
	public void onScannedRobot(ScannedRobotEvent event) {
		boolean lowEnergy = false;
		
		setTurnGunRight(getHeading() + event.getBearing() - getRadarHeading());

		if (getEnergy() < 30.0) {
			lowEnergy = true;
		} else lowEnergy = false;
		
		if (!lowEnergy) fire(Math.min(400 / event.getDistance(), 2));
		else fire(1);
		
		back(50);
		turnRight(event.getBearing() + 90); // Mete adversario Paralelo
		ahead(50);
		
	}

	public void onHittWall(HitWallEvent event) {
		turnRight(event.getBearing() * -1); // Vira para a direção contraria
		ahead(50);
	}

//	  // Quando acerta o tiro 
//	  public void onHitRobot(HitRobotEvent event){ }

	public void onHitByBulletEvent(HitByBulletEvent event) {
		dir*=-1;
		if (dir == 1) ahead(50);
		else back(50);
	}
	
	public void onRobotDeath(RobotDeathEvent event) { 
		turnGunRight(720);
	}
	
}