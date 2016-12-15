import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Hole here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hole extends Actor
{
    PoolTable table;
    public Hole(PoolTable table) {
        this.table = table;
    }
    
    public void act() 
    {
        for(Object obj : getWorld().getObjects(Ball.class)) {
            Ball other = (Ball) obj;

            // System.out.println(getX() + "\t" + other.position.x);

            if((Math.abs(getX()-other.getX()) < other.boundingRadius * 10) && (Math.abs(getY()-other.getY()) < other.boundingRadius * 10)) {
                // the ball is in the hole
                other.velocity = new Vector2D(0,0);
                GreenfootImage imagePath = other.getImage(); // rather than account for every single ball type, let's just determine its image, as the ball counter only needs the image

                // if (other instanceof SolidBall || other instanceof StripedBall) {
                    // getWorld().removeObject(other);
                    // other = null;
                // } else if (other instanceof CueBall) {
                    // other.position = new Vector2D((double) 512 / 10, (double) 278 / 10);
                    // other.setLocation(512, 278);
                // }
                
                if (other instanceof SolidBall) {
                    table.getController().sunkSolid(imagePath);
                    getWorld().removeObject(other);
                } else if (other instanceof StripedBall) {
                    table.getController().sunkStriped(imagePath);
                    getWorld().removeObject(other);
                } else if (other instanceof CueBall) {
                    table.getController().sunkCue();
                } else {
                    table.getController().sunkEight();
                    getWorld().removeObject(other);
                }
            }
        }
    }    
}
