import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject {
    private boolean destroyed = false;

    public GameObject() {
        destroyed = false;
    }

    public abstract void update(double deltaTime);

    public abstract void draw(Graphics g);

    public abstract Rectangle getBounds();

    public boolean collides(GameObject other) {
        if (isDestroyed() || other.isDestroyed()
            || getBounds() == null || other.getBounds() == null) {
            return false;
        } else {
            return getBounds().intersects(other.getBounds());
        }
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
