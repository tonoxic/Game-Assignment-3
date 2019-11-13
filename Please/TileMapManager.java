// Import Resources used for loading maps
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Import Resources used for drawing
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

// Import Resources used for collisions
import java.awt.Rectangle;

// Import Resources used for Sprite Management
import java.util.Iterator;

// TileMapManager Class
public class TileMapManager {

    // Initialise constants
    public static final int HEIGHT = 18;
    public static final int MIN_WIDTH = 32;

    // Declare useful variables for map
    private int screenWidth, screenHeight;
    private int tileWidth, tileHeight;
    private int numBullets;
    private ScoreSlot tens;
    private ScoreSlot ones;
    
    private Image background,bg1, bg2, block,gameOverImage;
    private boolean isGameOver;

    // Declare TileMap
    private TileMap map;

    // Declare Player
    private Player player;


    // Constructor
    public TileMapManager(int gameWidth, int gameHeight) {
        // Initialise Player
        player = Player.getInstance();

        // Initialise Variables used for the map
        screenWidth = gameWidth;
        screenHeight = gameHeight;
        tileWidth = gameWidth/MIN_WIDTH;
        tileHeight = gameHeight/HEIGHT;
        numBullets=0;
        
        
        tens=new ScoreSlot(0, 0, 10, "tens","map/images/score.png");
        ones=new ScoreSlot(105, 0, 10, "ones","map/images/score.png");
        
        background=loadImage("map/images/surround/background.jpg");
        bg1=loadImage("map/images/surround/moon.png");
        bg2=loadImage("map/images/surround/treeline.png");
        gameOverImage=loadImage("map/images/surround/gameOver.png");
        block=loadImage("map/images/surround/tile.png");
       
       loadMap("map/map.txt");

    }
    
    private Image loadImage (String filename) {
        return new ImageIcon(filename).getImage();
    }

