import java.awt.Graphics;

public class Enemy extends Animation {

    // The enemy's speed factor
    private final float SPEED = 0.08f;


    // Constructor
    public Enemy(float x, float y, int numFrames, String filename) {
        super(x, y,numFrames, filename);
        setVelocityX(SPEED);
        setVelocityY(0);

    }
    @Override
    public void setVelocityY(float dy) {}
    @Override
    public void setY(float y) {}

    public void hitWall() {
        setVelocityX(-getVelocityX());
    }

}