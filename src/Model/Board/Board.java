package Model.Board;

import Model.Direction;
import Model.Pieces.Block;
import Model.Pieces.BlockAgregat;
import Model.Position.Position;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Irindul on 09/02/2017.
 */
public class Board {

    private Grid grid;
    private List<BlockAgregat> blockAgregats;



    public Board(int height, int width) {
        grid = new Grid(height, width);
        blockAgregats = new ArrayList<>();
    }

    public void addPiece(BlockAgregat piece){
        blockAgregats.add(piece);
    }


    private boolean checkMovement(Direction direction, int ind){
        boolean possible = true;
        Position pos;
        for (Block block: blockAgregats.get(ind).getBlocks()) {
            pos = direction.getNewPosition(block.getPosition());
            int i = pos.getX();
            int j = pos.getY();
            if(grid.isInRange(i, j)){
                if(!grid.isEmpty(pos.getX(), pos.getY())){
                    possible = false;
                }
            } else {
                possible = false;
            }

        }

        return possible;
    }

    public void movePiece(Direction direction, int i){

        if(checkMovement(direction, i)){
            BlockAgregat blocks = blockAgregats.get(i);
            Position pos;
            for(Block block : blocks.getBlocks()){
                pos = direction.getNewPosition(block.getPosition());
                block.setPosition(pos);
            }

        }



    }
}
