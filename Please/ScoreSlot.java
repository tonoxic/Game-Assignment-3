import java.util.ArrayList;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Image;

public class ScoreSlot extends Animation
{
   public String slot;
   public ScoreSlot(float x, float y, int numFrames,String slot, String filename)
    {
        super(x, y,numFrames, filename);
        this.slot=slot;
    }
    
    public synchronized void update() 
    {
        
        int points=Points.getPoints(); 
        int value;
        
        if (slot.equals("ones"))
        {
            value=points%10;
        }
        
        else value=points/10;
        currFrameIndex=value; 
    }
    
  
    
}
