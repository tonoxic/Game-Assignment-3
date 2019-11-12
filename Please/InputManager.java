// Import Listeners
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

// InputManager Class
public final class InputManager implements KeyListener {

    // Singleton Instance
    private static volatile InputManager INSTANCE = null;

    // Boolean array to keep track of which keys are currently pressed
    private boolean[] keyCodes;
    private Player player;

    // Constructor
    private InputManager() {
        this.player = Player.getInstance();
        keyCodes = new boolean[KeyEvent.KEY_LAST]; // All values initialised to false (not pressed) automatically by Java
    }

    // Singleton method to get the only instance of InputManager
    public static InputManager getInstance() {
        if (INSTANCE == null) {
            synchronized(InputManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InputManager();
                }
            }
        }
        return INSTANCE;
    }

    // Update function
    public void update() {
        // If the player is pressing the LEFT arrow key or "A"
        player.movingLeft(keyCodes[KeyEvent.VK_LEFT] || keyCodes[KeyEvent.VK_A]);
        // If the player is pressing the RIGHT arrow key or "D"
        player.movingRight(keyCodes[KeyEvent.VK_RIGHT] || keyCodes[KeyEvent.VK_D]);
        // If the player presses SPACE then attempt to jump
        if (keyCodes[KeyEvent.VK_UP]) player.jump(false);
        // If the user presses ESC then stop the game from running
        if (keyCodes[KeyEvent.VK_ESCAPE]) GameView.running = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {
        // If a key is released then set its value to false
        if (e.getKeyCode() < KeyEvent.KEY_LAST)
            keyCodes[e.getKeyCode()] = false;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        // If a key is pressed then set its value to true
        if (e.getKeyCode() < KeyEvent.KEY_LAST)
            keyCodes[e.getKeyCode()] = true;
    }
    
}