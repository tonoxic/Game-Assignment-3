import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public abstract class Sprite {

    protected Image icon;
    private float x;
    private float y;
    private float dx;
    private float dy;
 
    private int width;
    private int height;
    
    private static final float MAX_SPEED = 1f;

    private boolean visible = true;

    /**
        Creates a new Sprite object with the specified Image.
    */
    public Sprite(float x, float y, String filename) {
        this.x = x;
        this.y = y;
        width=100;
        height=100;
        icon = new ImageIcon(filename).getImage();
    }

    /**
        Updates this Sprite
    */
    public void update(long elapsedTime) {
        x += dx * elapsedTime;
        y += dy * elapsedTime;
    }

    /**
        Gets this Sprite's current x position.
    */
    public float getX() {
        return x;
    }

    /**
        Gets this Sprite's current y position.
    */
    public float getY() {
        return y;
    }

    /**
        Sets this Sprite's current x position.
    */
    public void setX(float x) {
        this.x = x;
    }

    /**
        Sets this Sprite's current y position.
    */
    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
        Gets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityX() {
        return dx;
    }

    /**
        Gets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public float getVelocityY() {
        return dy;
    }

    /**
        Sets the horizontal velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityX(float dx) {
        if (dx > MAX_SPEED) dx = MAX_SPEED;
        if (dx < -MAX_SPEED) dx = -MAX_SPEED;
        this.dx = dx;
    }

    /**
        Sets the vertical velocity of this Sprite in pixels
        per millisecond.
    */
    public void setVelocityY(float dy) {
        if (dy <= -MAX_SPEED*1.5f) dy = -MAX_SPEED*1.5f;
        if (dy >= MAX_SPEED) dy = MAX_SPEED;
        this.dy = dy;
    }

    /**
        Gets this Sprite's current image.
    */
    public Image getImage() {
        return icon;
    }
    
    // Sets the current Sprite's image
    public void setImage(Image img) {
        icon = new ImageIcon(img).getImage();
    }
    
    public void setWidth(int w)
    {
        this.width=w;
    }

    public void setHeight(int h)
    {
        this.height=h;
    }
    // Returns a Rectangle used for Collision
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, getWidth(), getHeight());
    }

    // Returns whether the Sprite can be drawn or not
    public boolean getVisible() {
        return visible;
    }
    
    
    // Sets whether the Sprite can be drawn or not
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}
