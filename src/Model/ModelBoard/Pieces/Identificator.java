package Model.ModelBoard.Pieces;

import java.util.ArrayList;

/**
 * Created by Irindul on 27/03/2017.
 */
public class Identificator {

    ArrayList<Piece> pieces = new ArrayList<>();


    public boolean add(Piece p){
        if(pieces.stream().filter(p::equals).count() == 0) {
            pieces.add(p);
            return true;
        }
        return false;
    }
}
