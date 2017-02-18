package ModelBoard.Board;

/**
 * Created by Irindul on 09/02/2017.
 */
public class Tile {

    private boolean isEmpty;


    public Tile() {
        isEmpty = true;
    }

    public Tile(Tile tile){
        this.isEmpty = tile.isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
