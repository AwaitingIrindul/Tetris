package View.ViewBoard;

import Model.ModelBoard.Pieces.Piece;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    public void setStroke(Color stroke) {
        this.stroke = stroke;
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }


}
