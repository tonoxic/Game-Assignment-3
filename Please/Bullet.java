import java.awt.Graphics;

public class Bullet extends Animation {

    // The enemy's speed factor
    private final float SPEED = 0.08f;
    private int life;
    private Player player;

    // Constructor
    public Bullet(float x, float y, int numFrames, String filename) {
        super(x, y,numFrames, filename);
        setVelocityX(SPEED);
        player=Player.getInstance();
        life=0;
        setVelocityY(0);

    }
    
    public void update(long elapsedTime)
    {
        life++;
        if (life>5)
            setVisible(false);
        super.update(elapsedTime);
    }
    
    
    @Override
    public void setVelocityY(float dy) {}
    @Override
    public void setY(float y) {}

    public void hitWall() {
        setVelocityX(-getVelocityX());
    }

}