import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PlayerMarker here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PlayerLight extends Actor
{
    /**
     * Act - do whatever the PlayerMarker wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    public void set(boolean on) {
        // turns the light on or off
        if (on) {
            setImage("beeper.png");
        } else {
            setImage("GameController.png"); // this was originally only going to be used to make the gamecontroller invisible, and its too much work to refactor now. o well
        }
    }
}
