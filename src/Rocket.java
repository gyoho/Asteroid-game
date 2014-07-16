import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ListIterator;


public class Rocket implements ExistIn2D {
	private double posX, posY;	// position
	private double dx, dy;		// position change rate
	private int size = 5;		// fixed size
	
	// constructor
	public Rocket(double iPosX, double iPosY, double iDx, double iDy) {
		posX = iPosX;
		posY = iPosY;
		dx = iDx;
		dy = iDy;
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
		return size;
	}

	
	// move rocket by its change rate
	public void move() {
		posX += dx;
		posY += dy;
	}
	
	// rocket is gone when hits asteroid
	public void hitAsteroid() {
		size = 0;
	}
	
	/**The rocket will be given the collection of asteroids each time it moves, 
	so that it can check to see whether the asteroid has been hit. 
	If so, the rocket will notify the asteroid.**/
	public void checkHit(List<ExistIn2D> asteroids) {
		/*TODO << start with the beginning of the linked list asteroids >>*/
		ListIterator<ExistIn2D> itr = asteroids.listIterator();
		
		/*TODO << if asteroids list has more elements >>*/
		while(itr.hasNext()) {
			/*TODO << get the next element >>*/
			Asteroid asteroid = (Asteroid) itr.next();	// Linked-list only stores Object. Need to cast down 
			if (asteroid.isCloseTo(this)) {				// if the calling object is close to any asteroid
				asteroid.hitRocket();
				hitAsteroid();
			}
		}
	}
	
	@Override
	public void drawItself(Graphics g) {
		// draw outline with a color
		g.setColor(Color.WHITE);
		g.drawOval((int) posX, (int) posY, size, size);
		
		// fill the circle with a color
		g.setColor(Color.RED);
		g.fillOval((int) posX, (int) posY, size, size);
	}
}