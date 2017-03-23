package View;

import ModelBoard.Board.Board;
import ModelPuzzle.Puzzle;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by Irindul on 15/03/2017.
 */
public class PuzzleGame extends Application{
    public static int TILE_SIZE = 80;
    public static int WIDTH = 10 * TILE_SIZE;
    public static double HEIGHT = 7.5 * TILE_SIZE;

    private double time = 0;
    private PieceView goal;
    private BoardView bv;
    private Puzzle game;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        createHandlers(scene);

        primaryStage.setTitle("Blocks puzzle game");
        primaryStage.setScene(scene);
        primaryStage.show();
        this.primaryStage = primaryStage;

    }

    private void createHandlers(Scene scene){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                primaryStage.fireEvent(
                        new WindowEvent(
                                primaryStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }

        });

    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        bv = new BoardView();
        game = new Puzzle();

        goal = new PieceView(Color.RED, game.getGoal(), TILE_SIZE, 0);

        bv.getGroup().getChildren().addAll(goal.getSquare());


        root.getChildren().add(bv.getGroup());

        return root;
    }


    private void border(GraphicsContext g){
        g.setFill(Color.TRANSPARENT);
        g.setStroke(Color.BLACK);
        g.setLineWidth(5);
        g.strokeRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
    }
}
