import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Point;

public class TileMap
{
   private Image[][] tiles;
   private ArrayList<Sprite> sprites; 
   
   public TileMap(int width, int height)
   {
       tiles=new Image [width][height];
       sprites=new ArrayList();
    }
    
   public int getWidth()
   {
       return tiles.length;
    }
    
   public void clearSprites()
   {
       sprites.clear();  
    }
   public int getHeight()
   {
       return tiles[0].length;
    }
    
   public Image getTile(int x, int y)
   {
       if (x<0 || x>=getWidth() || y<0 || y>=getHeight())
            return null;
       return tiles[x][y];
    }
    
   public void setTile(int x, int y, Image tile)
   {
       tiles[x][y]=tile;
    }
 
    
   public void addSprite(Sprite sprite)
   {
       sprites.add(sprite);
    }
    
   public void removeSprite(Sprite sprite){
    sprites.remove(sprite);
    }
    
   public Iterator getSprites()
   {
       return sprites.iterator();
    }
   
   public Point[] getTileCollision(Sprite sprite, int tileWidth, int tileHeight)
   {
       Point[] collisions;
       
       float fromX = sprite.getX();
       float fromY = sprite.getY();
       float toX   = fromX+sprite.getWidth();
       float toY   = fromY+sprite.getHeight();
       
       int fromTileX = (int)Math.floor(fromX/tileWidth);
       int fromTileY = (int)Math.floor(fromY/tileHeight);
       int toTileX   = (int)Math.floor(toX/tileWidth);
       int toTileY   = (int)Math.floor(toY/tileHeight);
       
       int rows = toTileY-fromTileY+1;
       int cols = toTileX-fromTileX+1;
       
       if (rows*cols <= 0) return null;
       
       collisions = new Point[(rows)*(cols)];

       for (int x = 0; x < rows; x++) {
           for (int y = 0; y < cols; y++) {
                int pos = (x * cols) + y;
                if (fromTileX + x < 0 || fromTileX + x >= tiles.length || 
                    fromTileY + y < 0 || fromTileY + y >= getHeight() || 
                    tiles[fromTileX+x][fromTileY+y] != null) {
                    collisions[pos] = new Point(fromTileX+x, fromTileY+y);
                } else {
                    collisions[pos] = null;
                }
            }
        } 
       return collisions;
   }
    
   public Point[] getUnderfoot(float left, float right, float bot, int tileWidth, int tileHeight)
   {
       Point[] tiles;
       int fromTileX = (int)Math.floor(left/tileWidth);
       int toTileX   = (int)Math.floor(right/tileWidth);
       int tileY   = (int)Math.floor(bot/tileHeight);
       int rows = 1;
       int cols = toTileX-fromTileX+1;
       
       if (rows*cols <= 0) return null;
       
       tiles = new Point[(rows)*(cols)];
       
       for (int y = 0; y < cols; y++) {
            int pos = y;
            if (fromTileX < 0 || fromTileX >= tiles.length || 
                tileY + y < 0 || tileY + y >= getHeight() || 
                this.tiles[fromTileX][tileY+y] != null) {
                // possible collision found, add the tile
                tiles[pos] = new Point(fromTileX, tileY+y);
            } else {
                tiles[pos] = null;
            }
        }
       return tiles;
    }
}
