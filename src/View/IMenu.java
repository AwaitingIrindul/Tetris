package View;

import javafx.scene.Scene;

/**
 * Created by Irindul on 26/03/2017.
 * Interface to implement Observer/Observable
 */
public interface IMenu {

    void reset(Scene s);
    void launchAI(Scene s);
    void goBackToMenu();

}
