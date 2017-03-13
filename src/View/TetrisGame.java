package View;

import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelTetris.Player.ArtificialIntelligence;
import ModelTetris.Player.Evaluator;
import ModelTetris.Tetris;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Irindul on 16/02/2017.
 */
public class TetrisGame extends Application implements GravityListener{

    public static final int TILE_SIZE = 40;
    public static final int WIDTH = 10 * TILE_SIZE;
    public static final int HEIGHT = 16 * TILE_SIZE;
    public static final int SCORE_HEIGHT = 16 * TILE_SIZE;
    public static final int SCORE_WIDTH = 10 * TILE_SIZE;
    public static final int NEXT_WIDTH = 4 * TILE_SIZE;
    public static final int NEXT_HEIGHT = 4 * TILE_SIZE;
    public boolean go;

    
    private Tetris tetris;


    private ArrayList<Tetromino> tetrominos;
    private Tetromino next;
    private GraphicsContext g;
    private  GraphicsContext gcNextPiece;
    private Stage primaryStage;
    private double time;
    private  AnimationTimer timer;
    private Label score;
    private  boolean artificialPlayer;
    private ArtificialIntelligence artificialIntelligence;

    private Tetromino current;

    private static double timerSpeed = 0.017;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        createHandlers(scene);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

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

            if(e.getCode() == KeyCode.LEFT){
                tetris.move(Direction.LEFT);
            }
            if(e.getCode() == KeyCode.RIGHT){
                tetris.move(Direction.RIGHT);
            }
            if(e.getCode() == KeyCode.DOWN){
                tetris.move(Direction.DOWN);
            }
            if(e.getCode() == KeyCode.UP){
                tetris.rotate();
            }
            if(e.getCode() == KeyCode.R){
                System.out.println();
                
                tetris.resolve();
            }

