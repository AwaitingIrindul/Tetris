package View;

import ModelBoard.Direction;
import ModelBoard.Pieces.BlockAggregate;
import ModelTetris.Tetris;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Irindul on 16/02/2017.
 */
public class TetrisGame extends Application {

    public static final int TILE_SIZE = 40;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 16;
    
    private Tetris tetris;

    
    ArrayList<Tetromino> tetrominos;
    private GraphicsContext g;
    private double time;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.SPACE){

            }
            if(e.getCode() == KeyCode.ESCAPE){
                primaryStage.fireEvent(
                        new WindowEvent(
                                primaryStage,
                                WindowEvent.WINDOW_CLOSE_REQUEST
                        )
                );
            }

            if(e.getCode() == KeyCode.LEFT){
                tetris.move(Direction.LEFT);
            }
            if(e.getCode() == KeyCode.RIGHT){
                tetris.move(Direction.RIGHT);
            }
            if(e.getCode() == KeyCode.DOWN){
                tetris.move(Direction.DOWN);
            }

            render();

        });
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent createContent() {

        Pane root = new Pane();
        root.setPrefSize(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE);

        Canvas canvas = new Canvas(WIDTH*TILE_SIZE, HEIGHT*TILE_SIZE);
        g = canvas.getGraphicsContext2D();

        root.getChildren().addAll(canvas);
        tetris = new Tetris();
        tetrominos = new ArrayList<>();

        for(BlockAggregate b : tetris.getBlocks()){
            tetrominos.add(new Tetromino(getRandomColor(), b));
            // TODO: 16/02/2017 Change color whith randomizer
        }
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;

                if(time >= 0.5){
                    update();
                    render();
                    time = 0;
                }
            }
        };

        timer.start();
        return root;
    }

    private void render() {
        g.clearRect(0, 0, WIDTH*TILE_SIZE, HEIGHT *TILE_SIZE);

        tetrominos.forEach(p -> p.draw(g));

        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if(tetris.getGrid().isEmpty(i, j)){
                    System.out.print("0 ");
                } else
                    System.out.print("1 ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
        System.out.println();



        
    }

    private void update() {
        tetris.applyGravity();
        if(tetrominos.size() != tetris.getBlocks().size()){
            for (int i = tetrominos.size(); i < tetris.getBlocks().size(); i++) {
                tetrominos.add(new Tetromino(getRandomColor(), tetris.getBlocks().get(i)));
            }
        }
    }

    private Color getRandomColor(){
        Random rd = new Random();

        int color;

        color = rd.nextInt(7);

        switch (color){
            case 0:
                return Color.AQUA;
            case 1:
                return Color.BLUE;
            case 2:
                return Color.ORANGE;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.DARKGREEN;
            case 5:
                return Color.PURPLE;
            case 6:
                return Color.RED;
            default:
                return Color.BLACK;
        }

    }


}
