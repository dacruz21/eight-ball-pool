import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 2-Dimensional vector with math methods.
 * 
 * @author MTK
 */
public class Vector2D {
    public double x, y;
    
    public static Vector2D tenFactory(double x, double y) {
        return new Vector2D(x / 10, y / 10);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(Vector2D other) {
        this(other.x, other.y);
    }
    
    public Vector2D() {
        this(0, 0);
    }
    
    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }
    
    public Vector2D subtract(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }
    
    public Vector2D invert() {
        return new Vector2D(-x, -y);
    }
    
    public Vector2D scale(double factor) {
        return new Vector2D(x * factor, y * factor);
    }
    
    public Vector2D rotate(double angle) {
        Vector2D newVector = new Vector2D();
        newVector = newVector.addPolar(length(), angle() + angle);
        return newVector;
    }
    
    public Vector2D addPolar(double distance, double angle) {
        return new Vector2D(x + Math.sin(angle) * distance, y + Math.cos(angle) * distance);
    }
    
    public double length() {
        return Math.hypot(x, y);
    }
    
    public double angle() {
        return Math.atan2(x, y);
    }
    
    public double crossProduct(Vector2D other) {
        return y * other.x - x * other.y;
    }
    
    public double dotProduct(Vector2D other) {
        return x * other.x + y * other.y;
    }
    
    public Vector2D projectOnto(Vector2D other) {
        double dp = dotProduct(other);
        return new Vector2D(dp*other.x, dp*other.y);
    }
    
    public Vector2D unit() {
        return new Vector2D().addPolar(1, angle());
    }
    
    public Vector2D perpendicular() {
        return new Vector2D(y, -x);
    }
    
    public String toString() {
        return "Vector2D(" + x + ", " + y + ")";
    }
}
