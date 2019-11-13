Game-Assignment-3


				                Robot Alien World Game
			            xxxxxxxxxXXXXXXXXXXXXXXXXxxxxxxxxxxxxxxxxx

OBJECTIVE
XXXXXXXXX

The objective of the game is for the "Hero" (red robot sprite) to kill all hostile robots on the screen without 
losing all their life points.  The arrowkeys control the hero, with up being the jump key.  The hero attacks with
a short range electric charge when the space key is clicked.  The game ends when either the hero has killed all 
hostile robots or the hero has  been killed.  Using the escape key closes the game.

CLASSES
XXXXXXXX

Sprite

Basic sprite class that holds the dimensions, position and visibility of a sprite.
Constructor takes an x position, y position, xSize and ySize (that is, the width and height
we want to draw the image) and the image string.

Animation

Basic animation class that holds frames and handles updating.  Uses an animation strip
infers the width to cut the image at by using the width and height variables inherited
from sprite. Takes in sprite construction variables plus the number of frames.


Points

Singleton class that houses current points.  Repurposed into a health bar.

ScoreSlot

A class used to show the current points/time remaining on the screen.  Uses an image strip
animation that updates depending on the score housed in the Points class. Implements the 
animation interface.

Player

Implements the sprite interface. Playable sprite that moves around within the boundries
of the screen.Controlled using the arrow keys and can attack by using the space button.
Using gravity, friction and velocity.  Can jump.

Enemy

Implements the animation effects.  Adversary that the player must shoot. 

TileMap

Host all the functionalities necessary for bulding a tilemap.

TimeMap Manager

Loads and manages the tilemap.  Does drawings to the tilemap.  Sidescroller and parallax background effects here.

Input Manager

Calls functions based on user input to the keyboard.

Game Manager

Manages the Input Manager and the TileMap manager.  Creates them, updates them and, where necessary, calls the draw method.

Game View

Panel on which the game is played on. Runs in fullscreen exclusive mode.

Play

Runs the "Robot Alien World" Game


