package View.ViewBoard;

import ModelBoard.Pieces.Piece;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 16/02/2017.
 */
public class PieceView extends Parent{

    private Color color;
    private Color stroke;
    private Piece piece;
    private double tilesize;
    private int offset;

    private ObjectProperty<EventHandler<MouseEvent>> propertyOnAction = new SimpleObjectProperty<>();

    // private List<Rectangle> square;

    public PieceView(Color color, Piece piece, double tilesize, int offset) {
        this(color, piece, tilesize, offset, Color.BLACK);

    }

    public PieceView(Color color, Piece piece, double tilesize, int offset, Color stroke){
        super();
        this.color = color;
        this.piece = piece;
        this.tilesize = tilesize;
        this.offset = offset;
        //square = new ArrayList<>();

        piece.getPositions().stream()
                .map(position -> {
                    Rectangle rect = new Rectangle(this.tilesize, this.tilesize, this.color);
                    rect.relocate(position.getY()*this.tilesize, position.getX()*this.tilesize);
                    return rect;
                })
                .forEach(rectangle -> this.getChildren().add(rectangle));
        this.stroke = stroke;

        //onMouseClickedProperty().set(event -> System.out.println("Hello"));
    }

    public synchronized void update(){

        this.getChildren().clear();

        piece.getPositions().stream()
                .map(position -> {
                    Rectangle rect = new Rectangle(tilesize, tilesize, color);
                    rect.relocate(position.getY()*tilesize, (position.getX()-offset)*tilesize);
                    rect.setStroke(stroke);
                    return rect;
                })
                .forEach(rectangle -> this.getChildren().add(rectangle));
    }

    public List<Rectangle> getSquare() {
        List<Rectangle> square = new ArrayList<>();
        this.getChildren().forEach(rectangle -> square.add((Rectangle) rectangle));
        return square;
    }

    public Piece getPiece() {
        return piece;
    }

    public Color getColor(){
        return color;
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }


}
