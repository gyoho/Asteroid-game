import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ListIterator;

public class Asteroid implements ExistIn2D{
	private double posX, posY;	// position
	private double dx, dy;		// position change rate
	private final int INITIAL_SIZE = 20;
	private int size = INITIAL_SIZE;
	
	// constructor
	public Asteroid(double iPosX, double iPosY, double iDx, double iDy) {
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

	// move asteroid by its change rate
	public void move(){
		posX += dx;
		posY += dy;
	}

	// // draw circle with its current position
	@Override
	public void drawItself(Graphics g) {
		// draw outline with a color
		g.setColor(Color.WHITE);
		g.drawOval((int) posX, (int) posY, size, size);
		
		// fill the circle with a color
		g.setColor(Color.GREEN);
		g.fillOval((int) posX, (int) posY, size, size);
	}

	// if hits asteroid, shorink size by 2
	public void hitAsteroid() {
		size += -2;
	}
	
	// if hits rocket, shrink size by 4
	public void hitRocket() {
		size += -4;
	}
	
	// if hits station, shrink all size
	public void hitStation() {
		size = 0;
	}

	// check how close to an object
	public double distanceTo(double targetPosX, double targetPosY) {
		// use Pythagorean theorem to determine distance between positions
		double distance = Math.sqrt((posX - targetPosX) * (posX - targetPosX) + (posY - targetPosY) * (posY - targetPosY));
		return distance;
	}
	
	// check if close enough to contact
	public boolean isCloseTo(ExistIn2D target) {	
		return ( distanceTo(target.getPositionX(), target.getPositionY()) <= (size/2 + target.getSize()/2) );
	}
	
	// check if close enough to hit the other asteroids
	public void checkHit(List<ExistIn2D> asteroids) {
		ListIterator<ExistIn2D> itr = asteroids.listIterator();
		while(itr.hasNext()) {
			Asteroid asteroid = (Asteroid) itr.next();
			// check the object is not itself
			if (asteroid != this && asteroid.isCloseTo(this)) {
				// reduce the size for both collided objects
				asteroid.hitAsteroid();
				this.hitAsteroid();
				// collision affects their course [used elastic collision]
				asteroid.dx *= -1;
				this.dx *= -1;
				
				// weigh target size for change rate --> didn't work well
				/*asteroid.dx *= -1*(this.size/INITIAL_SIZE);
				this.dx *= -1*(asteroid.size/INITIAL_SIZE);*/
			}
		}
	}
}
