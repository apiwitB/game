import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObject {
    private PlatformScene scene;
    private SpriteAnimation idleSprites;
    private SpriteAnimation runningSprites;
    private SpriteAnimation runningSpritesl;
    private SpriteAnimation jumpingUpSprites;
    private SpriteAnimation jumpingUpSpritesl;
    private SpriteAnimation jumpingDownSprites;
    private int x, y;
    private int speed = 10;
    private int gravity = 1;
    private int initialJumpSpeed = -20;
    private int verticalSpeed = 0;
    private boolean grounded = false;
    private int width;
    private int height;
    private Sound jumpSound;
    private Sound dieSound;
    private Sound gameWinSound;
    private int previousY;
    public int live = 3;
    private Menu menu;
    private Sound sound;



    private enum ActionState { IDLE, RUNNINGr, RUNNINGl, JUMPING, JUMPINGl }
    private ActionState actionState = ActionState.IDLE;

    public Player(PlatformScene scene, int x, int y) {
        this.scene = scene;
        this.previousY = y;
        jumpSound = new Sound("E:/game-project/audio/jump.wav");

        idleSprites = new SpriteAnimation("E:/game-project/Assets/images/mario/Idle_", 1, 1);
        runningSprites = new SpriteAnimation("E:/game-project/Assets/images/mario/Run_", 3, 5);
        runningSpritesl = new SpriteAnimation("E:/game-project/Assets/images/mario/Runl_", 3, 5);
        jumpingUpSprites = new SpriteAnimation("E:/game-project/Assets/images/mario/Jump_", 1, 1);
        jumpingUpSpritesl = new SpriteAnimation("E:/game-project/Assets/images/mario/Jumpl_", 1, 1);
        jumpingDownSprites = new SpriteAnimation("E:/game-project/Assets/images/mario/Jump_", 1, 1);


        this.x = x - idleSprites.getWidth() / 2;
        this.y = y - idleSprites.getHeight() / 2;
    }

    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    

    private SpriteAnimation getCurrentSprites() {
        switch (actionState) {
            case IDLE:
                return idleSprites;
            case RUNNINGr:
                return runningSprites;
            case RUNNINGl:
                return runningSpritesl;
            case JUMPING:
                if (verticalSpeed < 0)
                    return jumpingUpSprites;
                else
                    return jumpingDownSprites;
            case JUMPINGl:
                if (verticalSpeed < 0){
                    return jumpingUpSpritesl;
                } else{
                    return jumpingUpSpritesl;
                }
            default:
                return idleSprites;
        }
    }

    private void setActionState(ActionState newActionState) {
        if (actionState != newActionState) {
            switch (newActionState) {
                case IDLE:
                    idleSprites.resetFrameCounter();
                    break;
                case RUNNINGr:
                    runningSprites.resetFrameCounter();
                    break;
                case RUNNINGl:
                    runningSpritesl.resetFrameCounter();
                    break;
                case JUMPING:
                    if (grounded)
                        verticalSpeed = initialJumpSpeed;
                    break;
                case JUMPINGl:
                    if (grounded)
                        verticalSpeed = initialJumpSpeed;
            }
            actionState = newActionState;
        }
    }

    private void tryMoveX(int dx) {
        int newX = x + dx;
        int refX;
        Map map = scene.getMap();
        if (dx < 0) {
            refX = newX;
            if (map.isBlocking(refX, y)) {
                int newRefX = (refX / map.getTileWidth() + 1) * map.getTileWidth();
                newX = newX + (newRefX - refX);
            }
        } else {
            refX = newX + getCurrentSprites().getWidth();
            if (map.isBlocking(refX, y)) {
                int newRefX = refX / map.getTileWidth() * map.getTileWidth();
                newX = newX + (newRefX - refX);
            }
        }

        x = newX;
    }

    
    

    private void tryMoveY(int dy) {
        int newY = y + dy;
        // footX and footY is the anticipated position of Player's feet, used
        // for checking if Player will be on a platform
        int footX = x + getCurrentSprites().getWidth() / 2;
        int footY = newY + getCurrentSprites().getHeight();
        Map map = scene.getMap();
        // headX and headY is the position of Player's head
        int headX = x + getCurrentSprites().getWidth() / 2;
        int headY = newY; // อาจต้องปรับเป็นตำแหน่งของหัวตัวละครตามการออกแบบของเกม


        // Negative verticalSpeed means jumping up -- we check for platform
        // grounding only when jumping (falling) down
        if (verticalSpeed >= 0  && map.isPlatform(footX, footY)) {
            int groundLevel = footY / map.getTileHeight() * map.getTileHeight();
            int sinkingDepth = footY - groundLevel;
            // If the anticipated foot position sinks within a specified
            // amount (32 here) into the platform, place Player back on the
            // ground level, otherwise let Player falls further
            if (sinkingDepth < 32 ) {
                newY = newY - sinkingDepth;
                verticalSpeed = 0;
                grounded = true;
            } else {
                grounded = false;
            }
        } else {
            grounded = false;
        }

        if (map.isBlocking(headX, headY)) {
            verticalSpeed = 15;
            grounded = false;
        }
    
        // เปลี่ยนตำแหน่งของตัวละครใหม่
        y = newY;


        // If Player falls off the screen, put it back at the beginning
        if (newY < 1024) {
            y = newY;
        } else {
            verticalSpeed = 0;
            x = 100;
            y = 200;
            live(1);
        }
    }

    @Override
    public void update(double deltaTime) {
        boolean idle = true;
        if (scene.getKeyState(GameScene.KEY_LEFT) == GameScene.KeyState.DOWN) {
            setActionState(ActionState.RUNNINGl);
            tryMoveX(-speed);
            idle = false;
        }

        if (scene.getKeyState(GameScene.KEY_RIGHT) == GameScene.KeyState.DOWN) {
            setActionState(ActionState.RUNNINGr);
            tryMoveX(speed);
            idle = false;
        }

        if (scene.getKeyState(GameScene.KEY_JUMP) == GameScene.KeyState.DOWN) {
           if (grounded) {
            jumpSound.play();
           }
            if (scene.getKeyState(GameScene.KEY_LEFT) == GameScene.KeyState.DOWN||grounded) {
                setActionState(ActionState.JUMPINGl);
            } else {
                setActionState(ActionState.JUMPING);
            }
            idle = false;
        }

        // Player is always pulled down by gravity
        verticalSpeed += gravity;
        tryMoveY(verticalSpeed);

        if (!grounded) {
            if (scene.getKeyState(GameScene.KEY_LEFT) == GameScene.KeyState.DOWN) {
                setActionState(ActionState.JUMPINGl);
            }else
                setActionState(ActionState.JUMPING);
        } else if (idle) {
            setActionState(ActionState.IDLE);
        }

        getCurrentSprites().update(deltaTime);


    }

    @Override
    public void draw(Graphics g) {
        Rectangle viewBox = scene.getViewBox();
        int relX = x - viewBox.x;
        int relY = y - viewBox.y;
        getCurrentSprites().paint(g, relX, relY);
    }

    public void live(int hit){
        live -= hit;
        this.live = live;
        System.out.printf("%d",live);
        if (live <1) {
            gameOver();
        }
    }

    public void gameOver(){
        // sound.stop();
        System.err.println("end");
        
        // sound.GameOversong();

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, idleSprites.getWidth(), idleSprites.getHeight());
    }

    
}
