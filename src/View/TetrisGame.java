package View;

import ModelBoard.Direction;
import ModelBoard.Observers.GravityListener;
import ModelBoard.Pieces.Piece;
import ModelTetris.Player.ArtificialIntelligence;
import ModelTetris.Player.Evaluator;
import ModelTetris.Tetris;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Irindul on 16/02/2017.
 */
public class TetrisGame extends Application implements GravityListener{

    //TODO refactor view.
    // TODO: 21/03/2017 Faire Thread ailleurs

    public static final int TILE_SIZE = 40;
    public static final int WIDTH = 10 * TILE_SIZE;
    public static final int HEIGHT = 16 * TILE_SIZE;
    public static final int SCORE_WIDTH = 10 * TILE_SIZE;
    public static final int NEXT_WIDTH = 4 * TILE_SIZE;
    public static final int NEXT_HEIGHT = 4 * TILE_SIZE;
    public boolean go;

    public int t = 0;
    private Tetris tetris;


    private Group nextGroup;
    private BoardView boardView;
    private Stage primaryStage;
    private double time;
    private  AnimationTimer timer;
    private Label score;
    private  boolean artificialPlayer;
    private ArtificialIntelligence artificialIntelligence;

    private static double timerSpeed = 0.017;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/tetris.css");
        createHandlers(scene);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setTitle("Blocks puzzle game");
        //primaryStage.setResizable(false);
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

           // render();

        });
    }

    public Parent createContent() {


        //Creating the different panes
        Pane root = new Pane();
        root.setPrefSize((WIDTH + SCORE_WIDTH), (HEIGHT));

        Pane  game = new Pane();
        game.setPrefSize(WIDTH, HEIGHT);
        game.getStyleClass().add("gamePane");

        Pane border = new Pane();
        game.setPrefSize(WIDTH, HEIGHT);
        game.getStyleClass().add("gamePane");


        Pane menu = new Pane();
        menu.setPrefSize(SCORE_WIDTH, HEIGHT);
        menu.relocate(WIDTH, 0);

        Pane nextPiece = new Pane();
        nextPiece.setPrefSize(NEXT_WIDTH, NEXT_HEIGHT);
        nextPiece.relocate((SCORE_WIDTH - NEXT_WIDTH)/2, 50);
        nextPiece.getStyleClass().add("nextPiecePane");



        //Creating buttons
        Button reset = new Button("Reset");
        reset.setMinHeight(100);
        reset.setMinWidth(NEXT_WIDTH);
        reset.setTranslateX((SCORE_WIDTH - NEXT_WIDTH) / 2);
        reset.setTranslateY(300);


        Button startAI = new Button("Start AI");
        startAI.setMinHeight(100);
        startAI.setMinWidth(NEXT_WIDTH);
        startAI.setTranslateX((SCORE_WIDTH - NEXT_WIDTH) / 2);
        startAI.setTranslateY(400);

        boardView = new BoardView();
        Group board = boardView.getGroup();
      //  board.getStyleClass().add("gamePane");
        //board.maxHeight(HEIGHT-15);

        //Adding every node to its root
        game.getChildren().addAll(board, border);
        menu.getChildren().addAll(reset, startAI);
        menu.getChildren().add(nextPiece);
        root.getChildren().add(game);
        root.getChildren().add(menu);

        /*tetrominos.addAll(
                tetris.getBlocks().stream() //List to stream
                        //.Association to a new tetromino
                        .map(blockAggregate -> new Tetromino(getRandomColor(), blockAggregate))
                        //Returning a list
                        .collect(Collectors.toList())
        ); */

        go = true;
        tetris = new Tetris();

        boardView.addPiece(tetris.getCurrent(), getRandomColor(), TILE_SIZE, 2);
       // current = new PieceView(getRandomColor(), tetris.getCurrent(), TILE_SIZE, 2);
        tetris.addGravityListener(this);


        //render();


        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += timerSpeed;

                if(time >= 0.5 && go){
                    if(artificialPlayer){
                        artificialIntelligence.executeNextMove();
                        //render();
                    }
                    update();
                    time = 0;
                }
            }
        };


        reset.setOnAction(event -> resetGame());

        startAI.setOnAction(event -> {
            resetGame();
            // TODO: 21/03/2017 Refactor in function
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
                    //new Evaluator(-0.7037403842532439, 0.2496640822533236, -0.007391819447286618, -0.05172343401406043
                    new Evaluator(-0.510066, 0.760666, -0.35663, -0.184483
                    ));
            timer.start();
            timerSpeed = 0.35;

        });



        timer.start();
        return root;
    }

    private void resetGame(){
        tetris.quit();
        timer.stop();
        boardView.clear();
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

      /*  g.clearRect( 0 , 0, WIDTH, HEIGHT);


        tetrominos.forEach(p -> p.draw(g));
        current.draw(g);

        gcNextPiece.clearRect(0, 0, gcNextPiece.getCanvas().getWidth(), gcNextPiece.getCanvas().getHeight());


        next.drawNext(gcNextPiece);

        score.setText(Integer.toString(tetris.getScore()));*/
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
        boardView.updatePiece(tetris.getCurrent());
    }


    @Override
    public void onChangedNext() {
        boardView.addPiece(tetris.getCurrent(), getRandomColor(), TILE_SIZE, 2);
        if (artificialPlayer){
            artificialIntelligence.setHasChanged(true);
        }
    }

    @Override
    public synchronized void onSweep() {
        Platform.runLater(() -> boardView.updateAll());
    }

    @Override
    public void onQuit() {
        stopGame();
        //// TODO: 21/03/2017 Game over
    }

    @Override
    public void onMovement(Piece p) {
        boardView.updatePiece(p);
    }

    @Override
    public void onCleanUp(Piece p) {
        boardView.clean(p);
    }
}
