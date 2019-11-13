import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.geom.Rectangle2D;


// Code adapted from Brackeen

/**
    The Player.
*/
public final class Player extends Animation {

    // Declare and Initialise Player Constants
    private static final float JUMP_SPEED = -2f;
    private static final float SPEED = 0.002f;
    private static final float FRICTION = 0.8f;
    private static final float GRAVITY = 0.005f;
    
    private AudioClip attackSound;

    private boolean movingLeft = false, movingRight = false;
    private boolean onGround = false;
    
    private boolean shoot=false;

    private static volatile Player INSTANCE = null;
    
    private int invulnerableTime;

    private Image stillPlayer = null;
    private Image attackPlayer=null;
    
    private static boolean isAttacking=false;
    private int attackTime;

    // Constructor
    private Player(String filename) {
        super(65, 65,8, filename);
        invulnerableTime=0;
        loadAttackSound();
        stillPlayer = new ImageIcon("map/images/still_hero.png").getImage();
        attackPlayer= new ImageIcon("map/images/attack_hero.png").getImage();
    }
    

    // Singleton method to get the only instance of Player
    public static Player getInstance() {
        if (INSTANCE == null) {
            synchronized(Player.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Player("map/images/hero.png");
                }
            }
        }
        return INSTANCE;
    }
    

    @Override
    public Image getImage() {
        // Return the current frame if moving but only one single frame if still
        if (isAttacking)
            return attackPlayer;
        else if (!(movingLeft || movingRight)) {
            return stillPlayer;
        } else {
            return super.getImage();
        }
    }

    // Update left movement
    public void movingLeft(boolean b) {
        movingLeft = b;
    }

    // Update right movement
    public void movingRight(boolean b) {
        movingRight = b;
    }

    // Stop horizontal movement
    public void collideHorizontal() {
        setVelocityX(0);
    }
    
    public void getHurt()
    {
        if (invulnerableTime<=0)
           {invulnerableTime=400;
            Points.deductPoints(1);
            }
    }
    
    public void attack()
    {
        if (attackTime<=0)
           {attackTime=200;
            isAttacking=true;
            attackSound.play();
            }
    }
    
    public static boolean getAttack()
    {
        return isAttacking; 
    }

    // Stop vertical movement
    public void collideVertical() {
        // if the player's speed is positive and they collide 
        //  vertically then they are on the ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }

    /**
        Makes the player jump if the player is on the ground or
        if forceJump is true.
    */
    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }

    // Set whether the Player is on the ground or not
    // - also used so that the player does not hover after walking off of a cliff
    public void setGrounded(boolean b) {
        onGround = b;
    }

    // Check whether the player is on the ground or not
    public boolean isGrounded() {
        return onGround;
    }

    // Update the Player
    public void update(long elapsedTime) {
        
        if (invulnerableTime > 0) 
            invulnerableTime -= elapsedTime;
        
        if (attackTime >0)
            attackTime-=elapsedTime;
        
        else{
            if (isAttacking==true);
            isAttacking=false;
        }
        
            
        if (movingLeft) {
            setVelocityX(getVelocityX() - SPEED*elapsedTime);
        }
        // If the player is moving right then add positive speed
        // - speed is added based on time since last game loop
        if (movingRight) {
            setVelocityX(getVelocityX() + SPEED*elapsedTime);
        }
        // Slow the player constantly based on FRICTION constant
        setVelocityX(getVelocityX()*FRICTION);
        // If the player is in the air then make them fall 
        // - based on GRAVITY constant and time since last game loop
        if (!onGround) {
            setVelocityY(getVelocityY() + GRAVITY*elapsedTime);
        } else {
            setVelocityY(0);
        }
        // All that was changed above was speed
        // Update x and y values of player based on their dx and dy speeds
        super.update(elapsedTime);
      
    }

    public void draw(Graphics gScr, int screenWidth, int mapWidth) {

        int middle = screenWidth/2;
        // Draw the player on the screen based on their position in the map
        // If the player is at the left of the map such that the map can't scroll
        if (Float.compare(getX(), middle) < 0) {
            // Check if player is moving left or right and draw the player accordingly
            if (Float.compare(getVelocityX(), 0) < 0) {
                gScr.drawImage(getImage(), (int)getX()+getWidth(), (int)getY(), -getWidth(), getHeight(), null);
            } else {
                gScr.drawImage(getImage(), (int)getX(), (int)getY(), getWidth(), getHeight(), null);
            }
        // If the player is moving with the scrolling map
        } else if (Float.compare(getX(), mapWidth-middle) < 0) {
            // Check if player is moving left or right and draw the player accordingly
            if (Float.compare(getVelocityX(), 0) < 0) {
                gScr.drawImage(getImage(), middle+getWidth(), (int)getY(), -getWidth(), getHeight(), null);
            } else {
                gScr.drawImage(getImage(), middle, (int)getY(), getWidth(), getHeight(), null);
            }
        // If the player is at the right of the map such that the map can't scroll
        } else {
            // Check if player is moving left or right and draw the player accordingly
            if (Float.compare(getVelocityX(), 0) < 0) {
                gScr.drawImage(getImage(), (int)getX() - mapWidth + (middle*2)+getWidth(), (int)getY(), -getWidth(), getHeight(), null);
            } else {
                gScr.drawImage(getImage(), (int)getX() - mapWidth + (middle*2), (int)getY(), getWidth(), getHeight(), null);
            }
        }
        
        
    }
    
    private void loadAttackSound()
    {
        
        try
        {
            attackSound=Applet.newAudioClip (
                    getClass().getResource("sounds/attack.wav"));
                

        }
        
        catch (Exception e) 
        {
            System.out.println ("Error loading sound file: " + e);
        } 

        
    }

}
