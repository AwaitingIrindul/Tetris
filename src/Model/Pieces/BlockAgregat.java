package Model.Pieces;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irindul on 09/02/2017.
 */
public class BlockAgregat {

    private List<Block> blocks;

    public BlockAgregat() {
        blocks = new ArrayList<>();
    }

    public void add(Block block){
        blocks.add(block);
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
