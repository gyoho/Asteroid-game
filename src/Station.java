import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ListIterator;

public class Station implements ExistIn2D {
	
	private double posX, posY;	// fixed position
	private int armSize, bodySize;
	private double rocketSpeed;
	private double angle = Math.PI/2.0;	// put cannon to 90 degree
	private int hits = 100;
	
	public Station(double iPosX, double iPosY, int newArmSize, int newBodySize, double newRocketSpeed, int newLifePoint) {
		posX = iPosX;
		posY = iPosY;
		armSize = newArmSize;
		bodySize = newBodySize;
		rocketSpeed = newRocketSpeed;
		hits = newLifePoint;
	}
	
	// getter
	@Override
	public double getPositionX() {
		return posX;
	}

	@Override
	public double getPositionY() {
		return posY;
	}
	
	@Override
	public int getSize() {
		return (armSize + bodySize);
	}
	
	public double getRocketSpeed() {
		return rocketSpeed;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double getHits() {
		return hits;
	}
	
	//setter for adapting new window size
	public void setPosition(double newPosX, double newPosY) {
		posX = newPosX;
		posY = newPosY;
	}
	
	
	/*cannon only moves 1st and 2nd quadrant*/
	public boolean isInUpperHalf() {
		return (angle >= 0 && angle <= Math.PI);
	}
	
	public void moveLeft() {
		//if(isInUpperHalf())
			angle = angle + 0.1;
	}
	
	public void moveRight() {
		//if(isInUpperHalf())
			angle = angle - 0.1;
	}
	
	
	/*Every time fire cannon, set the rocket's position and speed
	  and add rocket to its list to keep track of them*/
	public void fire(List<ExistIn2D> rockets) {
		// rocket goes same direction as gun is pointing
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		
		// rocket is released from the edge of the arm
		Rocket rocket = new Rocket(posX+bodySize*cosAngle, posY-bodySize*sinAngle, rocketSpeed*cosAngle, -rocketSpeed*sinAngle);
		
		/*TODO << add element(rocket) to linked-list rockets >>*/
		rockets.add(rocket);
	}
	
	// Traverse the list of asteroid to check if close enough to hit the station
	public void checkHit(List<ExistIn2D> asteroids) {
		ListIterator<ExistIn2D> itr = asteroids.listIterator();
		while(itr.hasNext()) {
			Asteroid asteroid = (Asteroid) itr.next();
			if(asteroid.isCloseTo(this)) {
				hits += -asteroid.getSize();
				asteroid.hitStation();
			}
		}
	}
	

	@Override
	public void drawItself(Graphics g) {
		g.setColor (Color.BLUE);
		double width = getSize() * Math.sin(angle);
		double height = getSize() * Math.cos(angle);
		//TODO: draw arc for the station
		g.drawArc((int)posX-50/2, (int)posY, 50, 30, 0, 180);
		g.drawLine((int)posX, (int)posY, (int)(posX+height), (int)(posY-width));
		if(hits < 0) {
			hits = 0;
		}
		g.drawString("Hits: " + hits, 0, (int)(posY));
	}

}
