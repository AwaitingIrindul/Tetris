package ModelBoard.Pieces;

import ModelBoard.Direction;
import ModelBoard.Position.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Irindul on 09/02/2017.
 */
public class BlockAggregate {

    private Position position;
    private List<Block> blocks;
    private List<Link> links;

    public BlockAggregate() {
        blocks = new ArrayList<>();
        links = new ArrayList<>();
        position = new Position(0, 0);
    }

    public void add(Block block){
        if(blocks.isEmpty()) {
            blocks.add(block);
            position = block.getPosition();
        }
    }

    public void add(Block block, Position pos1, Direction direction){
        if(blocks.isEmpty()){
            add(block);
        } else {
            blocks.add(block);

            int i = blocks.indexOf(block);

            links.add(new Link(blocks.get(i-1), pos1, block, direction));
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    private void setPosition(Position pos, int i){

        if( i == blocks.size() - 1){
            for(Link link : links){
                if(link.on(blocks.get(i))){
                    link.computeNewPosition();
                }
            }
        } else {
            if(i == 0){
                blocks.get(i).setPosition(pos);
            }

            setPosition(pos, i++);
        }
    }

    public void setPosition(Position pos){
        setPosition(pos, 0);
        this.position = pos;
    }
}
