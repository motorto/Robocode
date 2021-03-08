package man;
import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {
	private int moveDirection = 1; // change direction
	private int shots = 0; // Number of shots fired
	private int enemyEnergy = 100; //inicial enemy energy

	public void run() {
		// color of body / gun / radar / bullet /  arc Scan
		setColors(Color.pink,Color.pink,Color.white,Color.pink,Color.pink);

		setAdjustGunForRobotTurn(true); //gun independent from body
		setAdjustRadarForGunTurn(true); //radar independent from gun

		turnRadarRightRadians(Double.POSITIVE_INFINITY); //turns until scanned robot

		while (true) {
			scan();//as there are only one enemy force scanned robot event 
		}
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		int lastEnemyEnergy = enemyEnergy;
		boolean lowEnergy = false;

		//get turn required for scan -> (enemy angle - current radar heading)
		double radarTurn = getHeading() + event.getBearing()-getRadarHeading();

		setTurnRadarRight(normalizeBearing(radarTurn));

		//stay (parallel) with enemy
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		//get turn required for gun -> (enemy angle - current gun heading)
		setTurnGunRight(normalizeBearing(
				(getHeading() + event.getBearing()) - getGunHeading()));
		//move in circles
		setAhead(200 * moveDirection ); 

		if (getEnergy() < 1.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy , we don't shot we just move
			if (event.getDistance() < 20)//bot's touching
				fire(Rules.MAX_BULLET_POWER);
			else if(event.getDistance() < 100 ) { //bot's close
				fire(3);
			}
			else //bot's far away
				fire(1);

			shots++;
		}


		enemyEnergy  = (int) event.getEnergy();
		// if enemyShoots we change direction with 33.(3)% chance
		if ((lastEnemyEnergy > enemyEnergy) && random(shots)) {
			moveDirection *= -1;
		}
		AvoidWall();
	}

	public boolean random(int x) {
		if (x % 3 == 0) return true;
		else return false;
	}

	//change direction on hit wall
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

	//find next enemy
	public void onRobotDeath(RobotDeathEvent event) {
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	//dance
	public void onWin(WinEvent event) {
		setMaxVelocity(0);
		while(true) {
			turnRadarLeft(30);
			turnRadarRight(60);
			turnRadarLeft(30);
		}
	}

	//avoids hitting with max speed 
	public void AvoidWall() {
		boolean closeToWall = false;
		final int distance = 40;
		double Xpos = getX();
		double Ypos = getY();
		if (Xpos< distance 						|| Ypos<distance || 
			Xpos>getBattleFieldWidth()-distance || Ypos>getBattleFieldHeight()-distance) {
			if (closeToWall==false) {
				setMaxVelocity(3);
				closeToWall=true; 
			}
		}
		else {
			closeToWall=false;
			setMaxVelocity(Rules.MAX_VELOCITY);
		}
	}
}