package View;

import ModelBoard.Direction;
import ModelPuzzle.Puzzle;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
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
    private Tetromino goal;
    private GraphicsContext gc;
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
            
            
            render();
        });

        scene.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.ANY);
            System.out.println("over");
            
        });
        scene.setOnDragDetected(e -> {
            System.out.println("Selected at");

            //Getting board coordinates for x and y. (to swap or not if I fixed everything)
            System.out.println((int) (e.getX() - (WIDTH - 5*TILE_SIZE)/2) /TILE_SIZE);
            System.out.println((int) (e.getY() - (HEIGHT - 5*TILE_SIZE)/2) / TILE_SIZE);
            
        });

        scene.setOnDragDone(event -> {
            System.out.println("done");
        });

        scene.setOnDragEntered(event -> {
            System.out.println("entered");
        });



    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);

        Canvas canvas = new Canvas(5*TILE_SIZE, 5*TILE_SIZE);
        canvas.setTranslateX((WIDTH - 5*TILE_SIZE)/2);
        canvas.setTranslateY((HEIGHT - 5*TILE_SIZE)/2);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        border(gc);

        Canvas moving = new Canvas(5*TILE_SIZE, 5*TILE_SIZE);
        moving.setTranslateX((WIDTH - 5*TILE_SIZE)/2);
        moving.setTranslateY((HEIGHT - 5*TILE_SIZE)/2);
        this.gc = moving.getGraphicsContext2D();

        Puzzle puzzle = new Puzzle();
        goal = new Tetromino(Color.BURLYWOOD, puzzle.getGoal(), TILE_SIZE, 0);

        root.getChildren().addAll(moving);
        root.getChildren().addAll(canvas);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;
                
                if(time >= 0.5){
                    render();
                    time = 0;
                }
            }


        };
        timer.start();

        return root;
    }

    private void render() {
        //gc.clearRect( 0 , 0, WIDTH, HEIGHT);
        goal.draw(gc);
    }


    private void border(GraphicsContext g){
        g.setFill(Color.TRANSPARENT);
        g.setStroke(Color.BLACK);
        g.setLineWidth(5);
        g.strokeRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
    }
}
