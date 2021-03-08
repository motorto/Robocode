package man;
import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {
	private int moveDirection = 1; // change dir
	public void run() {
		setColors(Color.pink,Color.pink,Color.white); // body / gun / radar
		setBulletColor(Color.pink);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		turnRadarRightRadians(Double.POSITIVE_INFINITY);
		while (true) {
			//scan();
			AvoidWall();
		}
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		int enemyEnergy = 0;
		boolean lowEnergy = false;
		enemyEnergy  = (int) event.getEnergy();
		
		double radarTurn = getHeading() + event.getBearing()-getRadarHeading();
		
		setTurnRadarRight(normalizeBearing(radarTurn));
		
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		setTurnGunRight(normalizeBearing(
				(getHeading() + event.getBearing()) - getGunHeading()));
		setAhead(200 * moveDirection ); // move in circles;

		if (getEnergy() < 2.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy , we dont shot we just move
			if (event.getDistance() < 30)//bots touching
				fire(Rules.MAX_BULLET_POWER);
			else if(event.getDistance() < 120 ) {
				fire(3);
			}
			else 
				fire(0.5);
		}

		// if enemyShoots we change dir with 50% chance
		if ((enemyEnergy < (int)event.getEnergy()) && Math.random() > .50) {
			moveDirection *= -1;
		}
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
	
	public void onRobotDeath(RobotDeathEvent event) {
		ahead(20000);
	}

	
	  // Em vez de um avoidWall , talvez um brake antes de bater na parede nao sei (reduzir a velocidade
	  //Ao minimo para tirar menos dano.
	 //  setMaxVelocity(Math.abs(getTurnRemaining()) < 40 ? preferredVelocity : 0.1);
	public void AvoidWall() {
		boolean closeToWall = false;
		final int distance = 24;
		if (getX()< distance 						|| getY()<distance || 
			getX()>getBattleFieldWidth()-distance || getY()>getBattleFieldHeight()-distance) {
		    if (closeToWall==false) {
		        setMaxVelocity(0.5);
		        closeToWall=true; 
		        System.out.println("Near wall");
		    }
		    else {
		        System.out.println("Still near wall");
		    }
		}
		else {
		    closeToWall=false;
		    setMaxVelocity(8);
		}
	}
}