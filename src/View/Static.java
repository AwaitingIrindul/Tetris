package View;

import ModelBoard.Board.Grid;
import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static View.TetrisGame.TILE_SIZE;

/**
 * Created by Irindul on 22/02/2017.
 */
public class Static implements DisplayBlock{

    Color color;
    BlockAggregate block;
    public static Grid grid;

    public Static(Color color, BlockAggregate block) {
        this.color = color;
        this.block = block;
    }


    public Static(Tetromino t){
        this(t.color, t.block);
    }
    @Override
    public void draw(GraphicsContext g){
        draw(color, Color.BLACK, g);
    }

    @Override
    public void undraw(GraphicsContext g){
        draw(Color.TRANSPARENT, Color.TRANSPARENT, g);
    }

    private void draw(Color c, Color s, GraphicsContext g){
        g.setFill(c);
        g.setStroke(s);
        int x, y;
        for(Block b : block.getBlocks()){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {

                    x = b.getPosition(i, j).getY();
                    y = b.getPosition(i, j).getX();

                    if(grid.isEmpty(y, x)){
                        g.setFill(Color.TRANSPARENT);
                        g.setStroke(Color.TRANSPARENT);
                    } else {
                        g.setFill(c);
                        g.setStroke(s);
                    }
                    g.strokeRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

        }
    }


}
