package ModelBoard.Pieces;

import ModelBoard.Direction;
import ModelBoard.Position.Position;

/**
 * Created by Irindul on 10/02/2017.
 */
public class Link {
    private Block block1;
    private Position posOnBlock1;
    private Block block2;
    private Direction direction;

    public Link(Block block1, Position posOnBlock1, Block block2, Direction direction) {
        this.block1 = block1;
        this.posOnBlock1 = posOnBlock1;
        this.block2 = block2;
        this.direction = direction;
        computeNewPosition();
    }

    public Link(Link link){
        this.block1 = new Block(link.block1);
        this.posOnBlock1 = new Position(link.posOnBlock1);
        this.block2 = new Block(link.block2);
        this.direction = link.direction;
    }

    public void computeNewPosition(){
        Position tmp = block1.getPosition(posOnBlock1.getX(), posOnBlock1.getY());
        block2.setPosition(direction.getNewPosition(tmp));

    }

    public boolean contains(Block block){
        return block1.equals(block);
    }

    public boolean on(Block block){
        return block2.equals(block);
    }
}
