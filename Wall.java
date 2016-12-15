import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Ball
{
    int x, y;
    public Wall(int x, int y) {
        super();
        mass = Integer.MAX_VALUE;
        position.x = (double) x / 10;
        this.x = x;
        position.y = (double) y / 10;
        this.y = y;
        setLocation(x, y);
    }
    
    public void act() {
        super.act();
        // position.x = x;
        // position.y = y;
        // setLocation(x, y);
    }
    
    
    /*public Wall(Vector2D[] corners) {
        super(corners);
    }
    
    public void act() 
    {
        // Add your action code here.
    }*/
    
    
}
