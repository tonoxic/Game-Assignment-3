// Import Game Resources
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Color;

// Import Full-Screen and Frame Buffer resources
import java.awt.image.BufferStrategy;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyListener;

import java.applet.Applet;
import java.applet.AudioClip;

// GameView Class
public final class GameView extends JFrame implements Runnable {

    private AudioClip bgrSound;
    private GameManager gm;
    private int windowWidth, windowHeight;
    private AudioClip backgroundSound;
    
    // Declare variables used for full-screen and frame buffer
    private GraphicsDevice device;
    private Graphics gScr;
    private BufferStrategy bufferStrategy;
    private static final int NUM_BUFFERS = 2;
    
    // Declare Variables used for game loop
    private Thread gameThread = null;
    public static volatile boolean running;

    // Singleton Instance
    private static volatile GameView INSTANCE = null;
    

    // Constructor
    public GameView () {
        // Set Window Title
        super("Side-Scroller");
        // Initialise full screen
        initFullScreen();
        
        // Initialise main components of game
        gm = new GameManager(windowWidth, windowHeight);

        // Attach the KeyListener to the JFrame
        this.addKeyListener(InputManager.getInstance());
        
        //Start With Points
        Points.getPoints(); 
        loadBackgroundSound();


        // Commence Game Loop
        startGame();
    }

    // Singleton method to get the only instance of GameView
    public static GameView getInstance() {
        if (INSTANCE == null) {
            synchronized(GameView.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GameView();
                }
            }
        }
        return INSTANCE;
    }

    // Initialise the full screen window
    private void initFullScreen() {

        // Get the graphics environment and default screen
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = ge.getDefaultScreenDevice();

        setUndecorated(true);   // no menu bar, borders, etc.
        setIgnoreRepaint(true); // turn off all paint events since doing active rendering
        setResizable(false);    // screen cannot be resized
        
        // If the device doesn't support full screen exclusive mode then close the application
        if (!device.isFullScreenSupported()) {
            System.out.println("Full-screen exclusive mode not supported");
            System.exit(0);
        }

        device.setFullScreenWindow(this); // switch on full-screen exclusive mode

        // Save the width and height of the window
        windowWidth = getBounds().width;
        windowHeight = getBounds().height;

        // Set up double buffering
        try {
            createBufferStrategy(NUM_BUFFERS);
        }
        catch (Exception e) {
            System.out.println("Error while creating buffer strategy " + e); 
            System.exit(0);
        }
        bufferStrategy = getBufferStrategy();
    }

    // Initialise the game thread
    private void startGame() {
        // Initialise the game as a thread and start the game
        if (gameThread == null || !running) {
            gameThread = new Thread(this);
            System.out.println("Game Starting!");
            gameThread.start();
            backgroundSound.loop();
        }
    }
    
    // Game loop
    public void run() {
        running = true;
        // Set up variables to keep track of the time since between loops
        long currTime = System.nanoTime();
        long elapsedTime;
        try {
            // Game Loop
            while (running) {
                elapsedTime = (System.nanoTime() - currTime)/1000000;
                currTime = System.nanoTime();
                gameUpdate(elapsedTime); // Update game objects based on the amount of time since they were last updated
                screenUpdate();          // Display objects on the screen
                Thread.sleep(1000/30);   // ~30 frames per second
            }
        }
        catch(InterruptedException ie) {
            ie.printStackTrace();
        };
        // End Full-Screen mode and release game resources
        endGame();
    }
    
    // Update Game Objects
    private void gameUpdate(long elapsedTime) {
        gm.update(elapsedTime);
        
    }
    
    // Output buffered frame to screen
    private void screenUpdate() {
        try {
            // Output screen using double buffering
            gScr = bufferStrategy.getDrawGraphics();
            gameRender(gScr);   // Draw all objects to buffer strategy's graphics
            gScr.dispose();
            if (!bufferStrategy.contentsLost()) bufferStrategy.show(); // Display Screen
            else System.out.println("Contents of buffer lost.");

            Toolkit.getDefaultToolkit().sync();
        }
        catch (IllegalStateException ise) {
            ise.printStackTrace();
            running = false;
        }
        catch (Exception e) { 
            e.printStackTrace();  
            running = false; 
        }
    }
    
    // Draw to buffered frame
    private void gameRender(Graphics gScr){
        gm.draw(gScr);
    }

    // Close game
    private void endGame() {
        Window w = device.getFullScreenWindow();
        // Release resources
        if (w != null) w.dispose();
        device.setFullScreenWindow(null);
        System.exit(0);
    }
    
  
    private void loadBackgroundSound()
    {
        
        try
        {
            backgroundSound=Applet.newAudioClip (
                    getClass().getResource("sounds/background.wav"));
                

        }
        
        catch (Exception e) 
        {
            System.out.println ("Error loading sound file: " + e);
        } 

        
    }    
    
}