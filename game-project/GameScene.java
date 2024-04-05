import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.ArrayList;

public abstract class GameScene extends JPanel {
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;
    public static final int KEY_LEFT = 2;
    public static final int KEY_RIGHT = 3;
    public static final int KEY_JUMP = 4;

    protected Menu menu;
    protected Sound sound;
    private Player player;

    public enum KeyState { UP, DOWN }

    private KeyState[] keyStates = new KeyState[5];

    private static final int DEFAULT_FRAME_RATE = 60;
    private long previousTime;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public GameScene() {
        this(DEFAULT_FRAME_RATE);
    }

    public GameScene(int frameRate) {

        setBackground(Color.BLACK);
        setFocusable(true);
        setDoubleBuffered(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyStates[KEY_UP] = KeyState.DOWN;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyStates[KEY_DOWN] = KeyState.DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyStates[KEY_LEFT] = KeyState.DOWN;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyStates[KEY_RIGHT] = KeyState.DOWN;
                        break;
                    case KeyEvent.VK_SPACE:
                        keyStates[KEY_JUMP] = KeyState.DOWN;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keyStates[KEY_UP] = KeyState.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyStates[KEY_DOWN] = KeyState.UP;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyStates[KEY_LEFT] = KeyState.UP;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyStates[KEY_RIGHT] = KeyState.UP;
                        break;
                    case KeyEvent.VK_SPACE:
                        keyStates[KEY_JUMP] = KeyState.UP;
                        break;
                }
            }
        });

        previousTime = System.currentTimeMillis();

        Timer timer = new Timer(1000/frameRate, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAll();
            }
        });

        timer.start();
        
       

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }
    
    public void playSound(String soundName){
        Sound sound = new Sound(soundName);
        sound.play();

    }

    public void addGameObject(GameObject object) {
        gameObjects.add(object);
    }

    public KeyState getKeyState(int key) {
        return keyStates[key];
    }

    // Call update() on the game scene and all game objects
    private void updateAll() {
        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - previousTime) / 1000.0f;
        previousTime = currentTime;

        update(deltaTime);

        int i = 0;
        while (i < gameObjects.size()) {
            GameObject gameObject = gameObjects.get(i);
            if (!gameObject.isDestroyed()) {
                gameObject.update(deltaTime);
                i++;
            } else {
                // Prune destroyed game objects
                gameObjects.remove(i);
            }
        }

        
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g);
        }
    }

    public abstract void update(double deltaTime);
    public abstract void render(Graphics g);
}
