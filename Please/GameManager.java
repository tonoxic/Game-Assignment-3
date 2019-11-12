import java.awt.Graphics;
import java.awt.Point;

// GameManager Class
public class GameManager {

    // Declare all important variables
    private InputManager im;
    private static TileMapManager tmm;

    // Constructor
    public GameManager(int windowWidth, int windowHeight) {
        // Initialise all components
        tmm = new TileMapManager(windowWidth, windowHeight);
        im=InputManager.getInstance(); 
    }

    // Update all of the components
    public void update(long elapsedTime) {

        tmm.update(elapsedTime);
        // Update Player Input
        im.update();
    }

    // Draw the necessary components
    public void draw(Graphics gScr) {
        tmm.draw(gScr);

    }

    // Update the level
    // - If there are no more levels then end the game

}