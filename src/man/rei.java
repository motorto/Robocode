package man;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.*;
import robocode.*;
import robocode.util.Utils;

public class rei extends AdvancedRobot {
    /* MERDA QUE ADICIONEI */
	final static double BULLET_SPEED=20-3*1;//Formula for bullet speed.
    double oldEnemyHeading;
    
    // -------------------------------
    
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
		// --------------------------------
        double absBearing=event.getBearingRadians()+getHeadingRadians();
        //Finding the heading and heading change.
        double enemyHeading = event.getHeadingRadians();
        double enemyHeadingChange = enemyHeading - oldEnemyHeading;
        oldEnemyHeading = enemyHeading;
		// --------------------------------


		//get turn required for scan -> (enemy angle - current radar heading)
		double radarTurn = getHeading() + event.getBearing()-getRadarHeading();

		setTurnRadarRight(normalizeBearing(radarTurn));

		//stay (parallel) with enemy
		setTurnRight(normalizeBearing(event.getBearing() + 90 - (15 * moveDirection)));
		//move in circles
		setAhead(200 * moveDirection );
		// --------------------------------------

		double deltaTime = 0;
        double predictedX = getX()+event.getDistance()*Math.sin(absBearing);
        double predictedY = getY()+event.getDistance()*Math.cos(absBearing);
        while((++deltaTime) * BULLET_SPEED <  Point2D.Double.distance(getX(), getY(), predictedX, predictedY)){

            //Add the movement we think our enemy will make to our enemy's current X and Y
            predictedX += Math.sin(enemyHeading) * event.getVelocity();
            predictedY += Math.cos(enemyHeading) * event.getVelocity();


            //Find our enemy's heading changes.
            enemyHeading += enemyHeadingChange;

            //If our predicted coordinates are outside the walls, put them 18 distance units away from the walls as we know
            //that that is the closest they can get to the wall (Bots are non-rotating 36*36 squares).
            predictedX=Math.max(Math.min(predictedX,getBattleFieldWidth()-18),18);
            predictedY=Math.max(Math.min(predictedY,getBattleFieldHeight()-18),18);

        }
        double aim = Utils.normalAbsoluteAngle(Math.atan2(  predictedX - getX(), predictedY - getY()));
        setTurnGunRightRadians(Utils.normalRelativeAngle(aim - getGunHeadingRadians()));
		// --------------------------------------

		if (getEnergy() < 1.0) 
			lowEnergy = true;
		else
			lowEnergy = false;

		if (!lowEnergy) { // If low energy, we don't shot we just move
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

	//victory dance
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