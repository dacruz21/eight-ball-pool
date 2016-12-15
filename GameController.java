import greenfoot.*;

public class GameController extends Actor{

    final double MIN_POWER = 16, MAX_POWER = 128; // The ranges for how far we can pull the cue back, if less than min, cancel the shot, if greater than max, cap the power at the max
    
    Vector2D force = new Vector2D(1,0);
    double angle, power;
    GameState state = GameState.FIRST_RUN;
    PoolTable table;
    MouseInfo mi;
    int turnNumber = 1;
    String p1Name, p2Name;

    int mouseX, mouseY, cueBallX, cueBallY, origMouseX, origMouseY;
    PlayerLight p1Light, p2Light;
    boolean turnPlayerOne; // easiest to use a boolean if the current turn is for player one, or false for two
    Boolean playerOneSolid; // if true, p1 is solid and p2 is striped, if  false, p1 is striped and p2 is solid, if null no one has sunk a ball yet
    boolean keepTurn;
    boolean canMoveCue;

    public GameController(PoolTable table) {
        this.table = table;
    }
    
    public boolean isPlayerOneTurn() {
        return turnPlayerOne;
    }
    
    public void sunkSolid(GreenfootImage image) {
        if (playerOneSolid == null) { // when its time to pick sides
            if (turnPlayerOne) { // if its p1s turn
                playerOneSolid = true;
                table.getPlayer1Rack().setSolid(true);
                table.getPlayer1Rack().init();
                table.getPlayer2Rack().setSolid(false);
                table.getPlayer2Rack().init();
            } else { // or p2s
                playerOneSolid = false;
                table.getPlayer1Rack().setSolid(false);
                table.getPlayer1Rack().init();
                table.getPlayer2Rack().setSolid(true);
                table.getPlayer2Rack().init();
            }
            keepTurn = true;
        } else {
            keepTurn = !(playerOneSolid ^ turnPlayerOne); // not xor, trust me the logic is sound
        }
    }
    
    public void sunkStriped(GreenfootImage image) {
        if (playerOneSolid == null) { // when its time to pick sides
            if (turnPlayerOne) { // if its p1s turn
                playerOneSolid = false;
                table.getPlayer1Rack().setSolid(false);
                table.getPlayer1Rack().init();
                table.getPlayer2Rack().setSolid(true);
                table.getPlayer2Rack().init();
            } else { // or p2s
                playerOneSolid = true;
                table.getPlayer1Rack().setSolid(true);
                table.getPlayer1Rack().init();
                table.getPlayer2Rack().setSolid(false);
                table.getPlayer2Rack().init();
            }
            keepTurn = true;
        } else {
            keepTurn = playerOneSolid ^ turnPlayerOne;
        }
    }
    
    public void sunkCue() {
        table.getCueBall().position = new Vector2D((double) 512 / 10, (double) 278 / 10);
        table.getCueBall().setLocation(512, 278);
        keepTurn = false;
        canMoveCue = true;
    }
    
    public void sunkEight() {
        
        if (playerOneSolid && turnPlayerOne && table.getSolidBalls().length == 0) {
            table.showText(p1Name + " wins!", 512, 250);
            Greenfoot.stop();
        } else if (!playerOneSolid && !turnPlayerOne && table.getStripedBalls().length == 0) {
            table.showText(p2Name + " wins!", 512, 250);
            Greenfoot.stop();
        }
        
        table.showText( ( turnPlayerOne ? p2Name : p1Name ) + " wins!", 512, 250);
        Greenfoot.stop();
    }

