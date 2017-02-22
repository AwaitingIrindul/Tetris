package View;

import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Irindul on 22/02/2017.
 */
public interface DisplayBlock {

    void draw(GraphicsContext g);
    void undraw(GraphicsContext g);
}
