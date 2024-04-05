import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteAnimation {
    private BufferedImage[] sprites;
    private int framesPerSprite;
    private int frameCounter;
    private int spriteIndex;

    public SpriteAnimation(String filePrefix, int nSprites, int framesPerSprite) {
        sprites = new BufferedImage[nSprites];
        for (int i = 0; i < nSprites; i++) {
            try {
                String filename = String.format("%s%d.png", filePrefix, i);
                sprites[i] = ImageIO.read(new File(filename));
                if (sprites[i] == null) {
                    System.err.println("Unrecognized image type.");
                    System.exit(1);
                }
            } catch (IOException e) {
                System.err.println("Error while loading image.");
                System.exit(1);
            }
        }

        this.framesPerSprite = framesPerSprite;
        this.frameCounter = 0;
        this.spriteIndex = 0;
    }

    public int getWidth() {
        return sprites[spriteIndex].getWidth();
    }

    public int getHeight() {
        return sprites[spriteIndex].getHeight();
    }

    public void resetFrameCounter() {
        frameCounter = 0;
        spriteIndex = 0;
    }

    public void update(double deltaTime) {
        frameCounter++;
        spriteIndex = (frameCounter / framesPerSprite) % sprites.length;
    }

    public void paint(Graphics g, int x, int y) {
        g.drawImage(sprites[spriteIndex], x, y, null);
    }
}
