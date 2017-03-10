package View;

import ModelBoard.Pieces.Piece;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static View.TetrisGame.TILE_SIZE;

/**
 * Created by Irindul on 16/02/2017.
 */
public class Tetromino{

    //TODO Refactor view library;
    Color color;
    Piece block;

    public Tetromino(Color color, Piece block) {
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

        int minY = block.getMinimumY();

        block.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX();

            x-=minY;
            y+=1;
            g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        });



    }

    public void draw(Color c, Color s, GraphicsContext g){
        g.setFill(c);
        g.setStroke(s);

        block.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX(); //TODO Refactor with normal sense;
            y-=2;
            g.strokeRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
            g.fillRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        });

    }

    public void undraw(GraphicsContext g) {

       block.getPositions().forEach(pos -> {
           int x, y;
           x = pos.getY();
           y = pos.getX();

           y-=2;

           g.clearRect(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE+0.5, TILE_SIZE+0.5);
       });
    }
}
