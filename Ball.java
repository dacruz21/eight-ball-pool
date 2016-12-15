import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PhysicsCircle here.
 *
 * @author MTK
 */
public class Ball extends PhysicsObject
{
    

    public Ball() {
        super.boundingRadius = 1.6;
        // super.momentOfIntertia = (mass * radius * radius) /2;
    }
    
    boolean collision = false;

    public void act() {
        super.act();
    }

    public void checkWallCollisions() {
        if(position.y-boundingRadius < 0 && velocity.y < 0) {
            handleWallCollision(new Vector2D(0, 1), new Vector2D(0, -boundingRadius));
        }
        if(position.y+boundingRadius > getWorld().getHeight()/pixelsPerMeter && velocity.y > 0) {
            handleWallCollision(new Vector2D(0, -1), new Vector2D(0, boundingRadius));
        }
        if(position.x-boundingRadius < 0 && velocity.x < 0) {
            handleWallCollision(new Vector2D(1, 0), new Vector2D(-boundingRadius, 0));
        }
        if(position.x+boundingRadius > getWorld().getWidth()/pixelsPerMeter && velocity.x > 0) {
            handleWallCollision(new Vector2D(-1, 0), new Vector2D(boundingRadius, 0));
        }
    }

    public void narrowPhaseCollisionCheck(PhysicsObject other) {
        if(other instanceof Ball) {
            Ball circle = (Ball) other;
            if(Math.abs(position.subtract(circle.position).length()) < boundingRadius+circle.boundingRadius) {
                handleCollision(circle,
                                position.subtract(circle.position),
                                new Vector2D().addPolar(boundingRadius, circle.position.subtract(position).angle()),
                                new Vector2D().addPolar(circle.boundingRadius, position.subtract(circle.position).angle()));
            }
        }
    }
}
