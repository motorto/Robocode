package man;
import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {
	private int moveDirection = 1;

	public void run() {
		setColors(Color.pink,Color.pink,Color.white); // body / gun / radar
		setBulletColor(Color.pink);

		while (true) {
			turnGunRightRadians(1);
		}
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		int enemyEnergy = 0;
		boolean lowEnergy = false;
		enemyEnergy  = (int) event.getEnergy();
		
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		setAhead(200 * moveDirection); // move in circles;

		if (getEnergy() < 2.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy , we dont shot we just move
			if(event.getDistance() < 120 && Math.random() > .85) {
				fire(3);
			}
			else 
				fire(1);
		}

		// if enemyShoots we change dir with 15% chance
		if ((enemyEnergy < (int)event.getEnergy()) && Math.random() > .50) {
			moveDirection *= -1;
		}
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
	}

	public void onHitWall(HitWallEvent event) {
		moveDirection*=-1;
	}

	// normalize all angles in [-180 and 180]
	double normalizeBearing(double angle) {
		while (angle > 180) {
			angle -= 360;
		}
		while (angle < -180) {
			angle += 360;
		}
		return angle;
	}
}