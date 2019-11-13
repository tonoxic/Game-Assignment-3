import java.awt.Graphics;
import java.awt.Point;


public class GameManager {

    private InputManager im;
    private static TileMapManager tmm;

    public GameManager(int windowWidth, int windowHeight) {
        tmm = new TileMapManager(windowWidth, windowHeight);
        im=InputManager.getInstance(); 
    }

    public void update(long elapsedTime) {

        tmm.update(elapsedTime);
        im.update();
    }

    // Draw the necessary components
    public void draw(Graphics gScr) {
        tmm.draw(gScr);

    }
    
    

}