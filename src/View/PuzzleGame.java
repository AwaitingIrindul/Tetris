package View;

import Model.ModelBoard.Direction;
import Model.ModelPuzzle.Puzzle;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Random;

/**
 * Created by Irindul on 15/03/2017.
 * The view for the puzzle game
 */
class PuzzleGame {

    private static int TILE_SIZE = 80;
    private static int WIDTH = 6 * TILE_SIZE;
    private static double HEIGHT = 6 * TILE_SIZE;

    private PieceView selected;
    private BoardView bv;
    private Puzzle game;
    private IMenu menu;

    PuzzleGame(IMenu menu) {
        this.menu = menu;
    }

    Scene start() {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/puzzle.css");
        createHandlers(scene);

        return scene;

    }

    private void createHandlers(Scene scene){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE){
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

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        bv = new BoardView();
        game = new Puzzle();

        game.getPieces().forEach(piece -> bv.addPiece(piece, getRandomColor(), TILE_SIZE, 0));

        PieceView goal = new PieceView(Color.RED, game.getGoal(), TILE_SIZE, 0);

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


        root.getChildren().add(bv.getGroup());

        return root;
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
}
