import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BallRack here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BallRack extends Actor
{
    boolean player1; // is this rack for p1's balls?
    boolean solid; // is this for solid balls?
    BallCounter[] balls = new BallCounter[7]; // hold seven counters
    String[] words = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen"}; // makes it easier to get file names

    public BallRack(boolean player1) {
        this.player1 = player1;
    }
    
    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public void init() {
        for (int i = 0; i < 7; i++) {
            String filename = (solid ? words[i] + ".png" : words[i+8] + ".png");
            BallCounter bc = new BallCounter(filename); // make the ballcounter object with the right image
            balls[i] = bc;
            getWorld().addObject(bc, getX() + i * 34, getY());
        }
    }
}
