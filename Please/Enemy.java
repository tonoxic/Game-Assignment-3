import java.awt.Graphics;

public class Enemy extends Animation {

    // The enemy's speed factor
    private final float SPEED = 0.2f;
    private boolean onGround = false;


    // Constructor
    public Enemy(float x, float y, int numFrames, String filename) {
        super(x, y,numFrames, filename);
        setWidth(120);
        setHeight(120);
        setVelocityX(SPEED);
        setVelocityY(0);

    }
    @Override
    public void setVelocityY(float dy) {}
    @Override
    public void setY(float y) {}
    // Stop vertical movement
    public void collideVertical() {
        // if the player's speed is positive and they collide 
        //  vertically then they are on the ground
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }
    
     public void setGrounded(boolean b) {
        onGround = b;
    }

    // Check whether the player is on the ground or not
    public boolean isGrounded() {
        return onGround;
    }
    
    public void hitWall() {
        setVelocityX(-getVelocityX());
    }

}