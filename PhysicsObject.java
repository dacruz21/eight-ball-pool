import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A physics engine base class.
 * 
 * @author MTK
 */
public class PhysicsObject  extends Actor
{
    public Vector2D position = new Vector2D(0, 0);
    public Vector2D velocity = new Vector2D(0, 0);
    public double angle = 0, angularVelocity = 0;
    public double mass = 1, momentOfInertia = 1;
    public double damping = 0, elasticity = 1, friction = 0.5;
    public double boundingRadius;
    
    public static Vector2D gravity = new Vector2D(0, 0);//-9.80665); //Gravity in meters per second per second
    
    public static double pixelsPerMeter = 10;   //The pixel equivalent of a meter, used for conversion to the computer screen's pixel-based coordinate system
    public static double framesPerSecond = 60;  //The amount of frames in a second. Used to scale forces that are applied every frame to equal Newtons,
                                                //and to scale velocities the are applied every act but are measured in <something> per second.
                                                
    /**
     * Updates this object's angle and position.
     * <br /><br />
     * Must be called <code>framesPerSecond</code> times per second if you want measurement units to be accurate.
     */
    
    public void act() {
        checkWallCollisions();
        checkCollisions();
        velocity = velocity.add(gravity.scale(1/framesPerSecond));
        velocity = velocity.scale(1-damping/framesPerSecond); //Apply gravity and damping N/A, gravity not defined
        
        angularVelocity *= 1-damping/framesPerSecond;                                              //Apply damping to rotation
        
        // Friction
        // Since friction is a constant force, let's not get complicated with F = ma, let's just decrement the velocity until we reach zero
        // friction field above should be in units pixels/tick^2
        
        // double fricXComp = friction * Math.cos(velocity.angle());
        // double fricYComp = friction * Math.sin(velocity.angle());
        
        // if (Math.abs(velocity.x) > Math.abs(fricXComp)) {
            // velocity.x -= Math.abs(fricXComp);
        // } else {
            // velocity.x = 0;
        // }
        
        // if (Math.abs(velocity.y) > Math.abs(fricYComp)) {
            // velocity.y -= Math.abs(fricYComp);
        // } else {
            // velocity.y = 0;
        // }
        
        
        // if (velocity.x > 0 && velocity.x > fricXComp) {
            // velocity.x = velocity.x - fricXComp ;
        // } else if (velocity.x > 0) {
            // velocity.x = 0;
        // } else if (velocity.x < 0 && velocity.x < fricXComp) {
            // velocity.x = velocity.x + fricXComp;
        // } else {
            // velocity.x = 0;
        // }
        
        // if (velocity.y > 0 && velocity.y > fricYComp) {
            // velocity.y = velocity.y - fricYComp;
        // } else if (velocity.y > 0) {
            // velocity.y = 0;
        // } else if (velocity.y < 0 && velocity.y < fricYComp) {
            // velocity.y = velocity.y + fricYComp;
        // } else {
            // velocity.y = 0;
        // }
        
        if (velocity.x > 0) {
            velocity.x = Math.max(velocity.x - friction, 0);
        } else {
            velocity.x = Math.min(velocity.x + friction, 0);
        }
        
        if (velocity.y > 0) {
            velocity.y = Math.max(velocity.y - friction, 0);
        } else {
            velocity.y = Math.min(velocity.y + friction, 0);
        }
        
        if (! (this instanceof Wall)) position = position.add(velocity.scale(1/framesPerSecond));                                 //Add velocity to position
        angle += angularVelocity / framesPerSecond;                                                 //Add angular velocity to angle
        
        
        setRotation((int) Math.toDegrees(angle));                                                   //Set the rotation of the Actor (Converting radians to degrees)
        if (! (this instanceof Wall)) setLocation((int) (position.x * pixelsPerMeter), /*getWorld().getHeight() -*/ (int) (position.y * pixelsPerMeter)); //Set the position of the Actor (Converting meters
                                                                                                                        //to pixels and flipping the y-axis)
    }
    
    /**
     * Apply a force to a certain point on this object.
     * <br /><br />
     * If called <code>framesPerSecond</code> times per second, the force will be in Newtons.
     * <code>point</code> will be automatically rotated relative to the object's angle.
     * <code>relative</code> chooses whether <code>force</code> will be rotated relative to this object's angle, too.
     */
    
    public void applyForce(Vector2D force, Vector2D point) {
        velocity = velocity.add(force.scale(1/mass));
        angularVelocity += point.crossProduct(force) / momentOfInertia;
    }
    
    /**
     * Apply an impulse to a certain point on this object.
     * <br /><br />
     * Forces the velocity of point <code>point</point> to <code>newVelocity</code>
     * <code>point</code> will be automatically rotated relative to the object's angle.
     * <code>relative</code> chooses whether <code>newVelocity</code> will be rotated relative to this object's angle, too.
     */
    
    public void applyImpulse(Vector2D newVelocity, Vector2D point) {
        velocity = newVelocity;
        angularVelocity += point.crossProduct(newVelocity) / momentOfInertia;
    }
    
    public void applyAcceleration(Vector2D force, Vector2D point) {
        force.scale(1/framesPerSecond);
        velocity = velocity.add(force);
        angularVelocity += point.crossProduct(force) / momentOfInertia;
    }
    
    /**
     * Get the velocity at a certain point on this object.
     * <br /><br />
     * <code>relative</code> chooses whether <code>point</code> is rotated relative to this object's angle.
     */
    
    public Vector2D getVelocity(Vector2D point) {
        Vector2D newVector = new Vector2D(point);
        newVector.x = -angularVelocity * point.y;
        newVector.y = angularVelocity * point.x;
        return newVector.add(velocity);
    }
    
