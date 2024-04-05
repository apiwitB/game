import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;



public class Menu extends JPanel {
    private JButton startButton;
    private JFrame frame;
    private BufferedImage startImage;
    // private BufferedImage gameOverImage;
    private boolean isGameStarted = false;
    private Sound themeSound;


    public Menu() {
        frame = new JFrame("Game Menu");
        frame.setSize(1920, 755);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        themeSound = new Sound(FilePath.THEME_SOUND);


        try {
            startImage = ImageIO.read(new File(FilePath.manuGame));
            if (startImage == null) {
                System.err.println("Unrecognized image type.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error while loading image.");
            System.exit(1);
        }

        setLayout(new BorderLayout());

        startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        add(startButton, BorderLayout.PAGE_END);

        frame.add(this);
        frame.setVisible(true);
    }



    private void startGame() {
        JFrame game = new JFrame();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setSize(1920, 755);
        bulidMap buildMapData = new bulidMap();
        game.add(new PlatformScene(buildMapData));
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        themeSound.play();
        frame.dispose(); 
        
    }

    

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!isGameStarted && startImage != null) {
            g.drawImage(startImage, 0, 0, getWidth(), getHeight(), this);
        }
    }


}
