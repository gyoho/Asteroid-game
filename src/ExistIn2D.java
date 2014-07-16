import java.awt.Graphics;


public interface ExistIn2D {
	// getter
	double getPositionX();
	double getPositionY();
	int getSize();
	
	// graphical representation
	void drawItself(Graphics g);
}
