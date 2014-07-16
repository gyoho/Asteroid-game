
public class GameController {
	// driver
	static public void main(String [ ] args) {
		AsteroidGame world = new AsteroidGame();
		world.setVisible(true);
		/***Note: calling run method will actually runs run method in GameMover class***/
		world.run();		
		
		AsteroidGame.GameMover game = world.new GameMover();
		game.start();
	}
}