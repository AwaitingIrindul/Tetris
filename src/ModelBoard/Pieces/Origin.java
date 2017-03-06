package ModelBoard.Pieces;

import ModelBoard.Position.Position;

/**
 * Created by Irindul on 15/02/2017.
 */
public class Origin { //TODO Refactor with only an attribute position in block


    private Position position;


    public Origin(Position position) {
        this.position = position;
    }

    public Origin(Origin o){
        this(new Position(o.position));
    }


    public Position getPosition() {
        return position;
    }

}
