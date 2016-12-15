import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CueBall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CueBall extends Ball
{
    
    public CueBall() {
        super();
        
        position = new Vector2D((double) 283/10, (double) 278/10);
    }
    /**
     * Act - do whatever the CueBall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (Greenfoot.isKeyDown("right")) {
            applyForce(new Vector2D(50, 0).rotate(angle), new Vector2D(0, 0));
        }
        
        super.act();
    }    
}
