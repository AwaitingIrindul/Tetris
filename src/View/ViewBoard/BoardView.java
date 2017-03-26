package View.ViewBoard;

import Model.ModelBoard.Pieces.Piece;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Irindul on 21/03/2017.
 */
public class BoardView {

    Group group;
    //List<PieceView> pieces = new ArrayList<>();
    HashMap<Piece, PieceView> pieces;

    String style;
    public BoardView() {
        group = new Group();
        pieces = new HashMap<>();
        style = null;
    }

    public BoardView(String style){
        this();
        this.style = style;
    }

    public void addPiece(Piece piece, Color color, double tilesize, int offset){
        PieceView view = new PieceView(color, piece, tilesize, offset);
        if (style != null) {
            view.getSquare().forEach(rectangle -> rectangle.getStyleClass().add(style));
        }
        group.getChildren().add(view);
        pieces.put(piece, view);
    }

    public void addPiece(Piece piece, PieceView pieceView){
        group.getChildren().add(pieceView);
        pieces.put(piece, pieceView);
    }


    public List<PieceView> getPieceViews(){

        return pieces.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
    public void clear(){
        group.getChildren().clear();
        pieces.clear();
    }

    public void clean(){

    }

    public void updatePiece(Piece piece) {

        pieces.get(piece).getChildren().clear();
        pieces.get(piece).update();

        /*pieces.get(piece).getSquare().forEach(
        rectangle -> group.getChildren().remove(rectangle));
        pieces.get(piece).update();
        pieces.get(piece).getSquare().forEach(rectangle -> rectangle.getStyleClass().add(style));
        group.getChildren().addAll(pieces.get(piece).getSquare());*/
    }

    public void updateAll(){
        //group.getChildren().clear();
        pieces.entrySet().stream()
                .map(Map.Entry::getKey)
                .forEach(this::updatePiece);
    }

    public Group getGroup(){
        return group;
    }

    public void clean(Piece p) {
        pieces.get(p).getSquare().forEach(
                rectangle -> group.getChildren().remove(rectangle));
    }
}
