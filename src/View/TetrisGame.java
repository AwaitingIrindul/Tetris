package View;

import Model.ModelBoard.Direction;
import Model.ModelBoard.Observers.GravityListener;
import Model.ModelBoard.Pieces.Piece;
import Model.ModelBoard.Position.Position;
import Model.ModelTetris.Player.ArtificialIntelligence;
import Model.ModelTetris.Player.Evaluator;
import Model.ModelTetris.Tetris;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.Random;

/**
 * Created by Irindul on 16/02/2017.
 * The view for the Tetris game
 */
public class TetrisGame implements GravityListener{



    private static final int TILE_SIZE = 40;
    private static final int WIDTH = 10 * TILE_SIZE;
    private static final int HEIGHT = 16 * TILE_SIZE;
    private static final int SCORE_WIDTH = 10 * TILE_SIZE;
    private static final int NEXT_WIDTH = 5 * TILE_SIZE;
    private static final int NEXT_HEIGHT = 6 * TILE_SIZE;
    private boolean go;

    private Tetris tetris;


    private Group nextGroup;
    private BoardView boardView;
    private PieceView next;
    private Scene scene;
    private double time;
    private  AnimationTimer timer;
    private Label score;
    private  boolean artificialPlayer;
    private boolean pause;
    private ArtificialIntelligence artificialIntelligence;

    private static double timerSpeed = 0.017;
    private IMenu menu;


    TetrisGame(IMenu menu) {
        this.menu = menu;
    }

    //  @Override
    Scene start() {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/tetris.css");
        createHandlers(scene);
        this.scene = scene;
        return scene;

    }

    private void createHandlers(Scene scene){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                stopGame();
                menu.goBackToMenu();
            }

            if(e.getCode() == KeyCode.ENTER){
               // e.consume();
                pause();
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

           // render();

        });
    }

    private void pause() {
        
        if(pause){
            timer.start();
            pause = false;
        } else {
            timer.stop();
            pause = true;
        }


    }

    private Parent createContent() {


        //Creating the different panes
        Pane root = new Pane();
        root.setPrefSize((WIDTH + SCORE_WIDTH), (HEIGHT));

        Pane  game = new Pane();
        game.setPrefSize(WIDTH, HEIGHT);
       // game.getStyleClass().add("gamePane");
        game.getStyleClass().add("dark");

        Pane border = new Pane();
        border.setPrefSize(WIDTH, HEIGHT);
        border.getStyleClass().add("gamePane");


        Pane menu = new Pane();
        menu.setPrefSize(SCORE_WIDTH, HEIGHT);
        menu.relocate(WIDTH, 0);
        menu.getStyleClass().add("dark");

        Pane nextPiece = new Pane();
        nextPiece.setPrefSize(NEXT_WIDTH, NEXT_HEIGHT);
        nextPiece.relocate((SCORE_WIDTH - NEXT_WIDTH)/2, 50);
        nextPiece.getStyleClass().add("nextPiecePane");

        nextGroup = new Group();



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

        score = new Label();
        score.relocate(100, 600);


        boardView = new BoardView("block");
        Group board = boardView.getGroup();
      //  board.getStyleClass().add("gamePane");
        //board.maxHeight(HEIGHT-15);

        //Adding every node to its root
        nextPiece.getChildren().add(nextGroup);
        game.getChildren().addAll(board, border);
        menu.getChildren().addAll(reset, startAI, score);
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
        tetris.addGravityListener(this);
        next = new PieceView(getRandomColor(), tetris.getNext(), TILE_SIZE, 0);
        next.getSquare().forEach(rectangle -> rectangle.getStyleClass().add("block"));
        drawNext();


        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += timerSpeed;

                if(time >= 0.5 && go){
                    if(artificialPlayer){
                        artificialIntelligence.executeNextMove();
                    }
                    update();
                    time = 0;
                }
            }
        };


        reset.setOnAction(event -> resetGame());

        startAI.setOnAction(event -> {
            resetGame();
            scene.setOnKeyPressed(e -> {
                if(e.getCode() == KeyCode.ESCAPE){
                    this.menu.goBackToMenu();
                }
                if(e.getCode() == KeyCode.ENTER){
                    pause();
                }
            });

            artificialPlayer = true;
            artificialIntelligence = new ArtificialIntelligence(tetris,
                    //Evaluator.getRandomEvaluator());
                    //new Evaluator(-0.7037403842532439, 0.2496640822533236, -0.007391819447286618, -0.05172343401406043
                    new Evaluator(-0.510066, 0.760666, -0.35663, -0.184483
                    ));
            timer.start();
            timerSpeed = 0.35;

            this.menu.launchAI(scene);

        });


        pause = false;
        timer.start();
        return root;
    }

    private void resetGame(){
        tetris.quit();
        timer.stop();
        boardView.clear();
        scene = new Scene(createContent());
        scene.getStylesheets().add("style/tetris.css");
        createHandlers(scene);
        timerSpeed = 0.017;
        time = 0;

        menu.reset(scene);
    }


    private void update() {

        tetris.applyGravity();
    }

    private void stopGame() {
        timer.stop();
        tetris.stop();
    }


    private Color getRandomColor(){
        Random rd = new Random();

        int color;

        color = rd.nextInt(7);

        switch (color){
            case 0:
                return  Color.rgb(144, 198, 149);
            case 1:
                return  Color.rgb(104, 195, 163);
            case 2:
                return  Color.rgb(3, 201, 169);
            case 3:
                return Color.rgb(248, 148, 6);
            case 4:
                return Color.rgb(219, 10, 91);
            case 5:
                return Color.rgb(102, 51, 153);
            case 6:
                return Color.rgb(65, 131, 215);
            default:
                return Color.BLACK;
        }

        //
        //


    }


    @Override
    public void onMovement() {
        boardView.updatePiece(tetris.getCurrent());
    }


    @Override
    public void onChangedNext() {
        boardView.addPiece(tetris.getCurrent(), next.getColor(), TILE_SIZE, 2);
        next = new PieceView(getRandomColor(), tetris.getNext(), TILE_SIZE, 0);
        next.getSquare().forEach(rectangle -> rectangle.getStyleClass().add("block"));
        drawNext();

        if (artificialPlayer){
            artificialIntelligence.setHasChanged();
        }
    }

    private void drawNext() {
        nextGroup.getChildren().clear();
        int maxPieceY = next.getPiece().getPositions().stream().
        max(Comparator.comparingInt(Position::getY)).get().getY();

        int maxPieceX = next.getPiece().getPositions().stream().
                max(Comparator.comparingInt(Position::getX)).get().getX();

        int offsetY = (5 - maxPieceY)/2;
        int offsetX = (5 - maxPieceX)/2;

        next.getSquare().forEach(rectangle -> rectangle.relocate(rectangle.getLayoutX() + TILE_SIZE*offsetY, rectangle.getLayoutY() + TILE_SIZE*offsetX));

        next.getSquare().forEach(rectangle -> rectangle.setStroke(Color.BLACK));
        nextGroup.getChildren().addAll(next.getSquare());
    }

    @Override
    public synchronized void onSweep() {
        //boardView.updateAll();
        Platform.runLater(() -> boardView.updateAll());
        Platform.runLater(() -> score.setText(Integer.toString(tetris.getScore())));
    }

    @Override
    public void onQuit() {
        stopGame();
    }

    @Override
    public void onCleanUp(Piece p) {
        boardView.clean(p);
    }

    @Override
    public void update(Piece p) {
        Platform.runLater(() -> boardView.updatePiece(p));

    }
}
