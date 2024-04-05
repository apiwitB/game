import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class Map {
    private int[][] map;
    private BufferedImage[] mapTiles;
    private int tileWidth;
    private int tileHeight;
    private bulidMap data;

    public Map(bulidMap data) {
        this.data = data;
        map = this.data.getMapdata();
        mapTiles = new BufferedImage[50];

        for (int i = 0; i < 27; i++) {
            try {
                String filename = String.format("%s%d.png", FilePath.TILE_IMAGE_PATH, i + 1);
                mapTiles[i] = ImageIO.read(new File(filename));
                if (mapTiles[i] == null) {
                    System.err.println("Unrecognized image type.");
                    System.exit(1);
                }
            } catch (IOException e) {
                System.err.println("Error while loading image.");
                System.exit(1);
            }
        }

        tileWidth = mapTiles[0].getWidth();
        tileHeight = mapTiles[0].getHeight();
    }



    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileAt(int x, int y) {
        int row = y / tileHeight;
        int col = x / tileWidth;
        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length)
            return map[row][col];
        else
            return 0;
    }

    // Check if tile at (x, y) can be stood on
    public boolean isPlatform(int x, int y) {
        int tileIndex = getTileAt(x, y);
        return (tileIndex == 1 || tileIndex == 2 || tileIndex == 3 || tileIndex == 5 ||
                tileIndex == 6 || tileIndex == 11 || tileIndex == 12 || tileIndex == 15 || 
                tileIndex == 16|| tileIndex == 17 );
    }

    // public boolean isheadPlatform(int x,int y){
    //     int tileIndex = getTileAt(x, y);
    //     return (tileIndex == 2 || tileIndex == 3 || tileIndex == 1);
    // }

    // Check if tile at (x, y) cannot be passed through
    public boolean isBlocking(int x, int y) {
        int tileIndex = getTileAt(x, y);
        return (tileIndex == 1 || tileIndex == 2 || tileIndex == 3 || tileIndex == 4 || 
                tileIndex == 5 || tileIndex == 7 || tileIndex == 6 || tileIndex == 8 ||
                tileIndex == 15 );
    }

    public void paint(Graphics g, Rectangle viewBox) {
        int firstColumn = viewBox.x / tileWidth;
        int nColumns = viewBox.width / tileWidth + 2;
        int xOffset = -viewBox.x % tileWidth;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < nColumns; j++) {
                if (firstColumn + j < map[i].length) {
                    int tileIndex = map[i][firstColumn + j] - 1;
                    if (tileIndex >= 0)
                        g.drawImage(mapTiles[tileIndex], j * tileWidth + xOffset, i * tileHeight - viewBox.y, null);
                }
            }
        }
    }

    
    
}
