import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


import javax.imageio.ImageIO;



public class Background {
    private BufferedImage image;

  
    public Background() {
        try {
            image = ImageIO.read(new File(FilePath.BACKGROUND_IMAGE));
            if (image == null) {
                System.err.println("Unrecognized image type.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error while loading image.");
            System.exit(1);
        }
    }



    public void paint(Graphics g, Rectangle viewBox) {

        int imageWidth = image.getWidth();
        int leftEdge = -(viewBox.x / 2) % imageWidth;

        int xShift = viewBox.x % imageWidth; // คำนวณค่าที่จะขยับภาพเมื่อ viewBox เปลี่ยน
        int xStart = -xShift;


        for (int x = xStart; x < viewBox.x + viewBox.width; x += imageWidth) {
            for (int y = -viewBox.y; y < viewBox.y + viewBox.height; y += image.getHeight()) {
                g.drawImage(image, x, y, null);
            }
        }
    }
}
