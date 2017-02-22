package View;

import ModelBoard.Pieces.Block;
import ModelBoard.Pieces.BlockAggregate;
import ModelBoard.Position.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static View.TetrisGame.TILE_SIZE;

/**
 * Created by Irindul on 16/02/2017.
 */
public class Tetromino implements  DisplayBlock{

    Color color;
    BlockAggregate block;

    public Tetromino(Color color, BlockAggregate block) {
        this.color = color;
        this.block  = block;
    }

    public void setPosition(int x, int y){
        setPosition(x, y);
    }

    public void draw(GraphicsContext g){
        draw(color, Color.BLACK, g);
    }

    public void drawNext(GraphicsContext g){
        g.setFill(color);
        int x, y;
        int minY = block.getMinimumY();
        for(Block b : block.getBlocks()){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    x = b.getPosition(i, j).getY();
                    y = b.getPosition(i, j).getX();

                    x-=minY;
                    y+=1;
                    g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

        }
    }

    public void draw(Color c, Color s, GraphicsContext g){
        g.setFill(c);
        g.setStroke(s);
        int x, y;
        for(Block b : block.getBlocks()){
            for (int i = 0; i < b.getHeight(); i++) {
                for (int j = 0; j < b.getWidth(); j++) {
                    x = b.getPosition(i, j).getY();
                    y = b.getPosition(i, j).getX();
                    g.strokeRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

        }
    }

    public void undraw(GraphicsContext g) {
       draw(Color.TRANSPARENT, Color.TRANSPARENT, g);
    }
}
