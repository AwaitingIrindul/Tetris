package View;

import Model.ModelBoard.Direction;
import Model.ModelPuzzle.Puzzle;
import Model.ModelPuzzle.PuzzleObserver;
import View.Clocking.Clock;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Irindul on 15/03/2017.
 * The view for the puzzle game
 */
class PuzzleGame implements PuzzleObserver{

    private static int TILE_SIZE = 80;
    private static int WIDTH = 6 * TILE_SIZE;
    private static int HEIGHT = 6 * TILE_SIZE;
    //private static int MENU_HEIGHT = 4 * TILE_SIZE;
    private static int MENU_WIDTH = 4 * TILE_SIZE;

    private PieceView selected;
    private TextArea nameInput;
    private Label bestScore;
    private Label name;
    private Button nextLevel;
    private Button submitScore;
    private Scene scene;
    private PieceView goal;
    private Label time;
    private BoardView bv;
    private Puzzle game;
    private IMenu menu;
    private Timeline timeline;
    private Clock clock;

    PuzzleGame(IMenu menu) {
        this.menu = menu;
    }

    Scene start() {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/puzzle.css");
        createHandlers(scene);
        clock = new Clock();
        time.setText(clock.toString());
        this.scene = scene;
        return scene;

    }

    private void createHandlers(Scene scene){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                quit();
                menu.goBackToMenu();
            }

            if(e.getCode() == KeyCode.DOWN){
                game.moveSelected(Direction.DOWN);
            }
            if(e.getCode() == KeyCode.UP){
                game.moveSelected(Direction.UP);
            }
            if(e.getCode() == KeyCode.LEFT){
                game.moveSelected(Direction.LEFT);
            }
            if(e.getCode() == KeyCode.RIGHT){
                game.moveSelected(Direction.RIGHT);
            }

            if(selected != null){
                selected.update();
                selected.getSquare().forEach(square -> {
                    square.setArcHeight(15);
                    square.setArcWidth(15);
                });

            }


        });

    }

    private void quit() {
        game.quit();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH + MENU_WIDTH, HEIGHT);

        Pane game = new Pane();
        game.setPrefSize(WIDTH, HEIGHT);
        //game.getStyleClass().add("gamePane");

        Pane menu = new Pane();
        menu.setPrefSize(MENU_WIDTH, HEIGHT);
        menu.setLayoutX(WIDTH);
        menu.getStyleClass().add("gamePane");


        Button reset = new Button("Reset");
        reset.setOnAction(event -> resetGame());
        reset.relocate(30, 100);

        nextLevel = new Button("Next Level");
        nextLevel.relocate(80, 20);
        nextLevel.setVisible(false);

        Label label = new Label("Time elapsed : ");
        //label.setLayoutY(50);

        time = new Label();
        time.setLayoutX(100);

        HBox tBox = new HBox();
        tBox.relocate(50, 100);
        tBox.getChildren().addAll(label, time);


        name = new Label("Please enter your name :");
        name.setVisible(false);

        nameInput  = new TextArea();
        nameInput.setVisible(false);
        nameInput.setPrefWidth(100);
        nameInput.setPrefHeight(20);

        submitScore = new Button("Submit score");
        submitScore.setVisible(false);

        bestScore = new Label("Best Score Obtained !");
        bestScore.getStyleClass().add("score");
        bestScore.setVisible(false);

        HBox box = new HBox();
        //box.relocate(30, 200);
        box.getChildren().addAll(nameInput, submitScore);

        VBox vBox = new VBox();
        vBox.relocate(30, 200);
        vBox.getChildren().addAll(reset, tBox, name, box, bestScore);


        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        bv = new BoardView();
        this.game = new Puzzle(this);

        this.game.getPieces().forEach(piece -> bv.addPiece(piece, getRandomColor(), TILE_SIZE, 0));

        goal = new PieceView(Color.RED, this.game.getGoal(), TILE_SIZE, 0);

        bv.addPiece(goal.getPiece(), goal);
        bv.getPieceViews().forEach(view -> {
            createHandler(view);
            view.setStroke(Color.TRANSPARENT);
        });


        goal.getSquare().forEach(rect -> {
            rect.setArcHeight(15);
            rect.setArcWidth(15);
        });

        goal.getStyleClass().add("block");

        bv.getPieceViews().forEach(pieceView ->
                pieceView.getSquare().forEach(rect -> {
                    rect.setArcHeight(15);
                    rect.setArcWidth(15);
            }));


        menu.getChildren().addAll(reset, label, time, nextLevel, vBox);
        game.getChildren().add(bv.getGroup());
        root.getChildren().addAll(game, menu);

        return root;
    }

    private void updateTime() {
        clock.increment();
        time.setText(clock.toString());
    }

    private void resetGame() {
        bv.clear();
        hideAll();
        clock.reset();
        time.setText(clock.toString());
        createHandlers(scene);
       // this.game = new Puzzle(this);
        this.game.reset();
        timeline.playFromStart();

        this.game.getPieces().forEach(piece -> bv.addPiece(piece, getRandomColor(), TILE_SIZE, 0));

        goal = new PieceView(Color.RED, this.game.getGoal(), TILE_SIZE, 0);

        bv.addPiece(goal.getPiece(), goal);
        bv.getPieceViews().forEach(this::pieceActions);
    }


    private void createHandler(PieceView p){
        p.setOnMouseClicked(event -> {
            game.setSelected(p.getPiece());
            selected = p;
        });
    }

    private Color getRandomColor(){
        Random rd = new Random();

        int color;

        color = rd.nextInt(7);

        switch (color){
            case 0:
                return  Color.rgb(143, 198, 149);
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
    public void onFinish() {
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
                menu.goBackToMenu();
            }
        });


        nameInput.setVisible(true);
        name.setVisible(true);
        submitScore.setVisible(true);
        submitScore.setOnAction(event -> {
            if(nameInput.getText() != null && !nameInput.getText().isEmpty())
                game.highscore(nameInput.getText(), clock.toSeconds());
        });


        nextLevel.setVisible(true);
        nextLevel.setOnAction(event -> {
            next();
        });

        timeline.stop();
    }

    @Override
    public void onNewBestScore() {
        bestScore.setVisible(true);
    }

    void hideAll(){
        bestScore.setVisible(false);
        nameInput.setVisible(false);
        name.setVisible(false);
        submitScore.setOnAction(event -> {});
        submitScore.setVisible(false);
    }

    public void next(){
        hideAll();
        hideAll();
        bv.clear();
        clock.reset();
        time.setText(clock.toString());
        createHandlers(scene);
        this.game.next();

        timeline.playFromStart();
        this.game.getPieces().forEach(piece -> bv.addPiece(piece, getRandomColor(), TILE_SIZE, 0));

        goal = new PieceView(Color.RED, this.game.getGoal(), TILE_SIZE, 0);

        bv.addPiece(goal.getPiece(), goal);
        bv.getPieceViews().forEach(this::pieceActions);
        nextLevel.setVisible(false);
        nextLevel.setOnAction(event -> {});
    }

    public void pieceActions(PieceView view ){
        createHandler(view);
        view.setStroke(Color.TRANSPARENT);
        view.getSquare().forEach(rect -> {
            rect.setArcHeight(15);
            rect.setArcWidth(15);
        });
    }
}