    // Load the current map into the TileMap
    // - Adapted from Brackeen (ResourceManager.java)
    public void loadMap(String filename) {

      if (map != null) map.clearSprites();
        map=null;
        
        ArrayList<String> lines = new ArrayList<String>();
        try {
            if (!((new File(filename)).exists())) {
                System.out.println("File \"" + filename + "\" does not exist.");
                System.exit(1);
            }
           BufferedReader reader = new BufferedReader(new FileReader(filename));
           while (true) 
        {
                String line = reader.readLine();
                if (line == null) 
                {
            reader.close();
            break;
        }

        if (!line.startsWith("#")) 
        {
        lines.add(line);
        }
        }
        
        String stringWidth = lines.get(0);
        int mapTileWidth = Integer.parseInt(stringWidth);
        TileMap newMap = new TileMap(mapTileWidth, HEIGHT);
        for (int y = 0; y < HEIGHT; y++) 
        {
                String line = (String) lines.get(y+1);
                 
        for (int x = 0; x < line.length(); x++) {
        char ch = line.charAt(x);
        if (ch == 'B') { 
            newMap.setTile(x, y, block);
        } 
                else if (ch == 'P') {
                        player.setX(x*tileWidth);
                        player.setY(y*tileHeight);
                    } 
                else if (ch == 'E')
                {
                    Enemy e = new Enemy(x*tileWidth, y*tileHeight,10, "map/images/baddies/zombie.png");
                        newMap.addSprite(e);
                }
            }
        }
        map = newMap;
        
    } 
         catch (IOException ioe) {
            System.out.println("Got some error with map file.");
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    // Set up resources for the next level


    // Update the sprites in the map
    public void update(long elapsedTime) {
        
        Iterator<Sprite> i = map.getSprites();
        int enemyCount=0;
        while (i.hasNext()) {
            Sprite s = i.next();
            if (!(s.getVisible()))continue; 

            s.update(elapsedTime);
            
            if (s instanceof Enemy) {
                checkCollision(s);
                checkPlayerCollision(s);
                enemyCount++;
            }
           
           
        }
        
        if (enemyCount==0)
            isGameOver=true;
        // Update Player Sprite
        // - keep track of old position in case collision fails
        float oldX, oldY;
        oldX = player.getX();
        oldY = player.getY();
        // - move the player
        player.update(elapsedTime);
        // Continuously check for collisions
        int j = 0;
        while (checkCollision(player)) {
            j++;
            // If the player collides more than 10 times then the player is stuck
            if (j>=10) {
                // Move the player to its previous position and stop checking for collisions
                player.setX(oldX);
                player.setY(oldY);
                break;
            }
        }
        // Check for ground below the player
        if (!(groundBelow(player))) player.setGrounded(false);
   
        
        if (Points.getPoints()==0)
            isGameOver=true;
    }

    
    /**
        Draws the specified TileMap.
    */
    public void draw(Graphics g) {
        int mapWidth = tilesToPixels(map.getWidth());

        // get the scrolling position of the map
        // based on player's position
        int offsetX = screenWidth / 2 - Math.round(player.getX());  // Map's scrolling position when the character is in the middle
        offsetX = Math.min(offsetX, 0);                             // Map's scrolling position when the character is at the beginning
        offsetX = Math.max(offsetX, screenWidth - mapWidth);        // Map's scrolling position when the character is near the end

        // get the y offset to draw all sprites and tiles
        // - Our levels currently have no verticality
        int offsetY = screenHeight - tilesToPixels(HEIGHT); // = 0

        g.setColor(Color.GRAY);
        g.fillRect(0, 0, screenWidth, screenHeight);

        // draw parallax background image
        if (bg1!=null && bg2!=null && background!=null) {
               int x, y, iWidth;
            g.drawImage (background, 0, 0,screenWidth,  screenHeight, null);
            
            iWidth = bg1.getWidth(null);
            x = offsetX/2 *
                (screenWidth - bg1.getWidth(null)) /
                (screenWidth - mapWidth);
            y = screenHeight - bg1.getHeight(null);
            g.drawImage(bg1, x, y, null);
            
            /* iWidth = bg1.getWidth(null);
            x = 4*offsetX *
                (screenWidth - bg1.getWidth(null)) /
                (screenWidth - mapWidth);
            y = screenHeight - bg1.getHeight(null);
            g.drawImage(bg1, x, y, null);
            if (x + iWidth < screenWidth) g.drawImage(bg2, x+iWidth, y, null);*/
            
            iWidth = bg2.getWidth(null);
            x =  2*offsetX *
                (screenWidth - bg2.getWidth(null)) /
                (screenWidth - mapWidth);
            y = screenHeight - bg2.getHeight(null);
            g.drawImage(bg2, x, y, null);
            if (x + iWidth < screenWidth) g.drawImage(bg2, x+iWidth, y, null);
            ones.update();
            tens.update();
        
        }

        // Draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX, 
                        tilesToPixels(y) + offsetY,
                        tilesToPixels(x) + offsetX + tileWidth, 
                        tilesToPixels(y) + offsetY + tileHeight, 
                        0, 
                        0,
                        image.getWidth(null), 
                        image.getHeight(null),
                        null);
                }
            }
        }

        // Draw sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            if (!(sprite.getVisible())) continue;
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;

            // Draw the sprite in the center of the tile if it is smaller than a tile
            int midTileOffsetX = 0, midTileOffsetY = 0;
            if (sprite.getWidth() < tileWidth) {
                midTileOffsetX = (tileWidth-sprite.getWidth())/2;
            }
            if (sprite.getHeight() < tileHeight) {
                midTileOffsetY = (tileHeight-sprite.getHeight())/2;
            }
            
            // If the tile is moving left or right then face it in that direction
            // - note all sprites currently face right by default
            if (Float.compare(sprite.getVelocityX(), 0) < 0) {
                g.drawImage(sprite.getImage(), x+sprite.getWidth()+midTileOffsetX, y+midTileOffsetY, -sprite.getWidth(), sprite.getHeight(), null);
            } else {
                g.drawImage(sprite.getImage(), x+midTileOffsetX, y+midTileOffsetY, sprite.getWidth(), sprite.getHeight(), null);
            }
        }
        
        g.drawImage(tens.getImage(), (int)tens.getX(), (int)tens.getY(), null);
         g.drawImage(ones.getImage(), (int)ones.getX(), (int)ones.getY(), null);
        // Draw the Player
        player.draw(g, screenWidth, mapWidth);
        if (isGameOver)
    {
        g.drawImage (gameOverImage, 0, 0, screenWidth, screenHeight, null);
    }
            
    }   

    // Check whether a Sprite collides with the TileMap
    private boolean checkCollision(Sprite sprite) {
        // Get possible tile collisions
        Point[] tiles = map.getTileCollision(sprite, tileWidth, tileHeight);
        if (tiles == null || tiles.length <= 0) return false;

        // Declare and Initialise useful variables
        boolean tileAbove = false, tileBelow = false, tileLeft = false, tileRight = false;

        // If the sprite is the Player
        if (sprite instanceof Player) {
            
            // Check each tile until a collision is found
            // - If a collision is found then rectify it and return true
            for (Point tile : tiles) {
                if (tile != null) {
                    // If the collision occurs on a finishLine tile

                    // Get Player Bounds
                    int px_left  = Math.round(player.getX());
                    int px_right = Math.round(player.getX()+player.getWidth());
                    int py_top   = Math.round(player.getY());
                    int py_bot   = Math.round(player.getY()+player.getHeight());
                    // Get Tile Bounds
                    int tx_left  = tilesToPixels(tile.x);
                    int tx_right = tilesToPixels(tile.x+1);
                    int ty_top   = tilesToPixels(tile.y);
                    int ty_bot   = tilesToPixels(tile.y+1);

                    // Check the boundary cases for a collision
                    // - Edge of the map
                    boolean boundaryCase = false;
                    if (tile.x < 0) {
                        player.collideHorizontal();
                        player.setX(1f);
                        boundaryCase = true;
                    }
                    if (tile.x >= map.getWidth()) {
                        player.collideHorizontal();
                        player.setX(map.getWidth()*tileWidth - player.getWidth()-1);
                        boundaryCase = true;
                    }
                    if (tile.y < 0) {
                        player.collideVertical();
                        player.setY(1f);
                        boundaryCase = true;
                    } 
                    if (tile.y >= map.getHeight()) {
                        player.setY(map.getHeight()*tileHeight - player.getHeight()-1);
                        boundaryCase = true;
                    }
                    if (boundaryCase) return boundaryCase;

                    // If the tile is not completely outside the bounds of the Player
                    if (!(tx_right < px_left || tx_left > px_right || ty_bot < py_top || ty_top > py_bot)) {
                        // Check vertical plane of the tile
                        if (py_top < ty_top && py_bot < ty_bot && py_bot >= ty_top) {
                            // Tile is below
                            player.collideVertical();
                            py_top = ty_top-player.getHeight()-1;
                            py_bot = py_top+player.getHeight();
                            player.setY(py_top);
                            tileBelow = true;
                        } 
                        if (py_top > ty_top && py_top <= ty_bot && py_bot > ty_bot) {
                            // Tile is above
                            player.collideVertical();
                            py_top = ty_bot+1;
                            py_bot = py_top+player.getHeight();
                            player.setY(py_top);
                            tileAbove = true;
                        }
                        // If the Player collided strictly either above or below
                        // - "^" represents the logic gate XOR
                        if (tileAbove ^ tileBelow) return true;
                        // If the player either had no vertical collision 
                        //  or collided both above and below then continue

                        // Check horizontal plane of the tile
                        if (px_left > tx_left && px_left <= tx_right && px_right > tx_right) {
                            // Tile is to the left
                            if (tileAbove) {
                                py_top = ty_top-player.getHeight()-1;
                                py_bot = py_top+player.getHeight();
                                player.setY(py_top);
                            } else {
                                player.collideHorizontal();
                                px_left = tx_right+1;
                                px_right = px_left+player.getWidth();
                                player.setX(px_left);
                            }
                            tileLeft = true;
                        } 
                        if (px_right < tx_right && px_right <= tx_left && px_left < tx_left) {
                            // Tile is to the right
                            player.collideHorizontal();
                            px_left = tx_left-player.getWidth()-1;
                            px_right = px_left+player.getWidth();
                            player.setX(px_left);
                            tileRight = true;
                        }
                        // If the Player collided strictly either on the left or right
                        // - "^" represents the logic gate XOR
                        if (tileLeft ^ tileRight) return true;
                        // If the player either had no horizontal collision 
                        //  or collided both to the left and right then continue

                        // Check if the tile is within the player
                        if (tx_left > px_left && tx_right < px_right && ty_top > py_top && ty_bot < py_bot) {
                            // The tile is within the player fully
                            player.collideHorizontal();
                            player.collideVertical();
                            if (tx_left-px_left > px_right-tx_right) {
                                px_left = tx_left-player.getWidth()-1;
                                px_right = px_left+player.getWidth();
                                player.setX(px_left);
                            } else if (tx_left-px_left < px_right-tx_right) {
                                px_left = tx_right+1;
                                px_right = px_left+player.getWidth();
                                player.setX(px_left);
                            }
                            if (ty_top-py_top > py_bot-ty_bot) {
                                py_top = ty_bot+1;
                                py_bot = py_top+player.getHeight();
                                player.setY(py_top);
                            } else if (ty_top-py_top < py_bot-ty_bot) {
                                py_top = ty_top-player.getHeight()-1;
                                py_bot = py_top+player.getHeight();
                                player.setY(py_top);
                            }
                            return true;
                        } else if (tx_left > px_left && tx_right < px_right) {
                            // The tile is within the player horizontally but not vertically
                            player.collideHorizontal();
                            if (tx_left-px_left > px_right-tx_right) {
                                px_left = tx_left-player.getWidth()-1;
                                px_right = px_left+player.getWidth();
                                player.setX(px_left);
                            } else if (tx_left-px_left < px_right-tx_right) {
                                px_left = tx_right+1;
                                px_right = px_left+player.getWidth();
                                player.setX(px_left);
                            }
                            return true;
                        } else if (ty_top > py_top && ty_bot < py_bot) {
                            // The tile is within the player vertically but not horizontally
                            py_top = ty_top-player.getHeight()-1;
                            py_bot = py_top+player.getHeight();
                            player.collideVertical();
                            player.setY(py_top);
                            return true;
                        }
                    }
                }
            }
        } else if (sprite instanceof Enemy) {
            Enemy e = (Enemy)sprite;

            // Check each tile until a collision is found
            // - If a collision is found then rectify it and return true
            for (Point tile : tiles) {
                if (tile != null) {
                    // Declare and Initialise Enemy Bounds
                    int ex_left  = (int)e.getX();
                    int ex_right = (int)e.getX()+e.getWidth()-1;
                    int ey_top   = (int)e.getY();
                    int ey_bot   = (int)e.getY()+e.getHeight()-1;

                    // Declare and Initialise Tile Bounds
                    int tx_left  = tilesToPixels(tile.x);
                    int tx_right = tilesToPixels(tile.x+1)-1;
                    int ty_top   = tilesToPixels(tile.y);
                    int ty_bot   = tilesToPixels(tile.y+1)-1;

                    // Check the boundary cases for a collision
                    // - Edge of the map
                    boolean boundaryCase = false;
                    if (tile.x < 0) {
                        e.hitWall();
                        e.setX(1);
                        boundaryCase = true;
                    }
                    if (tile.x >= map.getWidth()) {
                        e.hitWall();
                        e.setX(map.getWidth()*tileWidth - e.getWidth()-1);
                        boundaryCase = true;
                    }
                    if (boundaryCase) return boundaryCase;

                    // If the tile is not completely outside the bounds of the Enemy
                    if (!(tx_right < ex_left || tx_left > ex_right || ty_bot <= ey_top || ty_top > ey_bot)) {
                        // Check horizontal plane of the tile
                        if (tx_left <= ex_left && tx_right >= ex_left && tx_right <= ex_right) {
                            // Tile is to the left
                            e.hitWall();
                            e.setX(tx_right+1);
                            return true;
                        } else if (tx_right >= ex_right && tx_left <= ex_right && tx_left >= ex_left) {
                            // Tile is to the right
                            e.hitWall();
                            e.setX(tx_left-e.getWidth()-1);
                            return true;
                        }
                    }
                }
            }
        }
        // If there are no collisions
        return false;
    }

   void checkPlayerCollision(Sprite sprite) {
        if (sprite instanceof Enemy) {
            Enemy enemy = (Enemy) sprite;
            // Get the bounding rectangles of the Player and the Enemy
            Rectangle e = enemy.getBounds();
            Rectangle p = player.getBounds();
            // If the Player and Ememy collide
            if (p.intersects(e)) {
                
                if (Player.getAttack())   
                   {
                    enemy.setVisible(false);
                    enemy = null;
                } else {
                    player.getHurt();
                }
            }
        } 
    }

    // Check if the ground is below a sprite
    private boolean groundBelow(Sprite sprite) {
        if (sprite instanceof Player) {
            float left = player.getX();
            float right = player.getX()+player.getWidth();
            float bot = player.getY()+player.getHeight();

            // Get possible tiles below the Player
            Point[] tiles = map.getUnderfoot(left, right, bot, tileWidth, tileHeight);

            if (tiles == null || tiles.length <= 0) return false;

            // If the tile exists and is a ground tile then return true
            for (Point tile : tiles) {
                if (tile != null) {
                    if (map.getTile(tile.x, tile.y) ==block)
                        return true;
                }
            }
        }
        // No tile was found below the Sprite
        return false;
    }

    // Convert Tiles to its Pixel equivalent
    private int tilesToPixels(int numTiles) {
        return numTiles * tileWidth;
    }

    // Convert Pixels to its Tile equivalent
    private int pixelsToTiles(int pixels) {
        return pixels / tileWidth;
    }
}