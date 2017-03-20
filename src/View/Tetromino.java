package View;

import ModelBoard.Pieces.Piece;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Irindul on 16/02/2017.
 */
public class Tetromino{

    //TODO Refactor view library;
    Color color;
    Piece piece;
    double tilesize;
    int offset;

    public Tetromino(Color color, Piece block, double tilesize, int offset) {
        this.color = color;
        this.piece = block;
        this.tilesize = tilesize;
        this.offset = offset;
    }

    public void setPosition(int x, int y){
        setPosition(x, y);
    }

    public void draw(GraphicsContext g){
        draw(color, Color.BLACK, g);
    }

    public void drawNext(GraphicsContext g){
        g.setFill(color);

        int minY = piece.getMinimumY();

        piece.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX();

            x-=minY;
            y+=1;
            g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
        });



    }

    // TODO: 15/03/2017 add offset in parameter (-2 for tetris, 0 for blocks ect..)
    public void draw(Color c, Color s, GraphicsContext g){
        g.setFill(c);
        g.setStroke(s);

        piece.getPositions().forEach(pos -> {
            int x, y;
            x = pos.getY();
            y = pos.getX(); //TODO Refactor with normal sense;
            y-=offset;
            g.strokeRect(x*tilesize, y*tilesize, tilesize, tilesize);
            g.fillRect(x*tilesize, y*tilesize, tilesize, tilesize);
        });

    }

    public Piece getPiece() {
        return piece;
    }

    public void undraw(GraphicsContext g) {

       piece.getPositions().forEach(pos -> {
           int x, y;
           x = pos.getY();
           y = pos.getX();

           y-=offset;

           g.clearRect(x*tilesize, y*tilesize, tilesize+0.5, tilesize+0.5);
       });
    }
}