    /**
     * Checks and processes collisions.
     * <br /><br />
     * Calls <code>checkCollsion()</code> with every other <code>PhysicsObject</code> in this <code>World</code> as the 
     */
    
    public void checkCollisions() {
        for(Object obj : getWorld().getObjects(PhysicsObject.class)) {
            PhysicsObject other = (PhysicsObject) obj;
            if(other != this) broadPhaseCollisionCheck(other);
        }
    }
    
    public void broadPhaseCollisionCheck(PhysicsObject other) {
        if((Math.abs(position.x-other.position.x) < boundingRadius+other.boundingRadius) && (Math.abs(position.y-other.position.y) < boundingRadius+other.boundingRadius)) {
                narrowPhaseCollisionCheck(other);
        }
    }
    
    public void narrowPhaseCollisionCheck(PhysicsObject other) {
    }
    
    /**
     * Handles a collsion with <code>other</code>.
     */
    
    public void handleCollision(PhysicsObject other, Vector2D n, Vector2D pa, Vector2D pb) {
        n = n.unit();
        Vector2D vpa = getVelocity(pa);
        Vector2D vpb = other.getVelocity(pb);
        Vector2D vab = vpa.subtract(vpb);
        if(vab.dotProduct(n) < 0) {
            double e = (elasticity + other.elasticity) / 2;
            double j = (-(1 + e) * vab.dotProduct(n)) / (1/mass + 1/other.mass + Math.pow(pa.crossProduct(n), 2)/momentOfInertia + Math.pow(pb.crossProduct(n), 2)/other.momentOfInertia);
            velocity = velocity.add(n.scale(1/mass).scale(j));
            angularVelocity += pa.crossProduct(n.scale(j)) / momentOfInertia;
            other.velocity = other.velocity.subtract(n.scale(1/other.mass).scale(j));
            other.angularVelocity -= pb.crossProduct(n.scale(j)) / other.momentOfInertia;
        }
    }
    
    /*public void handleCollision(PhysicsObject other, Vector2D n, Vector2D pa, Vector2D pb) {
        Vector2D mpa = getVelocity(pa).scale(mass);
        Vector2D mpb = other.getVelocity(pb).scale(other.mass);
        Vector2D mab = mpa.subtract(mpb);
        Vector2D mpaNorm = mpa.projectOnto(n);
        Vector2D mpaTang = mpa.projectOnto(n.perpendicular());
        Vector2D mpbNorm = mpb.projectOnto(n);
        Vector2D mpbTang = mpb.projectOnto(n.perpendicular());
        if(mab.dotProduct(n) < 0) {
            double e = (elasticity + other.elasticity) / 2;
            double f = (friction + other.friction) / 2;
            velocity = mpbNorm.scale(e).add(mpaTang.scale(1-f).add(mpbTang.scale(f))).scale(1/mass);
            other.velocity = mpaNorm.scale(e).add(mpbTang.scale(1-f).add(mpaTang.scale(f))).scale(1/other.mass);
        }
    }*/
    
    /*public void handleCollision(PhysicsObject other, Vector2D n, Vector2D pa, Vector2D pb) {
        n = n.unit();
        Vector2D t = n.perpendicular();
        Vector2D upa = getVelocity(pa).scale(mass);
        Vector2D upb = other.getVelocity(pb).scale(other.mass);
        Vector2D uab = upa.subtract(upb);
        Vector2D uaNorm = upa.projectOnto(n);
        Vector2D uaTang = upa.projectOnto(t);
        Vector2D ubNorm = upb.projectOnto(n);
        Vector2D ubTang = upb.projectOnto(t);
        if(uab.dotProduct(n) < 0) {
            double e = (elasticity + other.elasticity) / 2;
            double f = (friction + other.friction) / 2;
            Vector2D vaNorm = getPostCollisionVelocity(uaNorm, mass, ubNorm, other.mass).scale(e);
            Vector2D vbNorm = getPostCollisionVelocity(ubNorm, other.mass, uaNorm, mass).scale(e);
            Vector2D vaTang = uaTang.scale(1-f);
            Vector2D vbTang = ubTang.scale(1-f);
            velocity = vaNorm.add(vaTang);
            other.velocity = vbNorm.add(vbTang);
            angularVelocity += (vaTang.length() * f) / momentOfInertia;
            other.angularVelocity += (vbTang.length() * f) / other.momentOfInertia;
        }
    }*/
    
    public static Vector2D getPostCollisionVelocity(Vector2D v1, double m1, Vector2D v2, double m2) {
        return v1.scale(m1-m2).add(v2.scale(2*m2)).scale(1/(m1+m2));
    }
    
    /**
     * Checks and processes wall collsions.
     * <br /><br />
     * This cannot be implemented here because it depends on the shape, which is undefined in this class, so it should be implemented in a subclass.
     */
    
    public void checkWallCollisions() {
        //Must be implemented in a subclass
    }
    
    /**
     * Handles a collision with an immovable object.
     */
    
    public void handleWallCollision(Vector2D n, Vector2D point) {
        n = n.unit();
        Vector2D vp = getVelocity(point);
        if(vp.dotProduct(n) < 0) {
            double j = (-(1 + elasticity) * vp.dotProduct(n)) / (1/mass + Math.pow(point.crossProduct(n), 2) / momentOfInertia);
            velocity = velocity.add(n.scale(1/mass).scale(j));
            angularVelocity += point.crossProduct(n.scale(j)) / momentOfInertia;
        }
    }
}
