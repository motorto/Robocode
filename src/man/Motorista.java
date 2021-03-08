package man;
import java.awt.Color;
import robocode.*;

public class Motorista extends AdvancedRobot {
	private int moveDirection = 1; // change dir
	private int shots = 0; // Number of shots fired
	private int enemyEnergy = 100;

	public void run() {
		setColors(Color.pink,Color.pink,Color.white); // body / gun / radar
		setBulletColor(Color.pink);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);

		turnRadarRightRadians(Double.POSITIVE_INFINITY);
		while (true) {
			scan();
		}
	}

	public void onScannedRobot(ScannedRobotEvent event) {
		int lastEnemyEnergy = enemyEnergy;
		boolean lowEnergy = false;
		
		double radarTurn = getHeading() + event.getBearing()-getRadarHeading();

		setTurnRadarRight(normalizeBearing(radarTurn));

		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		setTurnGunRight(normalizeBearing(
				(getHeading() + event.getBearing()) - getGunHeading()));
		setAhead(200 * moveDirection ); // move in circles;

		if (getEnergy() < 1.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy , we dont shot we just move
			if (event.getDistance() < 20)//bots touching
				fire(Rules.MAX_BULLET_POWER);
			else if(event.getDistance() < 120 ) {
				fire(3);
			}
			else 
				fire(0.5);
			
			shots++;
		}

		// if enemyShoots we change dir with 50% chance
		enemyEnergy  = (int) event.getEnergy();

		if ((lastEnemyEnergy > enemyEnergy) && random(shots)) {
			System.out.println("Entrei no random com o shot " + shots);
			moveDirection *= -1;
		}
		AvoidWall();
	}
	
	public boolean random(int x) {
		if (x % 3 == 0) return true;
		else return false;
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
		turnRadarRightRadians(Double.POSITIVE_INFINITY);
	}

	public void onWin(WinEvent event) {
		while(true) {
			turnRadarLeft(30);
			turnRadarRight(60);
			turnRadarLeft(30);
		}
	}


	// Em vez de um avoidWall , talvez um brake antes de bater na parede nao sei (reduzir a velocidade
	//Ao minimo para tirar menos dano.
	//  setMaxVelocity(Math.abs(getTurnRemaining()) < 40 ? preferredVelocity : 0.1);
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