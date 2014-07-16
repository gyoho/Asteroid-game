import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

public class AsteroidGame extends Frame {
	
	// default setting
	public final int LIFE_POINT = 100;
	public final double DIFFICULTY = 0.5;
	public final double ROCKET_SPEED = 10;
	public final int ARM_SIZE = 10;
	public final int BODY_SIZE = 30;
	private int frameWidth = 500, frameHeight = 400;
	private int adjY = 30;
	
	// set timer
	long startTime = System.currentTimeMillis();
	long elapsedTime = 0L;
	
	
	// instantiate every component with the default value
	private List<ExistIn2D> asteroids = new LinkedList<>();
	private List<ExistIn2D> rockets = new LinkedList<>();
	private Station station = new Station(frameWidth/2, frameHeight-adjY, ARM_SIZE, BODY_SIZE, ROCKET_SPEED, LIFE_POINT);
	
	
	// constructor
	// keyDown and CloseQuit is defined below
	public AsteroidGame() {
		setTitle("Asteroid Game");
		setSize(frameWidth, frameHeight);
		setBackground(Color.BLACK);
		addKeyListener (new keyDown());
		addWindowListener(new CloseQuit());
		addComponentListener(new ResizeListener());
	}
	

	public void run() {
		System.out.println("run method is called");
		
		// when lost all life points game's over
		while(station.getHits() > 0) {
//			System.out.println("in while loop of GameMover class");
			
			generateAsteroid();
			moveAllPieces();
			checkAllhits();
			removeDust(asteroids);
			removeDust(rockets);
			
			// method in java.awt.Component.repaint()
			// repaint() ==> update() ==> paint()
			repaint();
			
			// give a short period of time for keyListener to respond
			try {
				// hang up for 100 milliseconds
			    Thread.sleep(100);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			//System.out.println("Asteroid size: " + asteroids.size());
			//System.out.println("Rocket size: " + rockets.size());
		}
		
		System.out.println("end of run method is called");
	}
	
	// draw all components
	public void paint(Graphics g) {
		// display time
		drawTimer(g);
		
		// draw station
		station.drawItself(g);
		
		// draw all asteroids
		ListIterator<ExistIn2D> asteroidListItr = asteroids.listIterator();
		while(asteroidListItr.hasNext()) {
			Asteroid asteroid = (Asteroid) asteroidListItr.next();
			asteroid.drawItself(g);
		}
		
		// draw all rockets
		ListIterator<ExistIn2D> rocketListItr = rockets.listIterator();
		while(rocketListItr.hasNext()) {
			Rocket rocket = (Rocket) rocketListItr.next();
			rocket.drawItself(g);
		}
		
		if(station.getHits() <= 0) {
			// game's over, display the up-time
			drawRecord(g);
		}
	}
		
	// create a new asteroid with probability of DIFFICULTY
	public void generateAsteroid() {
		if(Math.random() < DIFFICULTY) {	// Math.random() generates random number between 0.0 - 1.0
			/*TODO: generates a new asteroid in a random position floating into from the outside of the frame*/
			double xSpeed = 10*Math.random()-5;
			double ySpeed = 3*Math.random()+3;
			Asteroid newAsteroid = new Asteroid(frameWidth*Math.random(), 0, xSpeed, ySpeed);
			/*TODO << add element(newAsteroid) to linked-list asteroids >>*/
			asteroids.add(newAsteroid);
		}
	}
	
	// move all components and check any hits
	public void moveAllPieces() {
		// asteroid
		ListIterator<ExistIn2D> asteroidListItr = asteroids.listIterator();
		while(asteroidListItr.hasNext()) {
			Asteroid asteroid = (Asteroid) asteroidListItr.next();
			asteroid.move();
		}
		
		// rockets
		ListIterator<ExistIn2D> rocketListItr = rockets.listIterator();
		while(rocketListItr.hasNext()) {
			Rocket rocket = (Rocket) rocketListItr.next();
			rocket.move();
		}
	}
	
	/** maybe it's clean to put move and checkHit in the same while loop
	 * but, we need to separate the invocation of move from checkHit method
	 * because all pieces are moving simultaneously
	 * there is a case where an asteroid dodge a rocket or vice versa**/
	public void checkAllhits() {
		// asteroid against asteroid
		ListIterator<ExistIn2D> asteroidListItr = asteroids.listIterator();
		while(asteroidListItr.hasNext()) {
			Asteroid asteroid = (Asteroid) asteroidListItr.next();
			asteroid.checkHit(asteroids);
		}
		
		// rock against asteroid
		ListIterator<ExistIn2D> rocketListItr = rockets.listIterator();
		while(rocketListItr.hasNext()) {
			Rocket rocket = (Rocket) rocketListItr.next();
			rocket.checkHit(asteroids);
		}
		
		// station against asteroid
		station.checkHit(asteroids);
	}
	
	// TODO: remove all dust any components out of the window from the list
	public void removeDust(List<ExistIn2D> listOfObjects) {
		ListIterator<ExistIn2D> itr = listOfObjects.listIterator();
		while(itr.hasNext()) {
			/**each time calling next() method iterator advances to the next element
			   this will cause NoSuchElementException **/
			/**BAD: if(itr.next().getSize() == 0 || 
					( (itr.next().getPositionX() < 0) || (itr.next().getPositionX() > frameWidth) || (itr.next().getPositionY() > frameHeight) ) )**/
			// save the reference
			ExistIn2D nextElem = itr.next();
			if(nextElem.getSize() == 0 || 
					( (nextElem.getPositionX() < 0) || (nextElem.getPositionX() > frameWidth) || (nextElem.getPositionY() > frameHeight) ) ) {
				itr.remove();
			}
		}
	}
	
	// inner class for drawing timer
	private void drawTimer(Graphics g) {
		// convert milliseconds to seconds 
		elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		g.setColor(Color.BLUE);
		g.drawString("Time: " + elapsedTime + " seconds", 0, frameHeight-10);
	}
	
	// inner class for drawing record when game's over
	private void drawRecord(Graphics g) {
		Font helvetica = new Font ("Helvetica", Font.BOLD, 30);
		g.setFont(helvetica);
		g.setColor(Color.BLUE);
		g.drawString("Game Over", frameWidth/2-90, frameHeight/2-15);
		g.drawString("Record: " + elapsedTime + " seconds", frameWidth/2-140, frameHeight/2+20);
	}
	
	
	// inner class for intercepting the key press
	private class keyDown extends KeyAdapter {
		public void keyPressed (KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
				case KeyEvent.VK_LEFT:
					station.moveLeft( );
					break;
				case KeyEvent.VK_RIGHT:
					station.moveRight( );
					break;
				case KeyEvent.VK_SPACE:
					station.fire(rockets);
					break;
				case KeyEvent.VK_Q:
					System.exit(0);
			}
		}
	}
	
	// inner class for intercepting window action
	private class CloseQuit extends WindowAdapter{  
        public void windowClosing(WindowEvent e){  
            System.exit(0);  
        }  
    }
	
	// inner class for responding for window resizing
	private class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
        	// update to the new window size
            Dimension newSize = e.getComponent().getSize();
            frameWidth = (int) newSize.getWidth();
            frameHeight = (int) newSize.getHeight();
            // update the station position
            station.setPosition(frameWidth/2, frameHeight-adjY);
        }   
    }
	
	//TODO: put comment and see where to be called
	public class GameMover extends Thread {
		@Override
		public void run() {
			System.out.println("run method of GameMover class is called");
			
			while(station.getHits() > 0) {
//				System.out.println("in while loop of GameMover class");
				
				generateAsteroid();
				moveAllPieces();
				checkAllhits();
				removeDust(asteroids);
				removeDust(rockets);
				repaint();
				
				try {
				    Thread.sleep(100);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				
				//System.out.println("Asteroid size: " + asteroids.size());
				//System.out.println("Rocket size: " + rockets.size());
			}
			
			System.out.println("end of run method of GameMover class is called");
		}
	}
}
