package man;

import java.awt.*;
import robocode.*;

public class Motorista extends AdvancedRobot {
	boolean locked = false;
	byte dir = 1;
	public void run() {
		setColors(Color.black, Color.black, Color.white); // body / gun / radar
		setBulletColor(Color.green);
		
		setAdjustGunForRobotTurn(true);
		
		// Robot Main Loop
		while (true) {
			turnGunLeft(180);
		}
	}

	// Enemy found
	public void onScannedRobot(ScannedRobotEvent event) {
		boolean lowEnergy = false;
		double maxPower = Rules.MAX_BULLET_POWER;
		
		setTurnGunRight(getHeading() + event.getBearing() - getRadarHeading());
		turnRight(event.getBearing() + 90);
		
		
		if (getEnergy() < 30.0) {
			lowEnergy = true;
		} else lowEnergy = false;
		
		if (event.getDistance() < 100.0 && lowEnergy == false) {
			fire(maxPower);
		} 
		else if(event.getDistance() < 300.0 && lowEnergy == false){
			fire(2);
		}
		else { 
			fire(1);
		}
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
		locked=false;
	}
	
}