            render();

        });
    }

    private Parent createContent() {

        Pane root = new Pane();
        root.setPrefSize((WIDTH + SCORE_WIDTH), (HEIGHT));

        Canvas backgroundGame = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gcBackgroundGame = backgroundGame.getGraphicsContext2D();
        border(gcBackgroundGame);

        Canvas movingGame = new Canvas(WIDTH, HEIGHT);
        g = movingGame.getGraphicsContext2D();

        Canvas scoreBackground = new Canvas(SCORE_WIDTH, SCORE_HEIGHT);
        scoreBackground.setTranslateX(WIDTH);

        GraphicsContext g1 = scoreBackground.getGraphicsContext2D();
        border(g1);

        Canvas nextPieceBg = new Canvas(NEXT_WIDTH, NEXT_HEIGHT);
        nextPieceBg.setTranslateX(WIDTH + (SCORE_WIDTH - NEXT_WIDTH) / 2);
        nextPieceBg.setTranslateY(50);

        GraphicsContext gcNextPieceBg = nextPieceBg.getGraphicsContext2D();
        border(gcNextPieceBg);

        Canvas nextPiece = new Canvas(NEXT_WIDTH, NEXT_HEIGHT);
        nextPiece.setTranslateX(WIDTH + (SCORE_WIDTH - NEXT_WIDTH) / 2);
        nextPiece.setTranslateY(50);
        gcNextPiece = nextPiece.getGraphicsContext2D();


        Button reset = new Button("Reset");

        reset.setMinHeight(100);
        reset.setMinWidth(NEXT_WIDTH);
        reset.setTranslateX(WIDTH + (SCORE_WIDTH - NEXT_WIDTH) / 2);
        reset.setTranslateY(300);


        Button startAI = new Button("Start AI");

        startAI.setMinHeight(100);
        startAI.setMinWidth(NEXT_WIDTH);
        startAI.setTranslateX(WIDTH + (SCORE_WIDTH - NEXT_WIDTH) / 2);
        startAI.setTranslateY(400);


        score = new Label();
        score.setTranslateX(WIDTH + (SCORE_WIDTH - NEXT_WIDTH) / 2);
        score.setTranslateY(600);

        root.getChildren().addAll(movingGame);
        root.getChildren().addAll(backgroundGame);
        root.getChildren().addAll(nextPiece);
        root.getChildren().addAll(nextPieceBg);
        root.getChildren().addAll(scoreBackground);
        root.getChildren().add(reset);
        root.getChildren().add(startAI);
        root.getChildren().add(score);


        go = true;
        tetris = new Tetris();
        tetrominos = new ArrayList<>();
        next = new Tetromino(getRandomColor(), tetris.getNext());
        current = new Tetromino(getRandomColor(), tetris.getCurrent());
        tetris.addGravityListener(this);


        /*tetrominos.addAll(
                tetris.getBlocks().stream() //List to stream
                        //.Association to a new tetromino
                        .map(blockAggregate -> new Tetromino(getRandomColor(), blockAggregate))
                        //Returning a list
                        .collect(Collectors.toList())
        );*/


        render();


        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += timerSpeed;

                if(time >= 0.5 && go){
                    if(artificialPlayer){
                        artificialIntelligence.executeNextMove();
                        render();
                    }
                    update();
                    render();
                    time = 0;
                }
            }
        };


        reset.setOnAction(event -> resetGame());

        startAI.setOnAction(event -> {
            resetGame();

            primaryStage.getScene().setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.ESCAPE){
                    primaryStage.fireEvent(
                            new WindowEvent(
                                    primaryStage,
                                    WindowEvent.WINDOW_CLOSE_REQUEST
                            )
                    );
                }});
            artificialPlayer = true;
            artificialIntelligence = new ArtificialIntelligence(tetris,
                    //Evaluator.getRandomEvaluator());
                    new Evaluator(-0.7037403842532439, 0.2496640822533236, -0.007391819447286618, -0.05172343401406043
                    ));
            timer.start();
            timerSpeed = 0.25;

        });



        timer.start();
        return root;
    }

    private void resetGame(){
        tetris.quit();
        timer.stop();
        primaryStage.setScene(new Scene(createContent()));
        createHandlers(primaryStage.getScene());
        timerSpeed = 0.017;
        time = 0;
    }

    private void border(GraphicsContext g){
        g.setFill(Color.TRANSPARENT);
        g.setStroke(Color.BLACK);
        g.setLineWidth(5);
        g.strokeRect(0, 0, g.getCanvas().getWidth(), g.getCanvas().getHeight());
    }
    private void render() {

        g.clearRect( 0 , 0, WIDTH, HEIGHT);


        tetrominos.forEach(p -> p.draw(g));
        current.draw(g);

        gcNextPiece.clearRect(0, 0, gcNextPiece.getCanvas().getWidth(), gcNextPiece.getCanvas().getHeight());


        next.drawNext(gcNextPiece);

        score.setText(Integer.toString(tetris.getScore()));
    }

    private void update() {

        tetris.applyGravity();

        //go = ! tetris.isFinished();
       // if(!go){
         //   stopGame();
        //}
    }

    private void stopGame() {
        timer.stop();
        primaryStage.getScene().setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
            primaryStage.fireEvent(
                    new WindowEvent(
                            primaryStage,
                            WindowEvent.WINDOW_CLOSE_REQUEST
                    )
            );
        }});
    }


    private Color getRandomColor(){
        Random rd = new Random();

        int color;

        color = rd.nextInt(7);

        /*switch (color){
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
        }*/
        int r = rd.nextInt(255);
        int g = rd.nextInt(255);
        int b = rd.nextInt(255);
        return  Color.rgb(r, g, b);
    }


    @Override
    public void onMovement() {
        current.draw(g);
    }

    @Override
    public void moving() {
        current.undraw(g);
    }

    @Override
    public void onChangedNext() {
        tetrominos.add(current);
        current = next;
        next = new Tetromino(getRandomColor(), tetris.getNext());
        if (artificialPlayer){
            artificialIntelligence.setHasChanged(true);
        }
    }

    @Override
    public synchronized void onSweep() {
        current.draw(g);
        for(Tetromino t: tetrominos){
            t.draw(g);
        }
    }

    @Override
    public synchronized void sweeping() {
        current.undraw(g);
        for(Tetromino t: tetrominos){
            t.undraw(g);
        }
    }

    @Override
    public void onQuit() {
        stopGame();
    }
}
