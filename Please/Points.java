import java.applet.Applet;
import java.applet.AudioClip;
public class Points
{
    static int score;
    static Points points;
    private static AudioClip deadSound;
    
    private Points()
    {
        score=15;
        loadDeadSound();
    }
    
    public static int getPoints()
    {
        if (points==null)
            points=new Points();
        return points.score;
    }
    
    public static void addPoints(int merit)
    {
        if (points==null)
            points=new Points();
        score+=merit;
        if (score>=60)
            score=60;
    }
    
    public static void deductPoints(int demerit)
    {
        if (points==null)
            points=new Points();
        if (score>0)
        {
            score-=demerit;
        if (score<=0)
            {
                score=0;
                deadSound.play();
            }
        }
    }

     private void loadDeadSound()
    {
        
        try
        {
            deadSound=Applet.newAudioClip (
                    getClass().getResource("sounds/dead.wav"));
                

        }
        
        catch (Exception e) 
        {
            System.out.println ("Error loading sound file: " + e);
        } 

        
    }
}
