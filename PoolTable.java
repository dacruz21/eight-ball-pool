import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class PoolTable here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class PoolTable extends World
{
    
    EightBall eightBall = new EightBall();
    CueBall cueBall = new CueBall();
    
    OneBall oneBall;
    TwoBall twoBall;
    ThreeBall threeBall;
    FourBall fourBall;
    FiveBall fiveBall;
    SixBall sixBall;
    SevenBall sevenBall;
    
    NineBall nineBall;
    TenBall tenBall;
    ElevenBall elevenBall;
    TwelveBall twelveBall;
    ThirteenBall thirteenBall;
    FourteenBall fourteenBall;
    FifteenBall fifteenBall;
    
    GameController gameController;
    PlayerLight p1Light, p2Light;
    
    // BallCounter[] p1Counters = new BallCounter[7];
    // BallCounter[] p2Counters = new BallCounter[7];
    
    BallRack p1Rack;
    BallRack p2Rack;

    /**
     * Constructor for objects of class PoolTable.
     * 
     */
    public PoolTable()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1024, 556, 1);

        prepare();
    }
    
    public EightBall getEightBall() {
        return eightBall;
    }
    
    public CueBall getCueBall() {
        return cueBall;
    }
    
    public SolidBall[] getSolidBalls() {
        SolidBall[] r = new SolidBall[7];
        r[0] = oneBall;
        r[1] = twoBall;
        r[2] = threeBall;
        r[3] = fourBall;
        r[4] = fiveBall;
        r[5] = sixBall;
        r[6] = sevenBall;
        
        return r;
    }
    
    public StripedBall[] getStripedBalls() {
        StripedBall[] r = new StripedBall[7];
        r[0] = nineBall;
        r[1] = tenBall;
        r[2] = elevenBall;
        r[3] = twelveBall;
        r[4] = thirteenBall;
        r[5] = fourteenBall;
        r[6] = fifteenBall;
        
        return r;
    }
    
    public GameController getController() {
        return gameController;
    }
    
    public PlayerLight getPlayer1Light() {
        return p1Light;
    }
    
    public PlayerLight getPlayer2Light() {
        return p2Light;
    }
    
    public BallRack getPlayer1Rack() {
        return p1Rack;
    }
    
    public BallRack getPlayer2Rack() {
        return p2Rack;
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
        // Instantiate Cue Ball and Eight Ball first, b/c we have getter methods for them
        // Position is irrelevant and overriden by the physics engine in the first tick
        addObject(cueBall, 0, 0);
        
        addObject(eightBall, 0, 0);
        
        // All the other balls
        oneBall = new OneBall();
        addObject(oneBall, 0, 0);
        
        twoBall = new TwoBall();
        addObject(twoBall, 0, 0);
        
        threeBall = new ThreeBall();
        addObject(threeBall, 0, 0);
        
        fourBall = new FourBall();
        addObject(fourBall, 0, 0);
        
        fiveBall = new FiveBall();
        addObject(fiveBall, 0, 0);
        
        sixBall = new SixBall();
        addObject(sixBall, 0, 0);
        
        sevenBall = new SevenBall();
        addObject(sevenBall, 0, 0);
        
        nineBall = new NineBall();
        addObject(nineBall, 0, 0);
        
        tenBall = new TenBall();
        addObject(tenBall, 0, 0);
        
        elevenBall = new ElevenBall();
        addObject(elevenBall, 0, 0);
        
        twelveBall = new TwelveBall();
        addObject(twelveBall, 0, 0);
        
        thirteenBall = new ThirteenBall();
        addObject(thirteenBall, 0, 0);
        
        fourteenBall = new FourteenBall();
        addObject(fourteenBall, 0, 0);
        
        fifteenBall = new FifteenBall();
        addObject(fifteenBall, 0, 0);
        
        // Holes
        
        Hole topLeft = new Hole(this);
        addObject(topLeft, 50, 50);
        
        Hole top = new Hole(this);
        addObject(top, 512, 37);
        
        Hole topRight = new Hole(this);
        addObject(topRight, 974, 50);
        
        Hole bottomLeft = new Hole(this);
        addObject(bottomLeft, 50, 514);
        
        Hole bottom = new Hole(this);
        addObject(bottom, 512, 527);
        
        Hole bottomRight = new Hole(this);
        addObject(bottomRight, 974, 514);
        
        // Walls
        
        for (int x = 99; x < 474; x+= 23) {
            addObject(new Wall(x, 37), 0, 0);
            addObject(new Wall(x + 460, 37), 0, 0);
            addObject(new Wall(x, 528), 0, 0);
            addObject(new Wall(x + 460, 528), 0, 0);
        }
        
        for (int y = 98; y < 467; y += 23) {
             addObject(new Wall(38, y), 0, 0);
             addObject(new Wall(985, y), 0, 0);
        }
        
        // Ball racks
        
        p1Rack = new BallRack(true);
        addObject(p1Rack, 118, 539);
        p2Rack = new BallRack(false);
        addObject(p2Rack, 574, 539);
        
        // // Ball counters
        // for (int i = 0; i < 7; i++) {
            // BallCounter counter = new BallCounter();
            // addObject(counter, 118 + 34 * i, 539);
            // p1Counters[i] = counter;
        // }
        
        // for (int i = 0; i < 7; i++) {
            // BallCounter counter = new BallCounter();
            // addObject(counter, 574 + 34 * i, 539);
            // p2Counters[i] = counter;
        // }
        
        // Various logic controllers
        gameController = new GameController(this);
        addObject(gameController, 0, 0);
        
        p1Light = new PlayerLight();
        addObject(p1Light, 86, 545);
        
        p2Light = new PlayerLight();
        addObject(p2Light, 541, 545);
    }
}
