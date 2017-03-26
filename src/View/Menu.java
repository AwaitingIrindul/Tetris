package View;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by Irindul on 26/03/2017.
 */
public class Menu extends Application implements IMenu {
    public final static int WIDTH = 300;
    public final static int HEIGHT = 300;

    private Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        //createHandlers(scene);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        scene.getStylesheets().add("style/menu.css");


        primaryStage.setTitle("Blocks puzzle game");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);



        primaryStage.show();
        stage = primaryStage;


        createHandlers();
    }

    private void createHandlers(){
        stage.getScene().setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                stage.fireEvent(
                        new WindowEvent(
                                stage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }
        });
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        root.getStyleClass().add("menu");

        Button button = new Button("Tetris");
        button.relocate(75, 10);

        
        button.setOnAction(event -> launchTetris());

        Button buttonPuzzle = new Button("Puzzle");
        buttonPuzzle.relocate(75, 130);
        buttonPuzzle.setOnAction(event -> launchPuzzle());



        root.getChildren().addAll(button, buttonPuzzle);


        return root;

    }

    private void launchPuzzle() {
        PuzzleGame puzzleGame = new PuzzleGame(this);
        stage.setScene(puzzleGame.start());

        center();
    }

    private void center() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    private void launchTetris() {
        TetrisGame tetrisGame = new TetrisGame(this);

        this.stage.setScene(tetrisGame.start());
        center();
    }

    @Override
    public void reset(Scene s) {
        stage.setScene(s);
    }

    @Override
    public void launchAI(Scene s) {
        stage.setScene(s);
    }

    @Override
    public void goBackToMenu() {
        this.stage.setScene(new Scene(createContent()));
        createHandlers();
        stage.getScene().getStylesheets().add("style/menu.css");
    }
}