    public void act() {

        mi = Greenfoot.getMouseInfo(); // repoll the mouse every tick

        switch(state) {
            case FIRST_RUN:
                p1Name = Greenfoot.ask("Player 1 Name?");
                p2Name = Greenfoot.ask("Player 2 Name?");
                
                table.showText(p1Name, 283, 510);
                table.showText(p2Name, 741, 510);
                
                turnPlayerOne = true;
                
                state = GameState.PREPARE;
                break;
            case PREPARE:
                if (mi != null) { // Can't start polling the mouse until it enters the stage
                    state = GameState.AIMING;
                }
                break;
            case AIMING:
    
                if (mi == null) { // without this, we get an NPE if the mouse moves outside the window
                    state = GameState.PREPARE;
                    break;
                }
                
                table.getPlayer1Light().set(turnPlayerOne);
                table.getPlayer2Light().set(!turnPlayerOne);
                
                keepTurn = false;
    
                mouseX = mi.getX();
                mouseY = mi.getY();
    
                cueBallX = table.getCueBall().getX();
                cueBallY = table.getCueBall().getY();
    
                angle = Math.atan2(cueBallY - mouseY, mouseX - cueBallX);
                
                if (Greenfoot.mouseDragged(table.getCueBall()) && (turnNumber == 1 || canMoveCue) ) { // only let the player drag the cue if it is the first turn or if their opponent sunk the cue
                    state = GameState.DRAG_CUE;
                    break;
                }   
    
                if (Greenfoot.mouseDragged(null)) {
                    state = GameState.RECORD_MOUSE;
                }
                break;
            case DRAG_CUE:
                if (mi == null) { // Can't start polling the mouse until it enters the stage
                    break;
                }
                mouseX = mi.getX();
                mouseY = mi.getY();
                
                // clamp the cue ball position to the table
                int cueX = Math.max(mouseX, 72);
                int cueY = Math.max(mouseY, 72);
                cueX = Math.min(cueX, 952);
                cueY = Math.min(cueY, 494);
                
                // and if it is turn 1, clamp its position to the box
                if (turnNumber == 1) {
                    cueX = Math.min(cueX, 283);
                }
                
                table.getCueBall().setLocation(cueX, cueY);
                table.getCueBall().position.x = (double) cueX / 10;
                table.getCueBall().position.y = (double) cueY / 10;
                
                
                if (Greenfoot.mouseDragEnded(null)) {
                    state = GameState.AIMING;
                }
                
                break;
            case RECORD_MOUSE:
                if (mi == null) { // without this, we get an NPE if the mouse moves outside the window
                    state = GameState.PREPARE;
                    break;
                }
                origMouseX = mouseX;
                origMouseY = mouseY;
                state = GameState.POWER;
                break;
            case POWER:
                if (mi == null) { // without this, we get an NPE if the mouse moves outside the window
                    state = GameState.PREPARE;
                    break;
                }
                mouseX = mi.getX();
                mouseY = mi.getY();
    
                power = Math.sqrt(Math.pow(origMouseY - mouseY, 2) + Math.pow(mouseX - origMouseX, 2));
    
                if (Greenfoot.mouseDragEnded(null)) {
                    state = GameState.LAUNCH;
                }
                break;
            case LAUNCH:
                if (mi == null) { // without this, we get an NPE if the mouse moves outside the window
                    state = GameState.PREPARE;
                    break;
                }
                if (power < MIN_POWER) {
                    state = GameState.AIMING;
                    break;
                }
    
                table.getCueBall().applyForce(force.rotate(angle).scale(Math.min(power, MAX_POWER)), new Vector2D(0,0));
                canMoveCue = false;
                state = GameState.WAIT;
                turnNumber++;
                break;
            case WAIT:
                if (mi == null) { // without this, we get an NPE if the mouse moves outside the window
                    state = GameState.PREPARE;
                    break;
                }
                
                boolean notReady = false;
    
                for (SolidBall ball : table.getSolidBalls()) {
                    if (ball.velocity.x != 0 || ball.velocity.y != 0) {
                        notReady = true;
                    }
                }
    
                for (StripedBall ball : table.getStripedBalls()) {
                    if (ball.velocity.x != 0 || ball.velocity.y != 0) {
                        notReady = true;
                    }
                }
    
                if (table.getCueBall().velocity.x != 0 || table.getCueBall().velocity.y != 0) {
                    notReady = true;
                }
    
                if (table.getEightBall().velocity.x != 0 || table.getEightBall().velocity.y != 0) {
                    notReady = true;
                }
    
                if (notReady) break;
                
                if (!keepTurn) {
                    turnPlayerOne = !turnPlayerOne;
                }
    
                state = GameState.AIMING;
                break;
            default:
            state = GameState.PREPARE;
            break;

        }
    }
}