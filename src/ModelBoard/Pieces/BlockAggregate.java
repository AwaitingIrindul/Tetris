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
    private Origin origin;

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

    public void add(Block block, Block linker, Position pos1, Direction direction){
        if(blocks.isEmpty()){
            add(block);
        } else {
            blocks.add(block);



            links.add(new Link(linker, pos1, block, direction));
        }
    }

    public Block getBlock(Position p){
        for(Block b: blocks){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    if(b.getPosition(i, j).equals(p)){
                        return b;
                    }
                }
            }
        }

        throw new NullPointerException();
    }

    public void remove(Block b){
        blocks.remove(b);
    }

    public void remove(int i){
        blocks.remove(i);
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


    public boolean isInBlock(Position pos){
        for(Block block : blocks){
            if(block.isInBlock(pos)){
                return true;
            }
        }
        return false;
    }
    public void setOrigin(Origin origin){
        this.origin = origin;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void move(Direction d){
        for(Block b : blocks){
            b.move(d);
        }
    }

    public void setPosition(Position pos){
        setPosition(pos, 0);
        this.position = pos;
    }

    public int getMinimumY(){
        int min = blocks.get(0).getPosition().getY();

        for(Block b : blocks){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    int y = b.getPosition(i, j).getY();
                    if( y < min){
                        min = y;
                    }
                }
            }
        }

        return min;
    }
    public Position getPosition(){
        return position;
    }
}
