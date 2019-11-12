import java.util.ArrayList;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

    public abstract class Animation extends Sprite {

        protected ArrayList frames;     // collection of images for animation frames
        protected int currFrameIndex;       // current frame being displayed
        protected long animTime;      // time that the animation has run for already
        protected long startTime;       // start time of the animation
        protected long totalDuration;       // total duration of the animation

    public Animation (
             float x, float y, int numFrames,
             String filename) {
        super(x, y,filename);

            frames = new ArrayList();
            totalDuration = 0;
            loadImage(numFrames);
            start();
        }


    public synchronized void addFrame(BufferedImage image, long duration) {
            totalDuration += duration;
            frames.add(new AnimFrame(image, totalDuration));
    }


    public synchronized void start() {
        animTime = 0;               // reset time animation has run for to zero
        currFrameIndex = 0;         // reset current frame to first frame
        startTime = System.currentTimeMillis(); // reset start time to current time
    }



    public synchronized void update(long elapsedTime) {

        if (frames.size() > 1) {
            animTime += elapsedTime;        // add elapsed time to amount of time animation has run for
            if (animTime >= totalDuration) {    // if the time animation has run for > total duration
                animTime = animTime % totalDuration;    
                                //    reset time animation has run for
                currFrameIndex = 0;     //    reset current frame to first frame
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;       // set frame corresponding to time animation has run for
            }
        }
        
        super.update(elapsedTime);
    
    }

    /**
        Gets this Animation's current image. Returns null if this
        animation has no images.
    */

    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
            else {
            return (getFrame(currFrameIndex).image);
        }
    }


    public int getNumFrames() {             // find out how many frames in animation
        return frames.size();
    }

    public AnimFrame getFrame(int i) {
        return (AnimFrame)frames.get(i);
    }

    public class AnimFrame {

        BufferedImage image;
        long endTime;

        public AnimFrame(BufferedImage image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }

    public void loadImage(int numFrames) {
        
        int imageWidth = (int) icon.getWidth(null) / numFrames;
    int imageHeight = icon.getHeight(null);
        for (int i=0; i<numFrames; i++)
        {
            BufferedImage frameImage = GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration().
                createCompatibleImage(imageWidth, imageHeight, Transparency.TRANSLUCENT);
        
        Graphics2D g = (Graphics2D) frameImage.getGraphics();
        
        g.drawImage(icon,
                    0,0, imageWidth, imageHeight,
                    i*imageWidth, 0, (i*imageWidth)+imageWidth, imageHeight,
                    null);
                    
        addFrame(frameImage, 50);
        }
        
    }

    //copy functions
    public BufferedImage copyImage(BufferedImage src) {
        if (src == null)
            return null;

            BufferedImage copy = new BufferedImage (src.getWidth(), src.getHeight(),
                            BufferedImage.TYPE_INT_ARGB);
                                                                                                                                                       
            // copy image
            Graphics2D g = copy.createGraphics();
            
            g.drawImage(src, 0, 0, null);
            g.dispose();

            return copy; 
    }
    
    public void copyAnimation (ArrayList framesCopy, ArrayList frames) {
        framesCopy.clear();
        for (int i=0; i<frames.size(); i++) {
            AnimFrame frame = (AnimFrame)frames.get(i);
            BufferedImage copy = copyImage(frame.image);
            framesCopy.add(copy);
        }
    }
    
    
  
}
