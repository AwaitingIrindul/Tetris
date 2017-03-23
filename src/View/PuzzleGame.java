package View;

import ModelBoard.Direction;
import ModelBoard.Position.Position;
import ModelPuzzle.Puzzle;
import View.ViewBoard.BoardView;
import View.ViewBoard.PieceView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.geom.Point2D;
import java.util.HashMap;

/**
 * Created by Irindul on 15/03/2017.
 */
public class PuzzleGame extends Application{
    public static int TILE_SIZE = 80;
    public static int WIDTH = 6 * TILE_SIZE;
    public static double HEIGHT = 6 * TILE_SIZE;

    private double time = 0;
    private PieceView goal;
    private BoardView bv;
    private Puzzle game;
    private Stage primaryStage;
    private boolean dragging = false;
    private HashMap<Rectangle, Position> cliked;

    private int i;
    private int j;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.getStylesheets().add("style/puzzle.css");
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });

        createHandlers(scene);

        primaryStage.setTitle("Blocks puzzle game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
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
        final Point2D.Double dragDelta = new Point2D.Double();
        final Point2D.Double layout = new Point2D.Double();
        bv.addPiece(goal.getPiece(), goal);

        goal.getSquare().forEach(rect -> {
            rect.setArcHeight(15);
            rect.setArcWidth(15);

        });

        cliked = new HashMap<>();


        goal.setOnMousePressed(event -> {
            
            goal.getSquare().forEach(rectangle -> {

                Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                int x = (int) b.getMinX()/TILE_SIZE;
                int y = (int) b.getMinY()/TILE_SIZE;

                cliked.put(rectangle, new Position(x, y));

            });
            dragDelta.setLocation( (event.getSceneX()), (event.getSceneY()));
            layout.setLocation(goal.getLayoutX(), goal.getLayoutY());
            System.out.println(dragDelta.toString());

        });

        goal.setOnMouseDragged(event -> {
            dragging = true;
            
            game.setSelected(goal.getPiece());
            switch (goal.getPiece().getOrientation()){


                case VERTICAL:
                    double y = event.getSceneY() - dragDelta.getY();
                    goal.setLayoutY(layout.getY() + y);
                    break;
                case HORIZONTAL:
                    
                    double x = event.getSceneX() - dragDelta.getX();
                    boolean move = true;
                    for(Rectangle rectangle : goal.getSquare()){
                        Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                        Position tmp = new Position((int) b.getMaxX(), (int )b.getMaxY());
                        if(!tmp.equals(cliked.get(rectangle))) {
                            move = false;
                            break;
                        }
                    }

                    Direction direction;
                    if(!move){
                        
                        if(layout.getX() + x < goal.getLayoutX()){
                            move = game.checkSelected(Direction.LEFT);
                            direction = Direction.LEFT;
                        } else {
                            move = game.checkSelected(Direction.RIGHT);
                            direction = Direction.RIGHT;
                        }

                        if(move) {
                            System.out.println("j " + ++j);
                            
                            game.moveSelected(direction);
                            goal.getSquare().forEach(rectangle -> {
                                Bounds b = rectangle.localToScene(rectangle.getLayoutBounds());
                                Position tmp = new Position((int) b.getMaxX(), (int )b.getMaxY());

                                cliked.replace(rectangle, tmp);
                            });
                        }
                    }

                    if(move){
                        
                        goal.setLayoutX(layout.getX() + x);
                    }

                    break;
            }

        });

        goal.setOnMouseReleased(event -> {
            if(dragging){
                dragging = false;
            }
            cliked.clear();
            //goal.update();
        });


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
