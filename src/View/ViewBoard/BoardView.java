package View.ViewBoard;

import ModelBoard.Pieces.Piece;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irindul on 21/03/2017.
 */
public class BoardView {

    Group group;
    //List<PieceView> pieces = new ArrayList<>();
    HashMap<Piece, PieceView> pieces;


    public BoardView() {
        group = new Group();
        pieces = new HashMap<>();
    }

    public void addPiece(Piece piece, Color color, double tilesize, int offset){
        pieces.put(piece, new PieceView(color, piece, tilesize, offset));
    }

    public void clear(){
        group.getChildren().clear();
        pieces.clear();
    }

    public void clean(){

    }

    public void updatePiece(Piece piece) {

        pieces.get(piece).getSquare().forEach(
                rectangle -> group.getChildren().remove(rectangle));
        pieces.get(piece).update();
        group.getChildren().addAll(pieces.get(piece).getSquare());
    }

    public void updateAll(){
        group.getChildren().clear();
        pieces.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(pieceView -> {
                    pieceView.update();
                    group.getChildren().addAll(pieceView.getSquare());
                });
    }

    public Group getGroup(){
        return group;
    }

}
