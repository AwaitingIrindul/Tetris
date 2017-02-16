package ModelBoard.Pieces;

import ModelBoard.Position.Position;

/**
 * Created by Irindul on 15/02/2017.
 */
public class Origin {

    private Block block;
    private Position position;


    public Origin(Block block, Position position) {
        this.block = block;
        this.position = position;
    }

    public Block getBlock() {
        return block;
    }

    public Position getPosition() {
        return position;
    }
}
