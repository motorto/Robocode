package man;
import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {
	private int moveDirection = 1; // change dir
	public void run() {
		setColors(Color.pink,Color.pink,Color.white); // body / gun / radar
		setBulletColor(Color.pink);
		setAdjustGunForRobotTurn(true);

		while (true) {
			turnGunRightRadians(1);
			//AvoidWall();
		}
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		int enemyEnergy = 0;
		boolean lowEnergy = false;
		enemyEnergy  = (int) event.getEnergy();

		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		setAhead(200 * moveDirection ); // move in circles;

		if (getEnergy() < 2.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy , we dont shot we just move
			if (event.getDistance() < 30)
				fire(Rules.MAX_BULLET_POWER);
			else if(event.getDistance() < 120 && Math.random() > .85) {
				fire(3);
			}
			else 
				fire(1);
		}

		// if enemyShoots we change dir with 50% chance
		if ((enemyEnergy < (int)event.getEnergy()) && Math.random() > .50) {
			moveDirection *= -1;
		}
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
	}

	public void onHitWall(HitWallEvent event) {
		moveDirection*=-1;
	}

	// normalize all angles in [-180 and 180]
	public double normalizeBearing(double angle) {
		while (angle > 180) {
			angle -= 360;
		}
		while (angle < -180) {
			angle += 360;
		}
		return angle;
	}

	/*
	 * Em vez de um avoidWall , talvez um brake antes de bater na parede nao sei (reduzir a velocidade
	 * Ao minimo para tirar menos dano.
	 *  setMaxVelocity(Math.abs(getTurnRemaining()) < 40 ? preferredVelocity : 0.1);
	public void AvoidWall() {
		boolean closeToWall = false;
		if (getX()<30 || getY()<30 || getX()>getBattleFieldWidth()-20 || getY()>getBattleFieldHeight()-20) {
		    if (closeToWall==false) {
		        moveDirection*=-1;
		        closeToWall=true; System.out.println("Near wall");
		    }
		    else {
		        System.out.println("Still near wall");
		    }
		}
		else {
		    closeToWall=false;
		}
	}
	*/
}