package View;

import Model.ModelBoard.Direction;
import Model.ModelBoard.Position.Position;
import Model.ModelPuzzle.Puzzle;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Irindul on 15/03/2017.
 */
public class PuzzleGame {
    public static int TILE_SIZE = 80;
    public static int WIDTH = 6 * TILE_SIZE;
    public static double HEIGHT = 6 * TILE_SIZE;

    private PieceView goal;
    private PieceView selected;
    private ArrayList<PieceView> pieces;
    private BoardView bv;
    private Puzzle game;
    private Scene scene;
    private boolean dragging = false;
    private HashMap<Rectangle, Position> cliked;

    private int i;
    private int j;
    private IMenu menu;

    public PuzzleGame(IMenu menu) {
        this.menu = menu;
    }

    public Scene start() {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/puzzle.css");
        createHandlers(scene);

        this.scene = scene;
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
        pieces = new ArrayList<>();

        game.getPieces().forEach(piece -> {
            bv.addPiece(piece, getRandomColor(), TILE_SIZE, 0);
        });

        goal = new PieceView(Color.RED, game.getGoal(), TILE_SIZE, 0);

        bv.addPiece(goal.getPiece(), goal);
        bv.getPieceViews().forEach(view -> {
            pieces.add(view);
            createHandler(view);
            view.setStroke(Color.TRANSPARENT);
        });


        goal.getSquare().forEach(rect -> {
            rect.setArcHeight(15);
            rect.setArcWidth(15);
        });

        cliked = new HashMap<>();

        goal.getStyleClass().add("block");

        bv.getPieceViews().forEach(pieceView ->
                pieceView.getSquare().forEach(rect -> {
                    rect.setArcHeight(15);
                    rect.setArcWidth(15);
            }));


        root.getChildren().add(bv.getGroup());

        return root;
    }


    public void createHandler(PieceView p){

        final Point2D.Double dragDelta = new Point2D.Double();
        final Point2D.Double layout = new Point2D.Double();

       /* p.setOnMousePressed(event -> {
            p.getSquare().forEach(rectangle -> {

                Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                int x = (int) b.getMinX()/TILE_SIZE;
                int y = (int) b.getMinY()/TILE_SIZE;

                cliked.put(rectangle, new Position(x, y));

            });
            dragDelta.setLocation( (event.getSceneX()), (event.getSceneY()));
            layout.setLocation(p.getLayoutX(), p.getLayoutY());
        });

     /*   p.setOnMouseDragged(event -> {
            dragging = true;

            game.setSelected(p.getPiece());
            switch (p.getPiece().getOrientation()){


                case VERTICAL:
                    double y = event.getSceneY() - dragDelta.getY();
                    p.setLayoutY(layout.getY() + y);
                    break;
                case HORIZONTAL:

                    double x = event.getSceneX() - dragDelta.getX();
                    boolean move = true;
                    for(Rectangle rectangle : p.getSquare()){
                        Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                        Position tmp = new Position((int) b.getMinX()/TILE_SIZE, (int )b.getMinY()/TILE_SIZE);
                        if(!tmp.equals(cliked.get(rectangle))) {
                            move = false;
                            break;
                        }
                    }

                    Direction direction;
                    if(!move){
                        System.out.println("e");
                        
                        if(layout.getX() + x < p.getLayoutX()){
                            move = game.checkSelected(Direction.LEFT);
                            direction = Direction.LEFT;
                        } else {
                            move = game.checkSelected(Direction.RIGHT);
                            direction = Direction.RIGHT;
                        }

                        if(move) {

                            game.moveSelected(direction);
                            p.getSquare().forEach(rectangle -> {
                                Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                                Position tmp = new Position((int) b.getMinX()/TILE_SIZE, (int )b.getMinY()/TILE_SIZE);

                                cliked.replace(rectangle, tmp);
                            });
                        }
                    }

                    if(move){
                        p.setLayoutX(layout.getX() + x);
                    }

                    break;
            }

        });

        p.setOnMouseReleased(event -> {
            if(dragging){
                dragging = false;
            }
            cliked.clear();
            goal.update();
        }); */


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
}
