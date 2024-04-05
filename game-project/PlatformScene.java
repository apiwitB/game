import java.awt.Graphics;
import java.awt.Rectangle;

public class PlatformScene extends GameScene {
    private Player player;
    private Map map;
    private Background background;
    private bulidMap buildMapData;
    private double deltaTime;

    public PlatformScene(bulidMap buildMapData) {
        this.buildMapData = buildMapData;
        player = new Player(this, 100, 200);
        map = new Map(buildMapData);
        background = new Background();
        addGameObject(player);
    }

    @Override
    public void update(double deltaTime) {
        this.deltaTime = deltaTime;
    }
///////////////
    @Override
    public void render(Graphics g) {
        Rectangle viewBox = getViewBox();
        background.paint(g, viewBox);
        map.paint(g, viewBox);
        g.drawString(String.format("ViewBox: x: %d, y: %d, w: %d, h: %d ",
                                   viewBox.x, viewBox.y, viewBox.width, viewBox.height), 10, 20);
    }
////////////////////
    public Map getMap() {
        return map;
    }

    public Rectangle getViewBox() {
        Rectangle playerRect = player.getBounds();
        int xPlayer = playerRect.x + playerRect.width / 2;
        int leftEdge = xPlayer - getWidth() / 2;
        // Clip the left edge of the view box
        if (leftEdge < 0)
            leftEdge = 0;
        return new Rectangle(leftEdge, 0, getWidth(), getHeight());
    }
}
